package raisetech.studentmanagement.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private String studentId;
  private String name;
  private String furigana;
  private String nickname;
  private String email;
  private String city;
  private int age;
  private String gender;
}
