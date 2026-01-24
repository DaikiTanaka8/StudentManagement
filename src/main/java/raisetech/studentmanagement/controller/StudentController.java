package raisetech.studentmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.studentmanagement.data.StudentCourseStatus;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.StudentSearchCondition;
import raisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private final StudentService service;
  private Logger logger;

  /**
   * コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired //MEMO: オートワイヤードで自動で管理。コンストラクタインジェクション。
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   *
   * @return 受講生詳細一覧（全件）。
   */
  @Operation(
      summary = "受講生の一覧検索", //MEMO: 一言で説明。
      description = "受講生の一覧を検索します。", //MEMO: 詳しい説明。
      tags = {"student-controller" },//MEMO: カテゴリ分け。
      operationId = "searchStudentList" //MEMO: APIの識別子（自動生成コード用）。
  )
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  /**
   * 受講生詳細の条件検索です。
   *
   * @return 受講生詳細一覧（条件検索）。
   */
  @Operation(
      summary = "受講生の条件検索",
      description = "受講生の一覧を条件検索します。",
      tags = {"student-controller" },
      operationId = "searchStudentListByCondition"
  )
  @GetMapping("/studentList/search")
  public List<StudentDetail> getStudentListByCondition(@RequestBody StudentSearchCondition studentSearchCondition) {
    return service.searchStudentListByCondition(studentSearchCondition);
  }

  /**
   * コース申込状況を含む受講生詳細の一覧検索です。
   *
   * @return コース申込状況を含む受講生詳細一覧（全件）。
   */
  @Operation(
      summary = "コース申込状況を含む受講生の一覧検索",
      description = "コース申込状況を含む受講生の一覧を検索します。",
      tags = {"student-controller" },
      operationId = "searchStudentListWithStatus"
  )
  @GetMapping("/studentListWithStatus")
  public List<StudentDetail> getStudentListWithStatus() { return service.searchStudentListWithStatus(); }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @Operation(
      summary = "単一の受講生詳細の検索", //MEMO: 一言で説明。
      description = "指定したIDの受講生詳細情報を取得します。", //MEMO: 詳しい説明。
      tags = {"student-controller" }, //MEMO: カテゴリ分け。
      operationId = "searchStudentById", //MEMO: APIの識別子（自動生成コード用）。
      parameters = { //MEMO: クエリ/パスのパラメータ説明。
          @Parameter(
              name = "studentId", //MEMO: パラメータの名前。
              description = "取得したい受講生のID", //MEMO: パラメータの説明。
              required = true, //MEMO: 必須パラメータであることを示す。Swaggerでは※必須と表示される。
              in = ParameterIn.PATH //MEMO:「URL に含まれる /users/{id}」ですよ、と記述。
          )
      }
  )
  @GetMapping("/student/{studentId}") //MEMO: 単一の受講生の情報。「/student/1」みたいな表示になる。
  public StudentDetail getStudent(
      @PathVariable @Size(min = 1, max = 36) String studentId) { //MEMO: ID情報を持ってないといけない→「@PathVariable」：URLの一部から受け取るときに使う。今回は{student_id}だね。
    return service.searchStudentById(studentId);
  }

  /**
   * コース申込状況を含む受講生詳細検索です。 IDに紐づく任意の受講生詳細情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @Operation(
      summary = "コース申込状況を含む単一の受講生詳細の検索",
      description = "指定したIDのコース申込状況を含む受講生詳細情報を取得します。",
      tags = {"student-controller" },
      operationId = "searchStudentById",
      parameters = {
          @Parameter(
              name = "studentId",
              description = "取得したい受講生のID",
              required = true,
              in = ParameterIn.PATH
          )
      }
  )
  @GetMapping("/studentWithCourseStatus/{studentId}") //MEMO: 単一の受講生の情報。「/student/1」みたいな表示になる。
  public StudentDetail getStudentWithCourseStatus(
      @PathVariable @Size(min = 1, max = 36) String studentId) { //MEMO: ID情報を持ってないといけない→「@PathVariable」：URLの一部から受け取るときに使う。今回は{student_id}だね。
    return service.searchStudentByIdWithStatus(studentId);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生登録", //MEMO: 一言で説明。
      description = "受講生を登録します。", //MEMO: 詳しい説明。
      tags = {"student-controller" }, //MEMO: カテゴリ分け。
      operationId = "registerStudent", //MEMO: APIの識別子（自動生成コード用）。
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( //MEMO: 「@RequestBody」とだけ書くとSpringのリクエストボディが選ばれてしまうからフルパスで記述。
          description = "登録したい受講生詳細情報",
          required = true,
          content = @Content( //MEMO: レスポンス（またはリクエスト）のContent-Typeとデータ構造（スキーマ）の説明。
              mediaType = "application/json", //MEMO: APIレスポンス形式がJSONですよ。
              schema = @Schema(implementation = StudentDetail.class) //MEMO: 「このAPIのレスポンスはStudentDetailクラスの形のJSONです」ってこと。
          )
      )
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(
        studentDetail); //MEMO: Service層で書いたregisterStudentを呼び出している。
    return ResponseEntity.ok(
        responseStudentDetail); //MEMO: うまくいったら「responseStudentDetail」を返す。studentIdを知りたいので。
  }

  /**
   * 受講生詳細の更新を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  //MEMO: 講義30で作った受講生更新メソッド。 登録処理。新規受講生情報を登録する。 「@RequestBody」でStudentDetailが飛んできますよ。というのは変わらない。
  // 画面を返すわけではないので、何も返さない。→ResponseEntityを返す。POSTなので結果がない、ただ何も返さないと困るので、更新を成功したのか失敗したのかを返す。
  @Operation(
      summary = "受講生詳細の更新", //MEMO: 一言で説明。
      description = "受講生詳細の更新を行います。", //MEMO: 詳しい説明。
      tags = {"student-controller" }, //MEMO: カテゴリ分け。
      operationId = "updateStudent", //MEMO: APIの識別子（自動生成コード用）。
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( //MEMO: 「@RequestBody」とだけ書くとSpringのリクエストボディが選ばれてしまうからフルパスで記述。
          description = "更新したい受講生詳細情報",
          required = true,
          content = @Content( //MEMO: レスポンス（またはリクエスト）のContent-Typeとデータ構造（スキーマ）の説明。
              mediaType = "application/json", //MEMO: APIレスポンス形式がJSONですよ。
              schema = @Schema(implementation = StudentDetail.class) //MEMO: 「このAPIのレスポンスはStudentDetailクラスの形のJSONです」ってこと。
          )
      )
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(
        "更新処理が成功しました。"); //MEMO: うまくいったらOKを返す。OKの中にBodyに何を入れますか？ということ。今回メッセージなので、Stringにしている。
  }

  /**
   * コース申込状況の更新を行います。
   *
   * @param studentCourseStatus コース申込状況
   * @return 実行結果
   */
  //MEMO: 講義30で作った受講生更新メソッド。 登録処理。新規受講生情報を登録する。 「@RequestBody」でStudentDetailが飛んできますよ。というのは変わらない。
  // 画面を返すわけではないので、何も返さない。→ResponseEntityを返す。POSTなので結果がない、ただ何も返さないと困るので、更新を成功したのか失敗したのかを返す。
  @Operation(
      summary = "コース申込状況の更新",
      description = "コース申込状況の更新を行います。",
      tags = {"student-controller" },
      operationId = "updateStudentCourseStatus",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( //MEMO: 「@RequestBody」とだけ書くとSpringのリクエストボディが選ばれてしまうからフルパスで記述。
          description = "更新したいコース申込状況",
          required = true,
          content = @Content( //MEMO: レスポンス（またはリクエスト）のContent-Typeとデータ構造（スキーマ）の説明。
              mediaType = "application/json", //MEMO: APIレスポンス形式がJSONですよ。
              schema = @Schema(implementation = StudentCourseStatus.class) //MEMO: 「このAPIのレスポンスはStudentDetailクラスの形のJSONです」ってこと。
          )
      )
  )
  @PutMapping("/updateStudentCourseStatus")
  public ResponseEntity<String> updateStudentCourseStatus(@Valid @RequestBody StudentCourseStatus studentCourseStatus) {
    service.updateStudentCourseStatus(studentCourseStatus);
    return ResponseEntity.ok(
        "更新処理が成功しました。"); //MEMO: うまくいったらOKを返す。OKの中にBodyに何を入れますか？ということ。今回メッセージなので、Stringにしている。
  }

  /**
   * 受講生詳細の削除（論理削除）を行います。 //MEMO: 課題30で作った受講生削除メソッド（自作）。studentDetail→studentIdに変更。
   * //MEMO:DeleteMappingにしたので、URLは「deleteStudent/ID→students/ID」に変更。Deleteの重複は非推奨。
   *
   * @param studentId 受講生ID
   * @return 実行結果
   */
  @Operation(
      summary = "受講生詳細の削除（論理削除）", //MEMO: 一言で説明。
      description = "指定したIDの受講生詳細情報を（論理）削除します。", //MEMO: 詳しい説明。
      tags = {"student-controller" }, //MEMO: カテゴリ分け。
      operationId = "deleteStudent", //MEMO: APIの識別子（自動生成コード用）。
      parameters = { //MEMO: クエリ/パスのパラメータ説明。
          @Parameter(
              name = "studentId", //MEMO: パラメータの名前。
              description = "削除したい受講生のID", //MEMO: パラメータの説明。
              required = true, //MEMO: 必須パラメータであることを示す。Swaggerでは※必須と表示される。
              in = ParameterIn.PATH //MEMO:「URL に含まれる /users/{id}」ですよ、と記述。
          )
      }
  )
  @DeleteMapping("/student/{studentId}")
  public ResponseEntity<String> deleteStudent(@PathVariable String studentId) {
    service.localDeleteStudent(studentId);
    return ResponseEntity.ok(
        "削除処理が成功しました。"); //MEMO: うまくいったらOKを返す。OKの中にBodyに何を入れますか？ということ。今回メッセージなので、Stringにしている。
  }

}
