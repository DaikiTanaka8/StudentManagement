package raisetech.studentmanagement.controller;

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
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして受け付けるControllerです。
 */
@Validated //MEMO: これを書くことでControllerクラスで入力チェックをかけますよ、と言っている。
@RestController //MEMO: @RestController(JSONで返していた)→@Controllerに変える(HTML使用時）→再度RestControllerに変える
public class StudentController {

  private StudentService service; //MEMO: 受講生サービス。まずはサービスを持つ必要があるので、ここで記述。
  private Logger logger;

  /**
   * コンストラクタ
   *
   * @param service 受講生サービス
   */
  @Autowired //MEMO: オートワイヤードで自動で管理。コンストラクタインジェクション。
  public StudentController(StudentService service) {
    this.service = service; //MEMO: コンストラクターを生成。フィールドを持ったやつを作る。
  }

  /**
   * 受講生詳細の一覧検索です。 //MEMO: 受講生情報一覧。ユーザーからすると「ほしい」だからgetにしている。
   *
   * @return 受講生詳細一覧（全件）。 //MEMO: コンバートした受講生情報→受講生とその受講生が受講している受講生コース情報の一覧
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return service.searchStudentList();
  }

  //MEMO: 講義30で作った受講生更新メソッド。更新メソッドと言いつつ、単一受講生情報を取得して表示している。

  /**
   * 受講生検索です。 IDに紐づく任意の受講生情報を取得します。
   *
   * @param studentId 受講生ID
   * @return 受講生
   */
  @GetMapping("/student/{studentId}") //MEMO: 単一の受講生の情報。「/student/1」みたいな表示になる。
  public StudentDetail getStudent(
      @PathVariable @Size(min = 1, max = 36) String studentId) { //MEMO: ID情報を持ってないといけない→「@PathVariable」：URLの一部から受け取るときに使う。今回は{student_id}だね。
    return service.searchStudentById(studentId);
  }

  /**
   * 受講生詳細の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return 実行結果
   */
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
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@Valid @RequestBody StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok(
        "更新処理が成功しました。"); //MEMO: うまくいったらOKを返す。OKの中にBodyに何を入れますか？ということ。今回メッセージなので、Stringにしている。
  }


  /**
   * 受講生詳細の削除（論理削除）を行います。 //MEMO: 課題30で作った受講生削除メソッド（自作）。studentDetail→studentIdに変更。 //MEMO:
   * DeleteMappingにしたので、URLは「deleteStudent/ID→students/ID」に変更。Deleteの重複は非推奨。
   *
   * @param studentId 受講生ID
   * @return 実行結果
   */
  @DeleteMapping("/student/{studentId}")
  public ResponseEntity<String> deleteStudent(@PathVariable String studentId) {
    service.localDeleteStudent(studentId);
    return ResponseEntity.ok(
        "削除処理が成功しました。"); //MEMO: うまくいったらOKを返す。OKの中にBodyに何を入れますか？ということ。今回メッセージなので、Stringにしている。
  }

}
