package raisetech.studentmanagement.exception;

/**
 * データが見つからない時に投げる例外です。
 * HTTPステータス 404 Not Found を返します。
 */
public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}