package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

//MEMO: 画面に出す項目。StudentとStudentListの情報をまとめたクラス。
@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Valid
  private Student student; //MEMO: こっちは単一。

  @Valid
  private List<StudentCourse> studentCourseList; //MEMO: StudentCourseはStudentに対して複数入る可能性があるためListとなる。

}
