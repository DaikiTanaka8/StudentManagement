package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {

  private String courseId;
  private String studentId;

  @NotBlank(message = "コース名は必須です")
  private String courseName;

  private LocalDate startDate;
  private LocalDate endDate;
  private StudentCourseStatus status;

}