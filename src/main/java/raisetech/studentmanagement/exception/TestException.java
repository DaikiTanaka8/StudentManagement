package raisetech.studentmanagement.exception;

/**
 * テスト用の例外です。
 * HTTPステータス 400 Bad Request を返します。
 */
public class TestException extends RuntimeException{

  public TestException() {super();}

  public TestException(String message) {super(message);}

  public TestException(String message, Throwable cause) {super(message, cause);}

  public TestException(Throwable cause) {super(cause);}

}
