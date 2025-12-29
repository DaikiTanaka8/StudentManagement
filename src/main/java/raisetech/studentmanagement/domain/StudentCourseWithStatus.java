package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.data.StudentCourseStatus;


@Schema(description = "申込状況を含むコース情報")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseWithStatus {

  @Valid
  private StudentCourse studentCourse;

  @Valid
  private StudentCourseStatus studentCourseStatus;

}