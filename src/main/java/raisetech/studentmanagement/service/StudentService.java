package raisetech.studentmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 *  //MEMO: 業務処理：コントローラーがリクエストを受け取る→☆サービスは具体的な処理を行う。→DBで呼び出す。
 */
@Service //MEMO: これをつけることでSpringが認識してくれる。
public class StudentService {

  private StudentRepository repository;
  private StudentConverter converter;

  @Autowired //MEMO: Springが管理しているインスタンスやクラスを自動で管理してくれる。自動でインスタンス生成したり→自分でnewしなくて済むからコードがキレイ。コンストラクタインジェクション。
  public StudentService(StudentRepository repository,
      StudentConverter converter) { //MEMO: 上で書いた「private StudentRepository repository;」を呼び出すためにコンストラクターを生成している。インスタンス化する直前で書いている。
    this.repository = repository;   // ↑この引数は誰がどうやって持たせる？→newしてないのに使えているのは、SpringBootが自動で生成している。
    this.converter = converter;
  }

  /**
   * 受講生一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生一覧（全件）。全件検索した受講生情報一覧
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudents();
    List<StudentCourse> studentCoursesList = repository.searchStudentCourses(); //MEMO: この行と一つ上の行でで受講生情報と受講生コース情報の全件が取れてきている。
    return converter.convertStudentDetails(studentList, studentCoursesList);
    //MEMO: 「convertStudentDetails」→引数のstudentListとstudentCoursesListで全件取得を引っ張ってきて、それをコンバートしている。
  }

  /**
   * 受講生検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づく受講生コース情報を取得して設定します。 講義30でつくったやつ。IDで単一受講生を検索して取得するメソッド。
   *
   * @param studentId 受講生ID
   * @return IDで検索した受講生情報（単一の受講生情報）+受講生コース情報。（StudentDetailで返っている！）
   */
  public StudentDetail searchStudentById(String studentId) {
    Student student = repository.searchStudentById(studentId);
    List<StudentCourse> studentCourses = repository.searchStudentCoursesById(
        student.getStudentId());
    return new StudentDetail(student, studentCourses);
  }

  /**
   * 受講生情報を登録するメソッド。
   *
   * @param studentDetail StudentとStudentListの情報をまとめたクラス。
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！
  //MEMO: トランザクション管理、途中でエラーになったら登録内容を戻す。サービスに入れる。（片方登録されてもう片方は登録されない、というのを防ぐ。）
  // registerStudentをStudentDetailで返して、StudentIdを取れるようにする。
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    studentDetail.getStudent().setStudentId(UUID.randomUUID().toString()); //MEMO: ここでIDを設定。UUIDを自動採番。

    repository.registerStudent(studentDetail.getStudent()); //MEMO: ここで実際の登録処理。repositoryを呼び分ける。

    //MEMO: 今回はListで渡さないから、ループさせないといけない。
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      studentCourse.setCourseId(UUID.randomUUID().toString()); //MEMO: コースIDもUUIDで自動で設定するようにした。
      studentCourse.setStudentId(
          studentDetail.getStudent().getStudentId()); //MEMO: ここでさっき設定された受講生のIDを取ってくる。
      studentCourse.setStartDate(LocalDate.now());
      studentCourse.setEndDate(LocalDate.now().plusYears(1));
      repository.registerStudentCourse(studentCourse);
    }
    return studentDetail;
  }

  /**
   * 講義30で作った受講生更新メソッド。
   *
   * @param studentDetail StudentとStudentListの情報をまとめたクラス。
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！！
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    for (StudentCourse studentCourse : studentDetail.getStudentCourses()) {
      repository.updateStudentCourse(studentCourse);
    }
  }

  /**
   * 課題30で作った受講生削除メソッド。
   *
   * @param studentDetail StudentとStudentListの情報をまとめたクラス。
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！！
  public void deleteStudent(StudentDetail studentDetail) {
    repository.deleteStudent(studentDetail.getStudent());
  }

}