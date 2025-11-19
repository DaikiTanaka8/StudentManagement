package raisetech.studentmanagement.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

//MEMO: 画面に出す項目。StudentとStudentListの情報をまとめたクラス。
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student; //MEMO: こっちは単一。
  private List<StudentCourse> studentCourses; //MEMO: StudentCourseはStudentに対して複数入る可能性があるためListとなる。

}
