package com.example.final_project_java.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /*
    @RestControllerAdvice로 등록된 전역 예외 처리 방식은 Controller에서 발생된 예외만 처리합니다.
    Service, Repository, Filter에서 발생하는 예외는 처리하지 못합니다.
    ExpiredJwtException 처럼 security filter 단에서 발생하는 예외 타입은 애초에 요청 자체가
    Controller에 닿지 못하기 때문에 처리할 수 없습니다.

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleRuntimeException(ExpiredJwtException e) {
        log.info("ExpiredJwtException 발생!");
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    */

   @ExceptionHandler({RuntimeException.class, NoRegisteredArgumentException.class})
   public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().body(e.getMessage());
   }

   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
      log.info("handleIllegalArgumentException 호출!");
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<?> handleRuntimeException(Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
   }

}












































