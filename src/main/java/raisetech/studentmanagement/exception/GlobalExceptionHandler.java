package raisetech.studentmanagement.exception;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * アプリケーション全体の例外をキャッチするハンドラです。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
   * BadRequestException用のハンドラです（本番用）。
   * @param ex 実際に発生した例外オブジェクト。
   * @return 400 BAD_REQUESTを返している。
   */
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
    logger.warn("BadRequestExceptionが発生しました: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * TestException用のハンドラです(テスト・開発用)。
   * @param ex 実際に発生した例外オブジェクト。
   * @return 400BAD_REQUESTを返している。
   */
  @ExceptionHandler(TestException.class)
  public ResponseEntity<String> handleTestException(TestException ex) {
    logger.warn("TestExceptionが発生しました: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  /**
   * ResourceNotFoundException用のハンドラです。
   * @param ex 実際に発生した例外オブジェクト。
   * @return 404NOT_FOUNDを返している。
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
    logger.warn("リソースが見つかりません: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  /**
   * MethodArgumentNotValidException用のハンドラです。
   * @param ex 実際に発生した例外オブジェクト
   * @return 400BAD_REQUESTを返す
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
    logger.warn("バリデーションエラーが発生しました: {}", ex.getMessage());

    BindingResult bindingResult = ex.getBindingResult();

    String errorMessage = bindingResult.getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
  }

  /**
   * 予期しない例外(全ての例外)用のハンドラです。
   *
   * @param ex 実際に発生した予期しない例外オブジェクト。
   * @return 500INTERNAL_SERVER_ERRORを返している。
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    logger.error("予期しないエラーが発生しました", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("サーバーエラーが発生しました。管理者に連絡してください。");
  }
}
