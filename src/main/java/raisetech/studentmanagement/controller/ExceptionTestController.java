package raisetech.studentmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.exception.BadRequestException;
import raisetech.studentmanagement.exception.ResourceNotFoundException;
import raisetech.studentmanagement.exception.TestException;


/**
 * 例外処理テスト専用のコントローラです。 ※本番環境では、このファイルを削除またはコメントアウトする。
 */
@RestController
@RequestMapping("/testException")
public class ExceptionTestController {

  /**
   * TestExceptionをテストするエンドポイントです。 アクセス: GET /testException/test 期待結果: 400 Bad Request
   */
  @Operation(
      summary = "TestExceptionの動作確認",
      description = "このエンドポイントにアクセスすると強制的に TestException を発生させ、400 Bad Request を返します。",
      operationId = "testException"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "TestExceptionのテストです。このエンドポイントは例外を発生させます。"),
  })
  @GetMapping("/test")
  public void throwTestException() {
    throw new TestException("TestExceptionのテストです。このエンドポイントは例外を発生させます。");
  }

  /**
   * BadRequestExceptionをテストするエンドポイントです。 アクセス: GET /testException/badRequest 期待結果: 400 Bad Request
   */
  @Operation(
      summary = "BadRequestExceptionの動作確認",
      description = "このエンドポイントにアクセスすると強制的に BadRequestException を発生させ、400 Bad Request を返します。",
      operationId = "testBadRequestException"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "400", description = "BadRequestExceptionのテストです。不正なリクエストをシミュレートしています。"),
  })
  @GetMapping("/badRequest")
  public void throwBadRequestException() {
    throw new BadRequestException(
        "BadRequestExceptionのテストです。不正なリクエストをシミュレートしています。");
  }

  /**
   * ResourceNotFoundExceptionをテストするエンドポイントです。 アクセス: GET /testException/notFound 期待結果: 404 Not
   * Found
   */
  @Operation(
      summary = "ResourceNotFoundExceptionの動作確認",
      description = "このエンドポイントにアクセスすると強制的に ResourceNotFoundException を発生させ、400 Bad Request を返します。",
      operationId = "testResourceNotFoundException"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "404", description = "ResourceNotFoundExceptionのテストです。リソースが見つからない状況をシミュレートしています。"),
  })
  @GetMapping("/notFound")
  public void throwResourceNotFoundException() {
    throw new ResourceNotFoundException(
        "ResourceNotFoundExceptionのテストです。リソースが見つからない状況をシミュレートしています。");
  }

  /**
   * 予期しない例外をテストするエンドポイントです。 アクセス: GET /testException/error 期待結果: 500 Internal Server Error
   */
  @Operation(
      summary = "RuntimeExceptionの動作確認",
      description = "予期しないエラーを強制的に発生させ、500 Internal Server Error を返すテスト用エンドポイント。",
      operationId = "testUnexpectedException"
  )
  @ApiResponses({
      @ApiResponse(responseCode = "500", description = "予期しないエラーが発生しました(テスト用)。"),
  })
  @GetMapping("/error")
  public void throwUnexpectedException() {
    throw new RuntimeException("予期しないエラーが発生しました(テスト用)");
  }

}
