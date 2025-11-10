package raisetech.studentmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

/**
 * 業務処理：コントローラーがリクエストを受け取る→☆サービスは具体的な処理を行う。→DBで呼び出す。 ・今はまだ複雑な処理をしたいわけではない。リポジトリを呼び出して、検索した結果を返したい。
 */
@Service // これをつけることでSpringが認識してくれる。
public class StudentService {

  private StudentRepository repository;

  @Autowired
  // Springが管理しているインスタンスやクラスを自動で管理してくれる。自動でインスタンス生成したり→自分でnewしなくて済むからコードがキレイ。コンストラクタインジェクション。
  public StudentService(
      StudentRepository repository) { // 上で書いた「private StudentRepository repository;」を呼び出すためにコンストラクターを生成している。インスタンス化する直前で書いている。
    this.repository = repository;      // ↑この引数は誰がどうやって持たせる？→newしてないのに使えているのは、SpringBootが自動で生成している。
  }

  /**
   * 受講生を検索する。サービスとしては検索をする→search
   *
   * @return 全件検索した受講生情報一覧
   */
  public List<Student> searchStudentList() {
    return repository.searchStudents();
  }

  /**
   * 受講生コース情報を検索する。サービスとしては検索をする→search
   *
   * @return 全件検索した受講生コース情報の一覧
   */
  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourses();
  }

  /**
   * 受講生情報を登録するメソッド。
   *
   * @param studentDetail StudentとStudentListの情報をまとめたクラス。
   */
  @Transactional // サービスで登録したり更新をしたり削除したりする時に必ずつける！！
  // トランザクション管理、途中でエラーになったら登録内容を戻す。サービスに入れる。（片方登録されてもう片方は登録されない、というのを防ぐ。）
  public void registerStudent(StudentDetail studentDetail) {
    // IDを設定
    studentDetail.getStudent().setStudentId(UUID.randomUUID().toString());
    // ここで実際の登録処理
    repository.registerStudent(studentDetail.getStudent()); // repositoryを呼び分ける。

    // studentDetail.getStudent().getStudentId(); // ここに値が入っているはず？？
    // TODO:ここでコース情報登録も行う。　まとめて書ける。(TODOって書くと色が変わるメモ！)
    // 今回はListで渡さないから、ループさせないといけない。
    for (StudentCourse studentCourse : studentDetail.getStudentCourse()) {
      studentCourse.setCourseId(UUID.randomUUID().toString()); // コースIDもUUIDで自動で設定するようにした。
      studentCourse.setStudentId(
          studentDetail.getStudent().getStudentId()); // ここでさっき設定された受講生のIDを取ってくる。
      studentCourse.setStartDate(LocalDate.now());
      studentCourse.setEndDate(LocalDate.now().plusYears(1));
      repository.registerStudentCourse(studentCourse);
    }
  }
}
