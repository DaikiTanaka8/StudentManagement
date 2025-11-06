package raisetech.studentmanagement.repository;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM students")
  List<Student> searchStudents();

  @Select("SELECT * FROM students_courses")
  List<StudentCourse> searchStudentCourses();

  // ここでDISTINCT（重複除去して取得）を使って重複せずにコース一覧を取得する
  @Select("SELECT DISTINCT course_id, course_name FROM students_courses")
  List<StudentCourse> getDistinctCourses();

  @Insert(
      "INSERT INTO students(student_id,name, furigana, nickname, email, city, age, gender, remark)"
          + "VALUES(#{studentId}, #{name}, #{furigana}, #{nickname}, #{email}, #{city}, #{age}, #{gender}, #{remark})")
  void insertStudent(Student student);

  // ここで中間テーブルへの挿入メソッド
  @Insert("INSERT INTO students_courses(course_id, student_id, course_name, start_date, end_date)"
      + "VALUES(#{courseId}, #{studentId}, #{courseName}, #{startDate}, #{endDate})")
  void insertStudentCourse(StudentCourse studentCourse);

}
