package raisetech.studentmanagement.controller.converter;
//MEMO: StudentControllerの中でやっていたコンバーターなので、パッケージングがコントローラーの中にコンバーターを作っている。

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;

/**
 * 受講生詳細を受講生や受講生コース情報、もしくはその逆の変換を行うコンバーターです。
 * //MEMO: 値の詰め替えを行うクラス。責務ごとで欲しい情報が異なる。画面に出したい情報とプログラム内で触りたい情報が異なることがある。
 * //MEMO: Listで返すと全情報が出てきてしまい、セキュリティ面で危うかったりデータ量が多くなったりする。
 */
@Component //MEMO: このクラスは自分で勝手に作ったクラス。だから、SpringBootに管理させるために「@Component」をつけている。
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。
   * 受講生コース情報は受講生に対して複数存在するので、ループを回して受講生詳細情報を組み立てる。
   * 受講生とその受講生が受講している受講生コース情報の一覧
   * @param studentList 受講生一覧。
   * @param studentCourseList 受講生コース情報のリスト。
   * @return 受講生詳細情報のリスト。//MEMO: 返ってくるのは「受講生とその受講生が受講している受講生コース情報の一覧」
   */
  public List<StudentDetail> convertStudentDetails(List<Student> studentList, List<StudentCourse> studentCourseList) {

    List<StudentDetail> studentDetails = new ArrayList<>(); //MEMO: まずは空のリストを用意しておく。

    studentList.forEach(student -> { //MEMO: 受講生情報をループさせる。受講生の数分ぐるぐるさせる。　例：山田太郎が入る。
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student); //MEMO: 例：まず山田太郎が入る。

      List<StudentCourse> convertStudentCourseList = studentCourseList.stream() //MEMO: 受講生情報で引っ張ってきた「受講生」に対する、コース情報をループさせる。　例：山田太郎に関するコース情報をループさせる。
          .filter(studentCourse -> Objects.equals(student.getStudentId(), studentCourse.getStudentId())) //MEMO: 「受講生」のIDとコース情報上のIDが一致したら…　例：山田太郎のIDと一致したら
          .collect(Collectors.toList()); //MEMO: 「convertStudentCourseList」の中に追加する。　例：山田太郎のIDと一致したコース情報を追加する。

      studentDetail.setStudentCourseList(convertStudentCourseList); //MEMO: 一人目（例：山田太郎）のコンバートしたコースリストを、studentDetailリストの中にセットする。
      studentDetails.add(studentDetail); //MEMO: studentDetails（ディテール"ズ"）の方に加える。
    });

    return studentDetails; //MEMO: studentDetails（ディテール"ズ"）を返す。
  }

}
