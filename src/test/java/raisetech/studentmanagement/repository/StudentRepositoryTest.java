package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

@MybatisTest //MEMO: ←これだけで自動でロールバックされている。
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること() {
    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生のID検索が行えること() {
    Student expected = new Student();
    expected.setStudentId("1");
    expected.setName("鈴木大介");
    expected.setFurigana("すずきだいすけ");
    expected.setNickname("だいちゃん");
    expected.setEmail("suzuki@example.com");
    expected.setCity("東京都");
    expected.setAge(51);
    expected.setGender("男性");

    Student actual = sut.searchStudentById("1");

    assertThat(actual)
        .extracting( //MEMO: オブジェクト → 比較用の値リストに変換。
            Student::getName, //MEMO: 「student -> student.getName()」と同じ意味。
            Student::getFurigana,
            Student::getNickname,
            Student::getEmail,
            Student::getCity,
            Student::getAge,
            Student::getGender
        )
        .containsExactly( //MEMO: // 「順番・中身・数すべて一致」かどうか検証している。
            expected.getName(),
            expected.getFurigana(),
            expected.getNickname(),
            expected.getEmail(),
            expected.getCity(),
            expected.getAge(),
            expected.getGender()
        );

  }

  @Test
  void 受講生コース情報の全件検索が行えること() {
    List<StudentCourse> actual = sut.searchStudentCourseList();
    assertThat(actual.size()).isEqualTo(10);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseId("101");
    studentCourse1.setCourseName("Java基礎");

    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseId("102");
    studentCourse2.setCourseName("Spring Boot入門");

    List<StudentCourse> expected = List.of(studentCourse1, studentCourse2);

    List<StudentCourse> actual = sut.searchStudentCourseListById("1");

    assertThat(actual)
        .extracting(StudentCourse::getCourseName)
        .containsExactly(
            "Java基礎",
            "Spring Boot入門"
        );
  }

  @Test
  void 受講生の登録が行えること() {
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

    sut.registerStudent(student);

    List<Student> actual = sut.searchStudent();

    //MEMO: 登録後studentsの数が1つ増えているということを確認する。
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生コース情報の登録が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId("test-id-123");
    studentCourse.setStudentId("test-id-789");
    studentCourse.setCourseName("テストコース");
    studentCourse.setStartDate(LocalDate.parse("2025-01-01"));
    studentCourse.setEndDate(LocalDate.parse("2026-01-01"));

    sut.registerStudentCourse(studentCourse);

    List<StudentCourse> actual = sut.searchStudentCourseList();

    //MEMO: 登録後students_coursesの数が1つ増えているということを確認する。
    assertThat(actual.size()).isEqualTo(11);
  }

  @Test
  void 受講生の更新が行えること() {
    Student student = new Student();
    student.setStudentId("1");
    student.setName("鈴木大介");
    student.setFurigana("すずきだいすけ");
    student.setNickname("ブルドーザー");
    student.setEmail("suzuki@example.com");
    student.setCity("東京都");
    student.setAge(51);
    student.setGender("男性");

    sut.updateStudent(student);

    Student searchStudentById = sut.searchStudentById("1");

    assertThat(searchStudentById)
        .extracting(
            Student::getName,
            Student::getFurigana,
            Student::getNickname,
            Student::getEmail,
            Student::getCity,
            Student::getAge,
            Student::getGender
        )
        .containsExactly(
            student.getName(),
            student.getFurigana(),
            student.getNickname(),
            student.getEmail(),
            student.getCity(),
            student.getAge(),
            student.getGender()
        );
  }

  @Test
  void 受講生コース情報の更新が行えること() {
    StudentCourse studentCourse = new StudentCourse();
    studentCourse.setCourseId("101");
    studentCourse.setStudentId("1");
    studentCourse.setCourseName("テストコース");

    sut.updateStudentCourse(studentCourse);

    StudentCourse updateCourse = sut.searchStudentCourseListById("1")
        .stream().filter(course -> course.getCourseId().equals("101"))
        .findFirst()
        .orElseThrow();

    assertThat(updateCourse.getCourseName()).isEqualTo(studentCourse.getCourseName());

  }

  @Test
  void 受講生の論理削除が行えること() {

    sut.localDeleteStudent("1");

    List<Student> actual = sut.searchStudent();

    //MEMO: 登録後studentsの数が1つ減っているということを確認する。
    assertThat(actual.size()).isEqualTo(3);
  }

}