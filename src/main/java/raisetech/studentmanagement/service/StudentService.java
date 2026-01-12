package raisetech.studentmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.StudentCourseStatus;
import raisetech.studentmanagement.domain.assembler.StudentCourseAssembler;
import raisetech.studentmanagement.domain.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。 受講生の検索や登録・更新処理を行います。
 *
 */
@Service
public class StudentService {

  private final StudentRepository repository;
  private final StudentConverter converter;
  private final StudentCourseAssembler studentCourseAssembler;

  @Autowired
  public StudentService(StudentRepository repository, StudentConverter converter, StudentCourseAssembler studentCourseAssembler) {
    this.repository = repository;
    this.converter = converter;
    this.studentCourseAssembler = studentCourseAssembler;
  }

  /**
   * 受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 受講生詳細一覧（全件）。
   */
  public List<StudentDetail> searchStudentList() {
    List<Student> studentList = repository.searchStudent();
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * コース申込状況の一覧検索です。
   *
   * @return コース申込状況一覧（全件）。
   */
  public List<StudentCourseStatus> searchStudentCourseStatusList(){
    List<StudentCourseStatus> studentCourseStatusList = repository.searchStudentCourseStatus();
    return studentCourseStatusList;
  }

  /**
   * 受講生コース情報（全件）とcourseIdをキーにした申込状況を結合します。
   *
   * @return コース申込状況を含んだ受講生コースリスト。
   */
  public List<StudentCourse> studentCourseListWithStatus(){
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<StudentCourseStatus> studentCourseStatusList =repository.searchStudentCourseStatus();

    return studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);
  }

  /**
   * 申込状況を含む受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return 申込状況を含む受講生詳細一覧（全件）。全件検索した受講生詳細情報一覧
   */
  public List<StudentDetail> searchStudentListWithStatus() {
    List<Student> studentList = repository.searchStudent();
    List<StudentCourse> studentCourseList = studentCourseListWithStatus();
    return converter.convertStudentDetails(studentList, studentCourseList);
  }

  /**
   * 受講生詳細検索です。
   *
   * @param studentId 受講生ID
   * @return IDで検索した受講生詳細情報（単一の受講生情報+受講生コース情報）。
   */
  public StudentDetail searchStudentById(String studentId) {
    Student student = repository.searchStudentById(studentId);
    List<StudentCourse> studentCourseList = repository.searchStudentCourseListById(student.getStudentId());
    return new StudentDetail(student, studentCourseList);
  }

  /**
   * コース申込状況の検索です。
   *
   * @param courseId 受講生コース情報ID。
   * @return IDで検索したコースの申込状況。
   */
  public StudentCourseStatus searchStudentCourseStatusById(String courseId){
    StudentCourseStatus studentCourseStatus = repository.searchStudentCourseStatusById(courseId);
    return studentCourseStatus;
  }

  /**
   * 受講生詳細検索です。コース申込状況が一緒になった受講生コース情報が取得されます。
   *
   * @param studentId 受講生ID
   * @return IDで検索した受講生詳細情報（単一の受講生情報+受講生コース情報+コース申込状況）。
   */
  public StudentDetail searchStudentByIdWithStatus(String studentId) {
    Student student = repository.searchStudentById(studentId);
    List<StudentCourse> studentCourseList = repository.searchStudentCourseListById(student.getStudentId());

    for (StudentCourse studentCourse : studentCourseList){
      StudentCourseStatus status = repository.searchStudentCourseStatusById(studentCourse.getCourseId());
      studentCourse.setCourseStatus(status);
    }

    return new StudentDetail(student, studentCourseList);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！
  //MEMO: トランザクション管理、途中でエラーになったら登録内容を戻す。サービスに入れる。（片方登録されてもう片方は登録されない、というのを防ぐ。）
  // registerStudentをStudentDetailで返して、StudentIdを取れるようにする。
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent(); //MEMO: 「studentDetail.getStudent()」という記述が何箇所かあったので、変数の導入でスッキリさせる。
    student.setStudentId(UUID.randomUUID().toString()); //MEMO: ここでIDを設定。UUIDを自動採番。

    repository.registerStudent(student); //MEMO: ここで実際の登録処理。repositoryを呼び分ける。
    studentDetail.getStudentCourseList().forEach(studentCourse -> { //MEMO: 今回はListで渡さないから、ループさせないといけない。
      initStudentCourse(studentCourse, student); //MEMO: ごちゃっとした処理はメソッドの抽出でまとめてスッキリさせる。
      repository.registerStudentCourse(studentCourse);
    });
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   * //MEMO: 勝手に作ったメソッドはprivateにしておく。使うメソッドのすぐ下に置いたり、一番下に置いたり、ケースバイケース。
   *
   * @param studentCourse 受講生コース情報
   * @param student 受講生
   */
  private void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDate now = LocalDate.now(); //MEMO: これも２箇所あったから変数の導入を行う。

    studentCourse.setCourseId(UUID.randomUUID().toString()); //MEMO: コースIDもUUIDで自動で設定するようにした。
    studentCourse.setStudentId(student.getStudentId()); //MEMO: ここでさっき設定された受講生のIDを取ってくる。
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！！
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
  }

  /**
   * 受講生詳細の削除（論理削除）を行います。
   *
   * @param studentId 受講生ID
   */
  @Transactional //MEMO: サービスで登録したり更新をしたり削除したりする時に必ずつける！！
  public void localDeleteStudent(String studentId) {
    repository.localDeleteStudent(studentId);
  }

}