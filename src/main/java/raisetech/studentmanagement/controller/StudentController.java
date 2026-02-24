package raisetech.studentmanagement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
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

  /**
   * コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired
  public StudentController(StudentService service) {
    this.service = service;
  }

  /**
   * 受講生詳細の一覧検索です。
   *
   * @return 受講生詳細一覧（全件）。
   */
  @Operation(
      summary = "受講生の一覧検索",
      description = "受講生の一覧を検索します。",
      tags = {"student-controller" },
      operationId = "searchStudentList"
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
  @PostMapping("/studentList/search")
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
   * コース申込状況を含む受講生詳細の条件検索です。
   *
   * @return コース申込状況を含む受講生詳細一覧（条件検索）。
   */
  @Operation(
      summary = "コース申込状況を含む受講生の条件検索",
      description = "コース申込状況を含む受講生の一覧を条件検索します。",
      tags = {"student-controller" },
      operationId = "searchStudentListWithStatusByCondition"
  )
  @PostMapping("/studentListWithStatus/search")
  public List<StudentDetail> getStudentListWithStatusByCondition(@RequestBody StudentSearchCondition studentSearchCondition) {
    return service.searchStudentListWithStatusByCondition(studentSearchCondition);
  }

  /**
   * 受講生詳細検索です。 IDに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生詳細
   */
  @Operation(
      summary = "単一の受講生詳細の検索",
      description = "指定したIDの受講生詳細情報を取得します。",
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
  @GetMapping("/student/{studentId}")
  public StudentDetail getStudent(
      @PathVariable @Size(min = 1, max = 36) String studentId) {
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
      operationId = "searchStudentWithCourseStatusById",
      parameters = {
          @Parameter(
              name = "studentId",
              description = "取得したい受講生のID",
              required = true,
              in = ParameterIn.PATH
          )
      }
  )
  @GetMapping("/studentWithCourseStatus/{studentId}")
  public StudentDetail getStudentWithCourseStatus(
      @PathVariable @Size(min = 1, max = 36) String studentId) {
    return service.searchStudentByIdWithStatus(studentId);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生登録",
      description = "受講生を登録します。",
      tags = {"student-controller" },
      operationId = "registerStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "登録したい受講生詳細情報",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @Valid @RequestBody StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.registerStudent(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
  @Operation(
      summary = "受講生詳細の更新",
      description = "受講生詳細の更新を行います。",
      tags = {"student-controller" },
      operationId = "updateStudent",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したい受講生詳細情報",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StudentDetail.class)
          )
      )
  )
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * コース申込状況の更新を行います。
   *
   * @param studentCourseStatus コース申込状況
   * @return 実行結果
   */
  @Operation(
      summary = "コース申込状況の更新",
      description = "コース申込状況の更新を行います。",
      tags = {"student-controller" },
      operationId = "updateStudentCourseStatus",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          description = "更新したいコース申込状況",
          required = true,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StudentCourseStatus.class)
          )
      )
  )
  @PutMapping("/updateStudentCourseStatus")
  public ResponseEntity<String> updateStudentCourseStatus(@Valid @RequestBody StudentCourseStatus studentCourseStatus) {
    service.updateStudentCourseStatus(studentCourseStatus);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 受講生詳細の削除（論理削除）を行います。
   *
   * @param studentId 受講生ID
   * @return 実行結果
   */
  @Operation(
      summary = "受講生詳細の削除（論理削除）",
      description = "指定したIDの受講生詳細情報を（論理）削除します。",
      tags = {"student-controller" },
      operationId = "deleteStudent",
      parameters = {
          @Parameter(
              name = "studentId",
              description = "削除したい受講生のID",
              required = true,
              in = ParameterIn.PATH
          )
      }
  )
  @DeleteMapping("/student/{studentId}")
  public ResponseEntity<String> deleteStudent(@PathVariable String studentId) {
    service.localDeleteStudent(studentId);
    return ResponseEntity.ok("削除処理が成功しました。");
  }

}
