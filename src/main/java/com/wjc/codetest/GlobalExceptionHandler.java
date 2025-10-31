package com.wjc.codetest;

import com.wjc.codetest.product.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * [리뷰] 구체적인 예외 처리 추가
 * [문제] RuntimeException 처리로 구체적인 예외 구분 불가
 * [개선안] 커스텀 예외 추가, 예외별 적절한 HTTP 상태 코드 반환, 에러 응답에 메시지 포함
 */
@Slf4j
@ControllerAdvice(value = {"com.wjc.codetest.product.controller"})
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProductNotFound(ProductNotFoundException e) {
        log.error("ProductNotFoundException: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> runTimeException(Exception e) {
        log.error("status :: {}, errorType :: {}, errorCause :: {}",
                HttpStatus.INTERNAL_SERVER_ERROR,
                "runtimeException",
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
