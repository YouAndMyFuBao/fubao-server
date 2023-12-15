package com.fubao.project;

import com.fubao.project.global.common.exception.CustomErrorCode;
import com.fubao.project.global.common.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TEST API", description = "TEST API 명세서")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class TestController {
    @Operation(summary = "test api success")
    @PostMapping("/success")
    public ResponseEntity<String> testSuccess() {
        log.info("success test api");
        return ResponseEntity.status(HttpStatus.OK).body(CustomErrorCode.TEST.getMessage());
    }

    @Operation(summary = "test api fail")
    @PostMapping("/fail")
    public ResponseEntity<String> testFail() {
        log.info("fail test api");
        throw new CustomException(CustomErrorCode.TEST);
    }
}