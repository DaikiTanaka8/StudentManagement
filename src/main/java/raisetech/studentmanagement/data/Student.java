package raisetech.studentmanagement.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  private String studentId;

  @NotBlank(message = "名前は必須です")
  private String name;

  @NotBlank(message = "ふりがなは必須です")
  @Pattern(regexp = "^[\\u3040-\\u309F]+$", message = "ふりがなはひらがなで入力してください")
  private String furigana;

  private String nickname;

  @Email(message = "正しいメールアドレスを入力してください")
  @NotBlank(message = "メールアドレスは必須です")
  private String email;

  @NotBlank(message = "地域は必須です")
  private String city;

  @Min(value = 10, message = "年齢は10歳以上で入力してください")
  @Max(value = 100, message = "年齢は100歳以下で入力してください")
  private int age;

  @NotBlank(message = "性別を入力してください")
  @Pattern(regexp = "^(男性|女性|その他)$", message = "性別は 男性・女性・その他 のいずれかで入力してください")
  private String gender;

  private String remark;
  private boolean isDeleted;
}
