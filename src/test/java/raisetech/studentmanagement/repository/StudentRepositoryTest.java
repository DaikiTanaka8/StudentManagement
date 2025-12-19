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