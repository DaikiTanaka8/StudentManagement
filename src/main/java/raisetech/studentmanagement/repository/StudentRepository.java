package raisetech.studentmanagement.repository;
//MEMO: データベースそのものって思っていればいい。

import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 * 受講生情報を扱うリポジトリ
 * 全件検索や単一条件での検索、コース情報の検索が行えクラスです。
 */
@Mapper //MEMO: 「MyBatisが管理しないといけない」という指示。SpringBootにMyBatisのMapperであることを伝えている。
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   * @return 受講生一覧（全件）
   */
  @Select("SELECT * FROM students WHERE is_deleted = false") //MEMO: 「WHERE is_deleted = false」で削除されていないものが表示される。
  List<Student> searchStudent(); //MEMO: Listで返します。MyBatisがちゃんとListを認識する。

  /**
   * 受講生の検索を行います。 //MEMO: 受講生情報のID検索。IDで単一の受講生を取得してくる。（自分で作れていた！）
   * @return 受講生。ID検索した受講生に関する情報一覧。
   */
  @Select("SELECT * FROM students WHERE student_id = #{studentId} AND is_deleted = false")
  Student searchStudentById(String studentId);

  /**
   * 受講生のコース情報の全件検索を行います。
   * @return 受講生コース情報（全件）。
   */
  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づくコース情報を検索します。
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報。
   */
  @Select("SELECT * FROM students_courses WHERE student_id = #{studentId}")
  List<StudentCourse> searchStudentCourseListById(String studentId);

  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う（UUID）。
   * @param student 受講生
   */
  @Insert(
      "INSERT INTO students(student_id,name, furigana, nickname, email, city, age, gender, remark)"
          + "VALUES(#{studentId}, #{name}, #{furigana}, #{nickname}, #{email}, #{city}, #{age}, #{gender}, #{remark})")
  void registerStudent(Student student); //MEMO: INSERTだから登録した後何も返さないのでvoid。
  //MEMO: IDの管理は自分でしたくない。自動採番にして生成したりUUIDにしたり。MAXに＋１するのはOK。（ちなみに講義内ではAUTO_INCREMENTで自動連番にしている。）

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う（UUID）。
   * @param studentCourse 受講生コース情報
   */
  @Insert(
      "INSERT INTO students_courses(course_id, student_id, course_name, start_date, end_date) " +
          "VALUES(#{courseId}, #{studentId}, #{courseName}, #{startDate}, #{endDate})")
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   * @param student 受講生
   */
  @Update("UPDATE students SET name = #{name}, furigana = #{furigana}, nickname = #{nickname}, email = #{email},"
      + "city = #{city}, age = #{age}, gender = #{gender}, remark = #{remark}, is_deleted = #{isDeleted} WHERE student_id = #{studentId}")
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新します。
   * @param studentCourse 受講生コース情報
   */
  @Update(
      "UPDATE students_courses SET course_name = #{courseName} WHERE course_id = #{courseId}")
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を削除（論理削除）します。
   * @param student 受講生
   */
  @Update("UPDATE students SET is_deleted = true WHERE student_id = #{studentId}")
  void deleteStudent(Student student);

}