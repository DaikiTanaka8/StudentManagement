package raisetech.studentmanagement.controller;

import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.service.StudentService;

@Controller // @RestController(JSONで返していた)→@Controllerに変える。
public class StudentController {

  private StudentService service; // まずはサービスを持つ必要があるので、ここで記述。
  private StudentConverter converter; // ②プライベートでコンバーターを作る。

  @Autowired // オートワイヤードで自動で管理。コンストラクタインジェクション。
  public StudentController(StudentService service,
      StudentConverter converter) { // ①serviceに加えてconverterも使うので、引数にconverterを追加している。
    this.service = service; // コンストラクターを生成。フィールドを持ったやつを作る。
    this.converter = converter; // ③ここでコンストラクターを生成。
  }

  /**
   * 受講生情報一覧。ユーザーからすると「ほしい」だからgetにしている。
   *
   * @param model 受講生リスト.htmlとコントローラーの間にいるやつ
   * @return コンバートした受講生情報→受講生とその受講生が受講している受講生コース情報の一覧
   */
  @GetMapping("/studentList")
  public String getStudentList(Model model) {
    /*・Stringを返すようにする。→ JSONではリストをよしなにして表示していた。今回は文字列を返す必要がある。
      ・引数に"Model"が必要になる。Springframework.uiとしてmodel。*/
    List<Student> students = service.searchStudentList(); // 検索する側は個人名を固定して単一の受講生情報がほしいけど、実際は複数行検索されちゃうためListにしている。
    List<StudentCourse> studentCourses = service.searchStudentCourseList(); // この行と一つ上の行でで受講生情報と受講生コース情報の全件が取れてきている。

    model.addAttribute("studentList", converter.convertStudentDetails(students, studentCourses));
    // 「convertStudentDetails」→引数のstudentsとstudentCoursesで、上記のサービスの検索結果の全件取得を引っ張ってきている。それをコンバートしている。
    /*
     ・model.addAttributeで"studentList"の名前をつける。
     ・"studentList"の名前は、HTML（StudentList.html）の${studentList}の中のものと一致させる。
     ・モデル（model）は、受講生リスト.htmlとコントローラーの間にいるやつ、と思っていればいい。
     ・studentListの中身はこれだよ、という必要がある。→「converter.convertStudentDetails(students, studentCourses)」。今回はコンバータで追加したものだよ。
     */

    return "studentList"; // テンプレートエンジンの名前「ファイル名」を返す。「studentList.html」とつけているので、「studentList」としている。
    // →attributeNameや${}の中身と合わせているわけではない。
  }

  /**
   * 受講生コース情報一覧。ユーザーからすると「ほしい」だからgetにしている。
   *
   * @return 全件検索した受講生コース情報の一覧。
   */
  @GetMapping("/studentCourseList")
  public List<StudentCourse> getStudentCourseList() {
    return service.searchStudentCourseList(); //サービスからリストを呼び出している。
  }

  /**
   * IDで検索した単一の受講生情報。
   */
  @GetMapping("/updateStudent")
  public String getStudentById(@RequestParam String studentId, Model model) {
    model.addAttribute("searchStudent", service.searchStudentById(studentId));
    /*
     ・model.addAttributeで"searchStudent"の名前をつける。
     ・"searchStudent"の名前は、HTML（updateStudent.html）の${searchStudent}の中のものと一致させる。
     ・searchStudentの中身はこれだよ、という必要がある。→「service.searchStudentById(studentId)」。
     ・「service.searchStudentById(studentId)」は何を返しているのかというと…
     　→「public Student searchStudentById(String studentId) {return repository.searchStudentById(studentId);}」すなわちStudentオブジェクトを返している。
     */
    return "updateStudent"; // テンプレートエンジンの名前「ファイル名」を返す。「updateStudent.html」とつけているので、「updateStudent」としている。と思う。
    // →attributeNameや${}の中身と合わせているわけではない。
  }

  /**
   * まず一番始めにGetする必要がある。Getして画面表示するだけ。画面表示した後に更新する。更新する場合はまた別の処理。
   *
   * @param model これちょっと分からないかも。
   * @return 「registerStudent」を返している。一旦は「registerStudent.html」にくるよ、ということ。
   */
  @GetMapping("/newStudent") //「newStudent」にくると、一旦は「registerStudent.html」にくるよ、ということ。
  public String newStudent(Model model) {
    StudentDetail studentDetail = new StudentDetail();
    studentDetail.setStudentCourse(
        Arrays.asList(new StudentCourse())); // ここでStudentCourseのリストを入れておかないと、受講生コース一覧が登録画面に表示されない。
    model.addAttribute("studentDetail",
        studentDetail); // 表示する時にもstudentDetailを使う。new StudentDetailで中身空っぽのものを入れておく。
    return "registerStudent";
  }

  /**
   * 登録処理。新規受講生情報を登録する。 「@ModelAttribute」でモデル（ここでいうStudentDetail）に値を入れますよ、と言っている。
   * 「BindingResult」で入力チェックをすることができる。タイムリーフとかでPOSTを受け取る時にセットで使うことが多い。
   *
   * @param studentDetail
   * @param result        エラーがあったらregisterStudentに返している。
   * @return studentLiseにリダイレクトする。一覧画面に飛ばしている。
   */
  @PostMapping("/registerStudent")
  public String registerStudent(@Valid @ModelAttribute StudentDetail studentDetail,
      BindingResult result) {
    if (result.hasErrors()) {
      return "registerStudent";
    }
    // 新規受講生情報を登録する処理を実装する。
    service.registerStudent(studentDetail); // Service層で書いたregisterStudentを呼び出している。
    // コース情報も一緒に登録できるように実装する。コースは単体でいい。
    // ↑と書いてあるが、このregisterStudentでコース情報も一緒に登録するから、別のメソッドは呼び出さなくていい。

    return "redirect:/studentList"; // studentLiseにリダイレクトする。一覧画面に飛ばしている。
  }

  @PostMapping("/updateStudent")
  public String updateStudent(@Valid @ModelAttribute Student student, BindingResult result, Model model) {
      if (result.hasErrors()) {
        model.addAttribute("searchStudent", student);
        return "updateStudent";
      }
      // ここで更新処理を実装する。
      service.updateStudent(student);
      return "redirect:/updateStudent?studentId=" + student.getStudentId();
  }

}
