package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "コースの申込状況")
@Setter
@Getter
public class StudentCourseStatus {

  private String statusId;
  private String courseId;

  @NotBlank(message = "申込状況を入力してください")
  @Pattern(regexp = "^(仮申込|本申込|受講中|受講終了)$", message = "申込状況は 仮申込・本申込・受講中・受講終了 のいずれかで入力してください")
  private String status;

}
