package raisetech.studentmanagement.domain.converter;

import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.data.StudentCourseStatus;
import raisetech.studentmanagement.domain.StudentCourseWithStatus;

/**
 * 受講生コース情報とコース申込状況を組み合わせるコンバーターです。
 */
@Component
public class StudentCourseConverter {

  public StudentCourseWithStatus convertStudentCourseWithStatus(StudentCourse studentCourse, StudentCourseStatus studentCourseStatus) {

    StudentCourseWithStatus studentCourseWithStatus = new StudentCourseWithStatus(); //MEMO:一旦空を用意。


    studentCourseWithStatus.setStudentCourse();
    studentCourseWithStatus.setStudentCourseStatus();

    return studentCourseWithStatus;
  }





}
