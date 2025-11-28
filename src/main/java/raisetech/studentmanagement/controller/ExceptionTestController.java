package raisetech.studentmanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.exception.BadRequestException;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.exception.TestException;


/**
 * 例外処理テスト専用のコントローラです。
 * ※本番環境では、このファイルを削除またはコメントアウトする。
 */
@RestController
@RequestMapping("/testException")
public class ExceptionTestController {

  /**
   * TestExceptionをテストするエンドポイントです。
   * アクセス: GET /testException/test
   * 期待結果: 400 Bad Request
   */
  @GetMapping("/test")
  public void throwTestException() {
    throw new TestException("TestExceptionのテストです。このエンドポイントは例外を発生させます。");
  }

  /**
   * BadRequestExceptionをテストするエンドポイントです。
   * アクセス: GET /testException/badRequest
   * 期待結果: 400 Bad Request
   */
  @GetMapping("/badRequest")
  public void throwBadRequestException() {
    throw new BadRequestException("BadRequestExceptionのテストです。不正なリクエストをシミュレートしています。");
  }

  /**
   * ResourceNotFoundExceptionをテストするエンドポイントです。
   * アクセス: GET /testException/notFound
   * 期待結果: 404 Not Found
   */
  @GetMapping("/notFound")
  public void throwResourceNotFoundException() {
    throw new ResourceNotFoundException("ResourceNotFoundExceptionのテストです。リソースが見つからない状況をシミュレートしています。");
  }

  /**
   * 予期しない例外をテストするエンドポイントです。
   * アクセス: GET /testException/error
   * 期待結果: 500 Internal Server Error
   */
  @GetMapping("/error")
  public void throwUnexpectedException() {
    throw new RuntimeException("予期しないエラーが発生しました(テスト用)");
  }

}
