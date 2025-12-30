package raisetech.studentmanagement.controller.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.domain.converter.StudentConverter;

class StudentConverterTest {

  private StudentConverter sut;

  @BeforeEach
  void before(){
    sut = new StudentConverter();
  }

  @Test
  void 受講生詳細の受講生や受講生コース情報の変換を行うコンバーターが正しく動作していること() {

    // 事前準備
    Student student1 = new Student();
    student1.setStudentId("1");
    student1.setName("テスト太郎1");

    Student student2 = new Student();
    student2.setStudentId("2");
    student2.setName("テスト太郎2");

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseName("テストコース1");
    studentCourse1.setStudentId("1");
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseName("テストコース2");
    studentCourse2.setStudentId("1");
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setCourseName("テストコース3");
    studentCourse3.setStudentId("2");

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);

    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
     assertEquals(2, actual.size());

     StudentDetail actualStudentDetail1 = actual.get(0);
     assertEquals("1", actualStudentDetail1.getStudent().getStudentId());
     assertEquals("テスト太郎1", actualStudentDetail1.getStudent().getName());
     assertEquals(2, actualStudentDetail1.getStudentCourseList().size());
     assertEquals("テストコース1", actualStudentDetail1.getStudentCourseList().get(0).getCourseName());
     assertEquals("テストコース2", actualStudentDetail1.getStudentCourseList().get(1).getCourseName());

     StudentDetail actualStudentDetail2 = actual.get(1);
     assertEquals("2", actualStudentDetail2.getStudent().getStudentId());
     assertEquals("テスト太郎2", actualStudentDetail2.getStudent().getName());
     assertEquals(1, actualStudentDetail2.getStudentCourseList().size());
     assertEquals("テストコース3", actualStudentDetail2.getStudentCourseList().get(0).getCourseName());

  }

  @Test //MEMO: 解答を模して作成。フィールドではなくインスタンスを比較している。
  void 受講生のリストと受講生コース情報のリストを渡して受講生詳細のリストが作成できること() {

    // 事前準備
    Student student1 = createStudent1();
    Student student2 = createStudent2();

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseName("テストコース1");
    studentCourse1.setStudentId("1");
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseName("テストコース2");
    studentCourse2.setStudentId("1");
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setCourseName("テストコース3");
    studentCourse3.setStudentId("2");

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);

    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    assertThat(actual.get(0).getStudent()).isEqualTo(student1);
    assertThat(actual.get(0).getStudentCourseList()).isEqualTo(List.of(studentCourse1,studentCourse2));

  }

  @Test //MEMO: 解答より作成。
  void 受講生のリストと受講生コース情報のリストを渡した時に紐づかない受講生コース情報は除外されること() {

    // 事前準備
    Student student1 = createStudent1();
    Student student2 = createStudent2();

    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setCourseName("テストコース3");
    studentCourse3.setStudentId("2");

    List<Student> studentList = List.of(student1, student2);
    List<StudentCourse> studentCourseList = List.of(studentCourse3);

    // 実行
    List<StudentDetail> actual = sut.convertStudentDetails(studentList, studentCourseList);

    // 検証
    assertThat(actual.get(0).getStudent()).isEqualTo(student1);
    assertThat(actual.get(0).getStudentCourseList()).isEmpty();

  }


  private static Student createStudent1() {
    Student student1 = new Student();
    student1.setStudentId("1");
    student1.setName("テスト太郎1");
    student1.setFurigana("てすとたろう1");
    student1.setNickname("テスト1");
    student1.setEmail("test1@example.com");
    student1.setCity("東京都");
    student1.setAge(11);
    student1.setGender("男性");
    student1.setRemark("");
    student1.setDeleted(false);
    return student1;
  }

  private static Student createStudent2() {
    Student student2 = new Student();
    student2.setStudentId("2");
    student2.setName("テスト太郎2");
    student2.setFurigana("てすとたろう2");
    student2.setNickname("テスト2");
    student2.setEmail("test2@example.com");
    student2.setCity("東京都");
    student2.setAge(12);
    student2.setGender("女性");
    student2.setRemark("");
    student2.setDeleted(false);
    return student2;
  }

}