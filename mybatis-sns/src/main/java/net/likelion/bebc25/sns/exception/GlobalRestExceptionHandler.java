package net.likelion.bebc25.sns.exception;

import lombok.extern.slf4j.Slf4j;
import net.likelion.bebc25.sns.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

// REST API 전역 예외 처리 클래스 (@ControllerAdvice + @ResponseBody 기능 내포)
@RestControllerAdvice
@Slf4j  // 로그 객체 생성
public class GlobalRestExceptionHandler {

    // 1. @Valid 유효성 검증 실패 예외 처리 (400 Bad Request)
    // 클라이언트가 전송한 DTO의 제약조건(@NotBlank, @Size 등) 위반 시 프레임워크가 발생시키는 예외를 처리함
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // 검증 오류 결과 컨테이너 추출
        BindingResult bindingResult = ex.getBindingResult();

        // 검증에 실패한 필드 목록을 순회하여 ApiErrorResponse 전용 DTO 구조로 변환
        List<ApiErrorResponse.FieldErrorDetail> fieldErrors = bindingResult.getFieldErrors().stream()
                .map(error -> new ApiErrorResponse.FieldErrorDetail(
                        error.getField(),                                                           // 실패한 입력 필드명
                        error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(), // 거부된 잘못된 입력값
                        error.getDefaultMessage()                                                   // 검증 실패 사유
                ))
                .toList();

        // 필드 오류 목록과 함께 ErrorCode 기반 400 Bad Request 에러 응답 객체 생성
        ApiErrorResponse response = ApiErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, fieldErrors);
        return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getHttpStatus()).body(response);
    }

    // 2. 비즈니스 업무 규칙 위반 예외 처리 (400 Bad Request)
    // 도메인 계층에서 정책 불일치, 상태 조건 불만족 등으로 인해 발생한 예외를 처리함
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleBusinessRuleException(IllegalArgumentException ex) {
        ApiErrorResponse response = ApiErrorResponse.of(ErrorCode.BUSINESS_RULE_VIOLATION, ex.getMessage());
        return ResponseEntity.status(ErrorCode.BUSINESS_RULE_VIOLATION.getHttpStatus()).body(response);
    }

    // 3. 자원을 찾지 못했을 때의 예외 처리 (404 Not Found)
    // 데이터베이스 조회 시 요청 ID에 해당하는 엔티티 데이터가 없을 때 발생하는 예외를 처리함
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NoSuchElementException ex) {
        ApiErrorResponse response = ApiErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(ErrorCode.RESOURCE_NOT_FOUND.getHttpStatus()).body(response);
    }

    // 4. 비즈니스 권한 및 상태 오류 예외 처리 (403 Forbidden)
    // 타인의 게시글을 수정/삭제하려 하거나 권한이 없는 자원에 접근할 때 발생하는 예외를 처리함
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleForbiddenException(IllegalStateException ex) {
        ApiErrorResponse response = ApiErrorResponse.of(ErrorCode.FORBIDDEN_OPERATION, ex.getMessage());
        return ResponseEntity.status(ErrorCode.FORBIDDEN_OPERATION.getHttpStatus()).body(response);
    }

    // 5. 기타 서버 내부 오류 처리 (500 Internal Server Error)
    // 애플리케이션에서 미처 처리하지 못한 모든 런타임 예외를 최종적으로 가로채어 일관된 형태로 응답함
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        log.error(ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus()).body(response);
    }
}