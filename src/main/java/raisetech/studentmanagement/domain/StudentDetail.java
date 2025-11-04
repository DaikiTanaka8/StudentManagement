package raisetech.studentmanagement.domain;

import jakarta.validation.Valid;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

@Getter
@Setter
public class StudentDetail {

  private @Valid Student student;
  private List<StudentCourse> studentCourse;

}
