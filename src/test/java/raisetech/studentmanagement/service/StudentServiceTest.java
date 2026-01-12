package raisetech.studentmanagement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
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
import raisetech.studentmanagement.data.StudentCourseStatus;
import raisetech.studentmanagement.domain.assembler.StudentCourseAssembler;
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

  @Mock
  private StudentCourseAssembler studentCourseAssembler;

  private StudentService sut;

  @BeforeEach
  void before(){
    sut = new StudentService(repository, converter, studentCourseAssembler);
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
  void コース申込状況を含む受講生コース情報の一覧検索_リポジトリとアセンブラーの処理が適切に呼び出されていること(){
    // 事前準備
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    Mockito.when(repository.searchStudentCourseStatusList()).thenReturn(studentCourseStatusList);

    // 実行
    sut.studentCourseListWithStatus();

    // 検証
    Mockito.verify(repository, times(1)).searchStudentCourseList();
    Mockito.verify(repository, times(1)).searchStudentCourseStatusList();
    Mockito.verify(studentCourseAssembler, times(1)).assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);
  }

  @Test
  void コース申込状況を含む受講生詳細の一覧検索_リポジトリとアセンブラーとコンバーターらの依存コンポーネントが正しく呼び出せていること(){
    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
    List<StudentCourse> assembledList = new ArrayList<>();
    List<StudentDetail> expected = new ArrayList<>();

    Mockito.when(repository.searchStudent()).thenReturn(studentList);
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
    Mockito.when(repository.searchStudentCourseStatusList()).thenReturn(studentCourseStatusList);
    Mockito.when(studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList)).thenReturn(assembledList);
    Mockito.when(converter.convertStudentDetails(studentList, assembledList)).thenReturn(expected);

    // 実行
    List<StudentDetail> actual = sut.searchStudentListWithStatus();

    // 検証
    Mockito.verify(repository, times(1)).searchStudent();
    Mockito.verify(repository, times(1)).searchStudentCourseList();
    Mockito.verify(repository, times(1)).searchStudentCourseStatusList();
    Mockito.verify(studentCourseAssembler, times(1)).assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);
    Mockito.verify(converter, times(1)).convertStudentDetails(studentList, assembledList);
    assertThat(actual).isSameAs(expected);
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
    assertThat(actual.getStudent().getStudentId()).isEqualTo(expectedStudent.getStudentId());
    assertThat(actual.getStudent().getName()).isEqualTo(expectedStudent.getName());
    assertThat(actual.getStudentCourseList()).isEqualTo(expectedStudentCourseList);
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
  }

  @Test
  void コース申込状況を含む受講生詳細検索_リポジトリの呼び出しが適切に呼び出されていること(){
    // 事前準備
    Student student = new Student();
    String studentId = "test-id-123";
    student.setStudentId(studentId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
    List<StudentCourse> assembledList = new ArrayList<>();

    Mockito.when(repository.searchStudentById(studentId)).thenReturn(student);
    Mockito.when(repository.searchStudentCourseListById(student.getStudentId())).thenReturn(studentCourseList);
    Mockito.when(repository.searchStudentCourseStatusList()).thenReturn(studentCourseStatusList);
    Mockito.when(studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList)).thenReturn(assembledList);

    // 実行
    sut.searchStudentByIdWithStatus(studentId);

    // 検証
    Mockito.verify(repository, times(1)).searchStudentById(studentId);
    Mockito.verify(repository, times(1)).searchStudentCourseListById(student.getStudentId());
    Mockito.verify(repository, times(1)).searchStudentCourseStatusList();
    Mockito.verify(studentCourseAssembler, times(1)).assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);
  }

  @Test
  void コース申込状況を含む受講生詳細検索_コース申込状況を含む受講生詳細が正しく返されること(){
    // 事前準備
    Student student = new Student();
    student.setStudentId("test-id-123");
    student.setName("テスト太郎");

    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId("test-courseId-456");
    studentCourse.setCourseName("テストコース");
    List<StudentCourse> studentCourseList = List.of(studentCourse);

    StudentCourse assembledCourse = new StudentCourse();
    assembledCourse.setCourseId("test-courseId-456");
    assembledCourse.setCourseName("テストコース");

    StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
    studentCourseStatus.setStatusId("test-statusId-789");
    studentCourseStatus.setCourseId("test-courseId-456");
    studentCourseStatus.setStatus("仮申込");

    assembledCourse.setCourseStatus(studentCourseStatus);

    List<StudentCourseStatus> studentCourseStatusList = List.of(studentCourseStatus);
    List<StudentCourse> assembledList = List.of(assembledCourse);

    Mockito.when(repository.searchStudentById("test-id-123")).thenReturn(student);
    Mockito.when(repository.searchStudentCourseListById(student.getStudentId())).thenReturn(studentCourseList);
    Mockito.when(repository.searchStudentCourseStatusList()).thenReturn(studentCourseStatusList);
    Mockito.when(studentCourseAssembler.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList)).thenReturn(assembledList);

    // 実行
    StudentDetail actual= sut.searchStudentByIdWithStatus("test-id-123");

    // 検証
    assertThat(actual.getStudentCourseList().get(0).getCourseId()).isEqualTo("test-courseId-456");
    assertThat(actual.getStudentCourseList().get(0).getCourseStatus().getStatus()).isEqualTo("仮申込");
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
    assertThat(actual.getStudent().getName()).isEqualTo("テスト太郎");
    assertThat(actual.getStudentCourseList().get(0).getCourseName()).isEqualTo("テストコース");

    //MEMO: studentIdが自動生成されているか。
    assertThat(actual.getStudent().getStudentId()).isNotNull();

    //MEMO: コースの初期値が設定されているか。
    StudentCourse actualStudentCourseList = actual.getStudentCourseList().get(0);
    assertThat(actualStudentCourseList.getCourseId()).isNotNull();
    assertThat(actualStudentCourseList.getStartDate()).isNotNull();
    assertThat(actualStudentCourseList.getEndDate()).isNotNull();

    //MEMO: 登録したStudentのstudentIdとコース情報のstudentIdが一致しているかどうか。
    assertThat(actualStudentCourseList.getStudentId()).isEqualTo(actual.getStudent().getStudentId());
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