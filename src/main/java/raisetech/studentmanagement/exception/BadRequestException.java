package raisetech.studentmanagement.exception;

/**
 * クライアントのリクエストが不正な場合に投げる例外です。
 * HTTPステータス 400 Bad Request を返します。
 */
public class BadRequestException extends RuntimeException {
  //MEMO: 親クラスはRuntimeException→throwsが不要で扱いやすい。extendsは継承ね。

  // MEMO: messageはエラーメッセージ。この例外が発生した時に、一緒に出力するメッセージ。
  public BadRequestException(String message) {
    super(message);
  }
  //MEMO: causeは原因。causeに発生した原因を一緒にくっつけて表示させる。（色んなところに原因が発生しているとたくさん表示される。）
  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
