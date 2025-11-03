package raisetech.studentmanagement.repository;

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

@Insert("INSERT INTO students(student_id,name, furigana, nickname, email, city, age, gender, remark)"
    + "VALUES(#{studentId}, #{name}, #{furigana}, #{nickname}, #{email}, #{city}, #{age}, #{gender}, #{remark})")
  void insertStudent(Student student);

}
