package com.fubao.project.global.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Util {
    @Value("${aws.bucket-name}")
    private String bucketName;
    @Value("${aws.cloud-front-distribution}")
    private String cloudFrontDistribution;
    @Value("${aws.profile}")
    private String profile;

    private final S3Client s3Client;

    public String uploadFileToS3(String s3Path, MultipartFile multipartFile) {
        File uploadFile;
        try {
            uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return upload(uploadFile, s3Path, StringUtils.getFilenameExtension(multipartFile.getOriginalFilename()), multipartFile.getContentType());

    }

    private String upload(File uploadFile, String dirName, String extension, String contentType) {
        String fileName = profile + "/" + dirName + "/" + UUID.randomUUID() + "." + extension;
        String uploadImageUrl = putS3(uploadFile, fileName, contentType);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName, String contentType) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .contentDisposition("inline")
                .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
                .build();
        s3Client.putObject(putObjectRequest, RequestBody.fromFile(uploadFile));
        return cloudFrontDistribution + fileName;
    }

    private void removeNewFile(File targetFile) {
        if (!targetFile.delete())
            log.info("파일이 삭제되었습니다.");
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + UUID.randomUUID());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    public void delete(String imageUrl) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(imageUrl.substring(cloudFrontDistribution.length()))
                .build();
        s3Client.deleteObject(deleteObjectRequest);
    }

}
