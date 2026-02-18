package raisetech.studentmanagement.exception;

/**
 * クライアントのリクエストが不正な場合に投げる例外です。
 * HTTPステータス 400 Bad Request を返します。
 */
public class BadRequestException extends RuntimeException {
  public BadRequestException(String message) {
    super(message);
  }
  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
