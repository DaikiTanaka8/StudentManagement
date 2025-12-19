package raisetech.studentmanagement.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.studentmanagement.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の全件検索が行えること(){
    List<Student> actual = sut.searchStudent();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生のID検索が行えること(){
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

    assertEquals(expected.getName(), actual.getName());
    assertEquals(expected.getFurigana(), actual.getFurigana());
    assertEquals(expected.getNickname(), actual.getNickname());
    assertEquals(expected.getEmail(), actual.getEmail());
    assertEquals(expected.getCity(), actual.getCity());
    assertEquals(expected.getAge(), actual.getAge());
    assertEquals(expected.getGender(), actual.getGender());

  }

  @Test
  void 受講生の登録が行えること(){
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

}