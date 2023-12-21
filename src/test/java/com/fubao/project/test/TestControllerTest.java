package com.fubao.project.test;

import com.fubao.project.global.common.exception.ResponseCode;
import com.fubao.project.global.common.exception.CustomException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.test.web.servlet.MockMvc;

import static com.fubao.project.global.common.exception.ResponseCode.TEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @DisplayName("test 성공")
    void testSuccess() throws Exception {
        String json = "TEST 입니다";
        mockMvc.perform(post("/api/test/success")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    @DisplayName("test 실패")
    void testFail() {
        Assertions.assertThrows(CustomException.class, () -> {
            throw new CustomException(ResponseCode.TEST);
        });
    }
}
