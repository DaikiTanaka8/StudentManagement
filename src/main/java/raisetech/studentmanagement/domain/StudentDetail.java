package raisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 画面に出す項目。StudentとStudentListの情報をまとめたクラス。
 */
@Getter
@Setter
public class StudentDetail {

  private @Valid Student student; // こっちは単一。
  private List<StudentCourse> studentCourse; // StudentCourseはStudentに対して複数入る可能性があるため、Listとなる。

}
