package raisetech.studentmanagement.exception;

/**
 * データが見つからない時に投げる例外です。
 * HTTPステータス 404 Not Found を返します。
 */
public class ResourceNotFoundException extends RuntimeException {
  //MEMO: 親クラスはRuntimeException→throwsが不要で扱いやすい。extendsは継承ね。

  // MEMO: messageはエラーメッセージ。この例外が発生した時に、一緒に出力するメッセージ。
  public ResourceNotFoundException(String message) {
    super(message);
  }

  //MEMO: causeは原因。causeに発生した原因を一緒にくっつけて表示させる。（色んなところに原因が発生しているとたくさん表示される。）
  public ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}