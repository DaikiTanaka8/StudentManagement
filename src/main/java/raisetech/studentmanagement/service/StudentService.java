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

//  /**
//   * コース申込状況の一覧検索です。
//   *
//   * @return コース申込状況一覧（全件）。
//   */
//  public List<StudentCourseStatus> searchStudentCourseStatusList(){
//    List<StudentCourseStatus> studentCourseStatusList = repository.searchStudentCourseStatus();
//    return studentCourseStatusList;
//  }

  /**
   * コース申込状況を含む受講生コース情報の一覧検索です。
   *
   * @return コース申込状況を含む受講生コース一覧（全件）。
   */
  public List<StudentCourse> studentCourseListWithStatus(){
    List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
    List<StudentCourseStatus> studentCourseStatusList =repository.searchStudentCourseStatusList();

    return studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);
  }

  /**
   * コース申込状況を含む受講生詳細の一覧検索です。 全件検索を行うので、条件指定は行いません。
   *
   * @return コース申込状況を含む受講生詳細一覧（全件）。
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
   * コース申込状況を含む受講生詳細検索です。
   *
   * @param studentId 受講生ID
   * @return IDで検索した受講生詳細情報（単一の受講生情報+受講生コース情報+コース申込状況）。
   */
  public StudentDetail searchStudentByIdWithStatus(String studentId) {
    Student student = repository.searchStudentById(studentId);
    List<StudentCourse> studentCourseList = repository.searchStudentCourseListById(student.getStudentId());
    List<StudentCourseStatus> studentCourseStatusList = repository.searchStudentCourseStatusList();
    List<StudentCourse> assembledList = studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);

    return new StudentDetail(student, assembledList);
  }

  /**
   * 受講生詳細の登録を行います。
   * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生情報を紐づける値とコース開始日、コース終了日を設定します。
   *
   * @param studentDetail 受講生詳細
   * @return 登録情報を付与した受講生詳細
   */
  @Transactional
  public StudentDetail registerStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    student.setStudentId(UUID.randomUUID().toString());
    repository.registerStudent(student);

    studentDetail.getStudentCourseList().forEach(studentCourse -> {
      initStudentCourse(studentCourse, student);
      repository.registerStudentCourse(studentCourse);

      StudentCourseStatus studentCourseStatus = initStudentCourseStatus(studentCourse);
      studentCourse.setCourseStatus(studentCourseStatus);
      repository.registerStudentCourseStatus(studentCourseStatus);
    });

    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentCourse 受講生コース情報
   * @param student 受講生
   */
  private void initStudentCourse(StudentCourse studentCourse, Student student) {
    LocalDate now = LocalDate.now();

    studentCourse.setCourseId(UUID.randomUUID().toString());
    studentCourse.setStudentId(student.getStudentId());
    studentCourse.setStartDate(now);
    studentCourse.setEndDate(now.plusYears(1));
  }

  /**
   * コース申込状況を登録する際の初期情報を設定する。（初期値：仮申込）
   *
   * @param studentCourse 受講生コース情報
   * @return コース申込状況
   */
  private StudentCourseStatus initStudentCourseStatus(StudentCourse studentCourse) {
    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStatusId(UUID.randomUUID().toString());
    studentCourseStatus.setCourseId(studentCourse.getCourseId());
    studentCourseStatus.setStatus("仮申込");
    return studentCourseStatus;
  }

  /**
   * 受講生詳細の更新を行います。
   * 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail 受講生詳細
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
    studentDetail.getStudentCourseList()
        .forEach(studentCourse -> repository.updateStudentCourse(studentCourse));
  }

  /**
   * コース申込状況の更新を行います。
   *
   * @param studentCourseStatus コース申込状況
   */
  @Transactional
  public void updateStudentCourseStatus(StudentCourseStatus studentCourseStatus){
    repository.updateStudentCourseStatus(studentCourseStatus);
  }

  /**
   * 受講生詳細の削除（論理削除）を行います。
   *
   * @param studentId 受講生ID
   */
  @Transactional
  public void localDeleteStudent(String studentId) {
    repository.localDeleteStudent(studentId);
  }

}