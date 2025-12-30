package raisetech.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.domain.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before(){
    sut = new StudentService(repository, converter);
  }

  @Test
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること(){
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(repository.searchStudent()).thenReturn(studentList); //MEMO: Mock化されているrepositoryのsearchStudentを呼んだとき、このメソッドで実行されている範囲では、空のstudentListを返しますよ、としている。
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList); //MEMO: Mock化されているrepositoryのsearchStudentCourseListを呼んだとき、このメソッドで実行されている範囲では、空のstudentCourseListを返しますよ、としている。

    // List<StudentDetail> expected = new ArrayList<>(); //MEMO: テストの検証をする対象「expected」。検索結果で取れてきたものが適切に取れている。

    // 実行
    sut.searchStudentList(); //MEMO: この中で、repository.searchStudent()、repository.searchStudentCourseList()、converter.convertStudentDetails()が呼ばれる。
    // List<StudentDetail> actual = sut.searchStudentList(); //MEMO: テストの検証をする対象「actual」

    // 検証
    Mockito.verify(repository, times(1)).searchStudent(); //MEMO: 「repository」を１回呼んでます。ドット繋ぎでSearchStudentメソッドを呼んでいる。
    Mockito.verify(repository, times(1)).searchStudentCourseList(); //MEMO: 「repository」を１回呼んでます。ドット繋ぎでSearchStudentCourseメソッドを呼んでいる。
    Mockito.verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList); //MEMO: 「converter」を１回呼んでます。ドット繋ぎでSearchメソッドを呼んでいる。

    // Assertions.assertEquals(expected, actual); //MEMO: expectedとactualがイコールならテスト完了、ということ。

    // 後処理（DBに変更を加える場合は、DBをきれいにする必要がある。）
  }

  @Test
  void 受講生詳細検索_リポジトリの呼び出しが適切に動作していること(){
    // 事前準備
    Student student = new Student();
    String studentId = "test-id-123";
    student.setStudentId(studentId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(repository.searchStudentById(studentId)).thenReturn(student);
    Mockito.when(repository.searchStudentCourseListById(student.getStudentId())).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentById(studentId);

    // 検証
    Mockito.verify(repository, times(1)).searchStudentById(studentId);
    Mockito.verify(repository, times(1)).searchStudentCourseListById(student.getStudentId());

  }

  @Test
  void 受講生詳細検索_正しい受講生詳細が返されること(){
    // 事前準備
    Student expectedStudent = new Student();
    expectedStudent.setStudentId("test-id-123");
    expectedStudent.setName("テスト");

    StudentCourse course1 = new StudentCourse();
    course1.setCourseName("テストコース");
    List<StudentCourse> expectedStudentCourseList = List.of(course1);

    Mockito.when(repository.searchStudentById("test-id-123")).thenReturn(expectedStudent);
    Mockito.when(repository.searchStudentCourseListById("test-id-123")).thenReturn(expectedStudentCourseList);

    // 実行
    StudentDetail actual = sut.searchStudentById("test-id-123");

    // 検証
    assertEquals(expectedStudent.getStudentId(), actual.getStudent().getStudentId());
    assertEquals(expectedStudent.getName(), actual.getStudent().getName());
    assertEquals(expectedStudentCourseList, actual.getStudentCourseList());

  }

  @Test
  void 受講生詳細検索_存在しないIDの場合はNullPointerExceptionが発生すること(){
    // 事前準備
    String studentId = "存在しないID";
    Mockito.when(repository.searchStudentById(studentId)).thenReturn(null);

    // 実行 & 検証
    assertThrows(NullPointerException.class, () -> {
      sut.searchStudentById(studentId);
    });
    // MEMO: 「assertThrows(例外クラス, () -> { 実行するコード })」は、「この処理を実行したら、この例外が発生するはず」という検証。

  }

  @Test
  void 受講生詳細登録_リポジトリが適切に呼び出されていること(){

    // 事前準備
    Student student = new Student(); //MEMO: ②"StudentDetail"に含まれる"Student"を用意。
    StudentCourse course1 =new StudentCourse();
    StudentCourse course2 =new StudentCourse();
    StudentCourse course3 =new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(course1, course2, course3); //MEMO: ②"StudentDetail"に含まれる"StudentCourseList"を用意。
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList); //MEMO: ①メソッドの引数は"StudentDetail"。
    //MEMO: ちなみに、foreachやinitStudentCourseではrepositoryは呼び出されていない。なのでWhen-Thenは使わない。

    // 実行
    sut.registerStudent(studentDetail);

    // 検証
    Mockito.verify(repository, times(1)).registerStudent(student);
    Mockito.verify(repository, times(3)).registerStudentCourse(any(StudentCourse.class));
    //MEMO: any()は「「どんなStudentCourseでもいいから、3回呼ばれたことを確認」という意味。
  }

  @Test
  void 受講生詳細登録_登録情報が適切に付与されて返されること(){

    // 事前準備
    Student student = new Student();
    student.setName("テスト太郎");

    StudentCourse course1 =new StudentCourse();
    course1.setCourseName("テストコース");
    List<StudentCourse> studentCourseList = List.of(course1);
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
    //MEMO: studentIdやcourseId,startDate,endDateは自動生成されるから設定しない。

    // 実行
    StudentDetail actual= sut.registerStudent(studentDetail);

    // 検証
    //MEMO: 入力した値が保持されているか。
    assertEquals("テスト太郎", actual.getStudent().getName());
    assertEquals("テストコース", actual.getStudentCourseList().get(0).getCourseName());

    //MEMO: studentIdが自動生成されているか。
    assertNotNull(actual.getStudent().getStudentId());

    //MEMO: コースの初期値が設定されているか。
    StudentCourse actualStudentCourseList = actual.getStudentCourseList().get(0);
    assertNotNull(actualStudentCourseList.getCourseId());
    assertNotNull(actualStudentCourseList.getStartDate());
    assertNotNull(actualStudentCourseList.getEndDate());

    //MEMO: 登録したStudentのstudentIdとコース情報のstudentIdが一致しているかどうか。
    assertEquals(actual.getStudent().getStudentId(), actualStudentCourseList.getStudentId());
  }

  @Test
  void 受講生詳細更新_リポジトリが適切に呼び出されていること(){

    // 事前準備
    Student student = new Student(); //MEMO: ②"StudentDetail"に含まれる"Student"を用意。
    StudentCourse course1 =new StudentCourse();
    StudentCourse course2 =new StudentCourse();
    StudentCourse course3 =new StudentCourse();
    List<StudentCourse> studentCourseList = List.of(course1, course2, course3); //MEMO: ②"StudentDetail"に含まれる"StudentCourseList"を用意。
    StudentDetail studentDetail = new StudentDetail(student, studentCourseList); //MEMO: ①メソッドの引数は"StudentDetail"。
    //MEMO: ちなみに、foreachやinitStudentCourseではrepositoryは呼び出されていない。なのでWhen-Thenは使わない。

    // 実行
    sut.updateStudent(studentDetail);

    // 検証
    Mockito.verify(repository, times(1)).updateStudent(student);
    Mockito.verify(repository, times(3)).updateStudentCourse(any(StudentCourse.class));
    //MEMO: any()は「「どんなStudentCourseでもいいから、3回呼ばれたことを確認」という意味。
  }

  @Test
  void 受講生詳細の論理削除_リポジトリが適切に呼び出されていること(){

    // 事前準備
    String studentId = "test-id-123";

    // 実行
    sut.localDeleteStudent(studentId);

    // 検証
    Mockito.verify(repository, times(1)).localDeleteStudent(studentId);

  }

}