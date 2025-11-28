package raisetech.studentmanagement.exception;

/**
 * テスト用の例外です。
 * HTTPステータス 400 Bad Request を返します。
 */
public class TestException extends RuntimeException{

  public TestException() {
    super();
  }

  // MEMO: messageはエラーメッセージ。この例外が発生した時に、一緒に出力するメッセージ。
  public TestException(String message) {
    super(message);
  }

  //MEMO: causeは原因。causeに発生した原因を一緒にくっつけて表示させる。（色んなところに原因が発生しているとたくさん表示される。）
  public TestException(String message, Throwable cause) {
    super(message, cause);
  }

  //MEMO: causeは原因。原因だけを貼っつけるやつ。
  public TestException(Throwable cause) {
    super(cause);
  }

}
