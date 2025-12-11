package raisetech.studentmanagement.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;
import raisetech.studentmanagement.service.StudentService;


@WebMvcTest(StudentController.class) //MEMO: 「StudentControllerだけ起動するWeb層のテストですよ」という宣言。テスト用のSpringBootが立ち上がる。
class StudentControllerTest {

  @Autowired
  private MockMvc mockMvc; //MEMO: SpringBootが用意しているMockの仕組み。HTTPリクエストを擬似的に投げるための仕組み。

  @Autowired
  private ObjectMapper objectMapper; //MEMO: JSONの文字列で変換する(自分で追加)

  @MockBean
  private StudentService service; //MEMO: Mock化したサービスを用意しておく。コントローラーはMockBean。

  @MockBean
  private StudentRepository repository; //MEMO: これがないとMyBatisのリポジトリを呼び出してしまう。だから、リポジトリもMock化してしまった。

  private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
  //MEMO: Bean Validation（Jakarta Validation / Hibernate Validator）を使うための“バリデータ本体”を生成するコード。
  // 「Javaのバリデーションを手動で実行できる Validator インスタンスを取得している」。Controller の @Valid の裏側で動いているやつを、自分で呼び出せるようにしている。


  @Test
  void 受講生詳細の一覧検索が実行できてからのリストが返ってくること() throws Exception {
    Student student = new Student();
    student.setStudentId("test-id-123");
    student.setName("テスト太郎");
    StudentDetail studentDetail = new StudentDetail(student, List.of());
    when(service.searchStudentList()).thenReturn(List.of(studentDetail));

    mockMvc.perform(get("/studentList")) //MEMO: テスト用の疑似HTTPクライアント（mockMvc）を使って、GET/studentListを実行。
        .andExpect(status().isOk()) //MEMO: Controllerからの戻り値（HTTP ステータスコード）が 200 OK であることを確認。
        .andExpect(content().json("""
                    [
                        {
                            "student": {
                                "studentId": "test-id-123",
                                "name": "テスト太郎"
                            },
                            "studentCourseList": []
                        }
                    ]
            """));

    verify(service, times(1)).searchStudentList();
  }

  @Test
  void 受講生詳細の受講生で適切な値を入力したときに入力チェックに異常が発生しないこと(){
    Student student = new Student();
    student.setStudentId("test-id-123");
    student.setName("テスト太郎");
    student.setFurigana("てすとたろう");
    student.setEmail("test@example.com");
    student.setCity("東京都");
    student.setAge(100);
    student.setGender("その他");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    //MEMO: studentに付けたバリデーションアノテーション（@NotNull, @Pattern, @Size など）が破られていないかチェックして、違反していた項目を全部返す。

    Assertions.assertEquals(0, violations.size()); //MEMO: 「違反は発生しないよ」という期待をテストしている。
    assertThat(violations.size()).isEqualTo(0); //MEMO: assertThatの方が直感的。
  }

  @Test
  void 受講生詳細の受講生でメールアドレスに不正な値を入力したときに入力チェックにかかること(){
    Student student = new Student();
    student.setName("テスト太郎");
    student.setFurigana("てすとたろう");
    student.setEmail("example.com");
    student.setCity("東京都");
    student.setAge(100);
    student.setGender("その他");

    Set<ConstraintViolation<Student>> violations = validator.validate(student);
    //MEMO: studentに付けたバリデーションアノテーション（@NotNull, @Pattern, @Size など）が破られていないかチェックして、違反していた項目を全部返す。

    Assertions.assertEquals(1, violations.size()); //MEMO: 「違反は１つだけ発生する」という期待をテストしている。
    assertThat(violations.size()).isEqualTo(1); //MEMO:「バリデーションエラーが 1 件だけ発生していること」を確認している。
    assertThat(violations).extracting("message").containsOnly("正しいメールアドレスを入力してください"); //MEMO: 「どんなエラーメッセージが出ているか」を検証している。

  }

  @Test
  void 受講生検索のID検索が実行したら受講生詳細が返ってくること() throws Exception {
    Student student = new Student();
    student.setStudentId("test-id-123");
    student.setName("テスト太郎");
    student.setFurigana("てすとたろう");
    student.setNickname("テストさん");
    student.setEmail("test@example.com");
    student.setCity("東京");
    student.setAge(100);
    student.setGender("その他");
    student.setRemark("");

    StudentDetail studentDetail = new StudentDetail(student,List.of());

    when(service.searchStudentById("test-id-123")).thenReturn(studentDetail);

    mockMvc.perform(get("/student/test-id-123"))
        .andExpect(status().isOk())
        .andExpect(content().json("""
            {
                "student": {
                    "studentId": "test-id-123",
                    "name": "テスト太郎",
                    "furigana": "てすとたろう",
                    "nickname": "テストさん",
                    "email": "test@example.com",
                    "city": "東京",
                    "age": 100,
                    "gender": "その他",
                    "remark": "",
                    "deleted": false
                },
                "studentCourseList": []
            }
            """));

    verify(service, times(1)).searchStudentById("test-id-123");

  }

  @Test
  void 受講生登録を実行したら登録した受講生詳細が返ってくること() throws Exception {
    // 事前準備
    // 送るデータ
    Student inputStudent = new Student();
    inputStudent.setStudentId("test-id-123");
    inputStudent.setName("登録テスト");
    inputStudent.setFurigana("とうろくてすと");
    inputStudent.setEmail("test@example.com");
    inputStudent.setCity("東京都");
    inputStudent.setAge(100);
    inputStudent.setGender("その他");

    StudentCourse course =new StudentCourse();
    course.setCourseName("登録テストコース");

    StudentDetail inputStudentDetail = new StudentDetail(inputStudent,List.of(course));

    // 返ってくるデータ
    Student respenseStudent = new Student();
    respenseStudent.setStudentId("test-id-123"); //MEMO: 自動生成されたID
    respenseStudent.setName("登録テスト");
    respenseStudent.setFurigana("とうろくてすと");
    respenseStudent.setEmail("test@example.com");
    respenseStudent.setCity("東京都");
    respenseStudent.setAge(100);
    respenseStudent.setGender("その他");

    StudentCourse responseCourse = new StudentCourse();
    responseCourse.setCourseName("登録テストコース");

    StudentDetail responseStudentDetail = new StudentDetail(respenseStudent,List.of(responseCourse));

    // Mock設定
    when(service.registerStudent(any(StudentDetail.class))).thenReturn(responseStudentDetail);
    //MEMO: any()を使うことで、「どんなStudentDetailでもOK」とする。

    // 実行 & 検証
    mockMvc.perform(post("/registerStudent")
        .contentType(MediaType.APPLICATION_JSON) //MEMO: Content-Typeを指定。
        .content(objectMapper.writeValueAsString(inputStudentDetail))) //MEMO: JSONの文字列を指定。StudentDetailをStringにしてくれている。
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(responseStudentDetail)));

    verify(service, times(1)).registerStudent(any(StudentDetail.class));

  }

  @Test
  void 受講生更新を実行したら更新成功メッセージが返ってくること() throws Exception {

    Student student = new Student();
    student.setName("更新テスト");
    student.setFurigana("こうしんてすと");
    student.setEmail("test@example.com");
    student.setCity("東京都");
    student.setAge(100);
    student.setGender("その他");

    StudentCourse course =new StudentCourse();
    course.setCourseName("更新テストコース");

    StudentDetail studentDetail = new StudentDetail(student,List.of(course));

    // 実行 & 検証
    mockMvc.perform(put("/updateStudent")
        .contentType(MediaType.APPLICATION_JSON) //MEMO: Content-Typeを指定。
        .content(objectMapper.writeValueAsString(studentDetail))) //MEMO: JSONの文字列を指定。StudentDetailをStringにしてくれている。
        .andExpect(status().isOk())
        .andExpect(content().string("更新処理が成功しました。"));

    verify(service, times(1)).updateStudent(any(StudentDetail.class));

  }

  @Test
  void 受講生更新で不正なメールアドレスを入力して実行したら500エラーが返ること() throws Exception {
    // MEMO: 現在はMethodArgumentNotValidExceptionのハンドラーがないため、GlobalExceptionHandlerのhandleGeneralExceptionで500エラーとなる。
    // TODO: 将来的には400エラーに修正することを検討。

    Student student = new Student();
    student.setName("更新テスト");
    student.setFurigana("こうしんてすと");
    student.setEmail("不正なアドレス"); // バリデーションエラー
    student.setCity("東京都");
    student.setAge(100);
    student.setGender("その他");

    StudentCourse course =new StudentCourse();
    course.setCourseName("更新テストコース");

    StudentDetail studentDetail = new StudentDetail(student,List.of(course));

    // 実行 & 検証
    mockMvc.perform(put("/updateStudent")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(studentDetail)))
        .andExpect(status().isInternalServerError()) //MEMO: 500エラーを期待
        .andExpect(content().string("サーバーエラーが発生しました。管理者に連絡してください。"));

    // MEMO: バリデーションエラーなのでserviceは呼ばれない
    verify(service, times(0)).updateStudent(any(StudentDetail.class));

  }

  @Test
  void 受講生詳細の論理削除を実行したら削除成功メッセージが返ってくること() throws Exception {
    Student student = new Student();
    student.setStudentId("12345");

    // 実行 & 検証
    mockMvc.perform(delete("/student/12345")
        .contentType(MediaType.APPLICATION_JSON) //MEMO: Content-Typeを指定。
        .content(objectMapper.writeValueAsString(student))) //MEMO: JSONの文字列を指定。StudentDetailをStringにしてくれている。
        .andExpect(status().isOk())
        .andExpect(content().string("削除処理が成功しました。"));

    verify(service, times(1)).localDeleteStudent("12345");

  }

}