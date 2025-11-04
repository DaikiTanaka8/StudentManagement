package raisetech.studentmanagement.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.searchStudents();
  }

  public List<StudentCourse> searchStudentCourseList() {
    return repository.searchStudentCourses();
  }

  // ここでService層でDistinct（重複しない）コースリストを取得する。
  public List<StudentCourse> getDistinctCourses(){
    return repository.getDistinctCourses();
  }

  @Transactional
  public void registerStudent(StudentDetail studentDetail) {
    // IDを設定
    studentDetail.getStudent().setStudentId(UUID.randomUUID().toString());
    // ここで実際の登録処理
    repository.insertStudent(studentDetail.getStudent());

    // ここでコース情報の登録処理も行う予定
    //repository.insertStudentCourse(studentDetail.getStudentCourse());

  }
}
