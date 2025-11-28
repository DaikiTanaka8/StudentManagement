package raisetech.studentmanagement.exception;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 * アプリケーション全体の例外をキャッチするハンドラです。
 * // MEMO: @ControllerAdvice → アプリケーション全体の例外をキャッチする。
 */
@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  //MEMO: private→このクラス内でしか使えない、外に出さないために使う。 static→クラス単位で１つあれば十分だからつける。 final→変更されるべきものではないためつける。
  // getLogger()の引数にクラスオブジェクト(◯◯.class)を渡すことで、そのクラス専用のロガーが作られる。「どのクラスで出たログか」が明確に記録できるようになる。

    /**
   * BadRequestException用のハンドラです（本番用）。
   * @param ex 実際に発生した例外オブジェクト。 //MEMO: ex.getMessage()で例外のメッセージが取れる。
   * @return 400 BAD_REQUESTを返している。 //MEMO: 「400 Bad Request」→クライアントの「リクエスト内容」が悪い（形式・バリデーション）、という意味。
   */
  @ExceptionHandler(BadRequestException.class) //MEMO: ハンドリングしたいクラスを指定する。
  public ResponseEntity<String> handleBadRequestException(BadRequestException ex) { //MEMO: 任意の方を指定できる（今回はString）。メソッド名はhandleで始めることが多い。引数はexと略すことが多い。
    logger.warn("BadRequestExceptionが発生しました: {}", ex.getMessage());
    //MEMO: {} はプレースホルダ（後で値が入る場所）。ex.getMessage() がここに埋め込まれる。例：「TestExceptionが発生しました: パラメータが不正です」

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    //MEMO: .statsでステータス指定、HTTPステータスでバッドリクエスト400番、.bodyで中身を作れる、今回はゲットメッセージでそのままメッセージを表示する。
  }

  /**
   * ResourceNotFoundException用のハンドラです。 //MEMO: ExceptionHandler→例外をハンドリングする、捕まえてコントロールする。
   * @param ex 実際に発生した例外オブジェクト。 //MEMO: ex.getMessage()で例外のメッセージが取れる。
   * @return 404NOT_FOUNDを返している。 //MEMO: 「404 Not Found」→ クライアントが探している「データ・URL」が存在しない、という意味。
   */
  @ExceptionHandler(ResourceNotFoundException.class) //MEMO: ハンドリングしたいクラスを指定する。
  public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
    logger.warn("リソースが見つかりません: {}", ex.getMessage());
    //MEMO: {} はプレースホルダ（後で値が入る場所）。ex.getMessage() がここに埋め込まれる。

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    //MEMO: .statsでステータス指定、HTTPステータスでノットファウンドリクエスト404番、.bodyで中身を作れる、今回はゲットメッセージでそのままメッセージを表示する。
  }

  /**
   * 予期しない例外(全ての例外)用のハンドラです。
   *
   * @param ex 実際に発生した予期しない例外オブジェクト。
   * @return 500INTERNAL_SERVER_ERRORを返している。
   */
  @ExceptionHandler(Exception.class) //MEMO: 「Exception」はJava全例外の親クラス。どんな例外もここで拾える。
  public ResponseEntity<String> handleGeneralException(Exception ex) {
    logger.error("予期しないエラーが発生しました", ex);
    //MEMO: 第２引数にexを渡して、例外スタックトレースも出力される。これにより、サーバー側で原因を特定しやすくしている。

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("サーバーエラーが発生しました。管理者に連絡してください。");
    //MEMO: セキュリティの観点から、内部エラー詳細は返さないのが正しい。
  }

  /**
   * TestException用のハンドラです(テスト・開発用)。 //MEMO: ExceptionHandler→例外をハンドリングする、捕まえてコントロールする。
   * @param ex 実際に発生した例外オブジェクト。 //MEMO: ex.getMessage()で例外のメッセージが取れる。
   * @return 400BAD_REQUESTを返している。 //MEMO: 「400 Bad Request」→クライアントの「リクエスト内容」が悪い（形式・バリデーション）、という意味。
   */
  @ExceptionHandler(TestException.class) //MEMO: ハンドリングしたいクラスを指定する。
  public ResponseEntity<String> handleTestException(TestException ex) { //MEMO: 任意の方を指定できる（今回はString）。メソッド名はhandleで始めることが多い。引数はexと略すことが多い。
    logger.warn("TestExceptionが発生しました: {}", ex.getMessage());
    //MEMO: {} はプレースホルダ（後で値が入る場所）。ex.getMessage() がここに埋め込まれる。例：「TestExceptionが発生しました: パラメータが不正です」

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    //MEMO: .statsでステータス指定、HTTPステータスでバッドリクエスト400番、.bodyで中身を作れる、今回はゲットメッセージでそのままメッセージを表示する。
  }

}
