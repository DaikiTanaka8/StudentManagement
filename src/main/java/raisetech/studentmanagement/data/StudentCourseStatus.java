package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コースの申込状況")
@Setter
@Getter
public class StudentCourseStatus {

  private String statusId;
  private String courseId;
  private String status;
}
