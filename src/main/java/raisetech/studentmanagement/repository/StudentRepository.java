package raisetech.studentmanagement.repository;
// データベースそのものって思っていればいい。

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 受講生情報を扱うリポジトリ
 * 全件検索や単一条件での検索、コース情報の検索が行えクラスです。
 */
@Mapper // MyBatisが管理しないといけない、という指示。SpringBootにMyBatisのMapperであることを伝えている。
public interface StudentRepository {

  /**
   * 受講生情報の全件検索。
   * @return 全件検索した受講生情報の一覧
   */
  @Select("SELECT * FROM students") // MyBatisのアノテーションで、このメソッドが実行するSQL文を直接書いている。
  List<Student> searchStudents(); // Listで返します。MyBatisがちゃんとListを認識する。

  /**
   * コース情報の全件検索。
   * @return 全件検索した受講生コース情報の一覧
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

  @Insert(
      "INSERT INTO students(student_id,name, furigana, nickname, email, city, age, gender, remark)"
          + "VALUES(#{studentId}, #{name}, #{furigana}, #{nickname}, #{email}, #{city}, #{age}, #{gender}, #{remark})")
  // @Options(useGeneratedKeys = true, keyProperty = "id") ←自動生成した項目を使いますよ、という意味。ただ私のコードでは使わない。
  void registerStudent(Student student); // INSERTだから登録した後何も返さないのでvoid。
  // IDの管理は自分でしたくない。自動採番にして生成したりUUIDにしたり。MAXに＋１するのはOK。（ちなみに講義内ではAUTO_INCREMENTで自動連番にしている。）
  // keyPropertyで設定したやつを自動採番したやつを受け取れる？？

  @Insert(
      "INSERT INTO students_courses(course_id, student_id, course_name, start_date, end_date) " +
          "VALUES(#{courseId}, #{studentId}, #{courseName}, #{startDate}, #{endDate})")
      // ここの中身はデータベースのフィールド名と一致するように。
  void registerStudentCourse(StudentCourse studentCourse); //

}