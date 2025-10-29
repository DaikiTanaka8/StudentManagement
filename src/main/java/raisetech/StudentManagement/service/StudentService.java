package raisetech.StudentManagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.StudentManagement.data.Student;
import raisetech.StudentManagement.data.StudentCourse;
import raisetech.StudentManagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    // 絞り込みをする。年齢が30代の人のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    List<Student> students = repository.searchStudents();
    List<Student> thirtyList = new ArrayList<>();

    for (Student s : students) {
      if (s.getAge() >= 30 && s.getAge() < 40) {
        thirtyList.add(s);
      }
    }
    return thirtyList;
  }

  public List<StudentCourse> searchStudentCourseList() {
    // 絞り込み検索で、「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    List<StudentCourse> courses = repository.searchStudentCourses();

    List<StudentCourse> javaCourse = courses.stream()
        .filter(course -> "Java基礎".equals(course.getCourseName()))
        .collect(Collectors.toList());

    return javaCourse;
  }

}
