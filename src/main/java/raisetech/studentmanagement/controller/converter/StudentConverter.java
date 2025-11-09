package raisetech.studentmanagement.controller.converter;
// StudentControllerの中でやっていたコンバーターなので、パッケージングがコントローラーの中にコンバーターを作っている。

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 値の詰め替えを行うクラス。
 * 責務ごとで欲しい情報が異なる。画面に出したい情報とプログラム内で触りたい情報が異なることがある。
 * Listで返すと全情報が出てきてしまい、セキュリティ面で危うかったりデータ量が多くなったりする。
 */
@Component // このクラスは自分で勝手に作ったクラス。SpringBootに管理させるために「@Component」をつけている。
public class StudentConverter {

  /**
   * 受講生とその受講生が受講している受講生コース情報の一覧
   * @param students 全件の受講生情報
   * @param studentCourses 全件の受講生コース情報
   * @return 返ってくるのは「受講生とその受講生が受講している受講生コース情報の一覧」
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students, List<StudentCourse> studentCourses) {

    List<StudentDetail> studentDetails = new ArrayList<>(); // まずは空のリストを用意しておく。

    students.forEach(student -> { // 受講生情報をループさせる。受講生の数分ぐるぐるさせる。　例：山田太郎が入る。
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student); // 例：まず山田太郎が入る。

      List<StudentCourse> convertStudentCourse = studentCourses.stream() // 受講生情報で引っ張ってきた「受講生」に対する、コース情報をループさせる。　例：山田太郎に関するコース情報をループさせる。
          .filter(studentCourse -> Objects.equals(student.getStudentId(), studentCourse.getStudentId())) // 「受講生」のIDとコース情報上のIDが一致したら…　例：山田太郎のIDと一致したら
          .collect(Collectors.toList()); // 「convertStudentCourse」の中に追加する。　例：山田太郎のIDと一致したコース情報を追加する。

      studentDetail.setStudentCourse(convertStudentCourse); // 一人目（例：山田太郎）のコンバートしたコースリストを、studentDetailリストの中にセットする。
      studentDetails.add(studentDetail); // studentDetails（ディテール"ズ"）の方に加える。
    });

    return studentDetails; // studentDetails（ディテール"ズ"）を返す。
  }

}
