package raisetech.studentmanagement.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String studentId;
  @NotBlank(message = "名前は必須です")
  private String name;

  @NotBlank(message = "ふりがなは必須です")
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
  private String gender;
  private String remark;
  private boolean isDeleted;
}
