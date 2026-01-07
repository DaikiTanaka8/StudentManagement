package raisetech.studentmanagement.domain.assembler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.data.StudentCourseStatus;

@Component
public class StudentCourseAssembler {

  /**
   * 全件の受講生コース情報にコース申込状況を紐づけるメソッドです。
   *
   * @param studentCourseList 受講生コース情報リスト
   * @param studentCourseStatusList コース申込状況リスト
   * @return コース申込状況が紐づいた受講生コースリスト
   */
  public List<StudentCourse> assembleCourseListWithStatus(List<StudentCourse> studentCourseList, List<StudentCourseStatus> studentCourseStatusList){

    Map<String, StudentCourseStatus> studentCourseStatusMap = new HashMap<>();

    for(StudentCourseStatus studentCourseStatus : studentCourseStatusList){
      studentCourseStatusMap.put(studentCourseStatus.getCourseId() , studentCourseStatus);
    }

    for (StudentCourse studentCourse : studentCourseList){
      studentCourse.setCourseStatus(studentCourseStatusMap.get(studentCourse.getCourseId()));
    }

    return studentCourseList;
  }

}
