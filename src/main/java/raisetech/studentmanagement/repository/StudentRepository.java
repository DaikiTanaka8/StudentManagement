package raisetech.studentmanagement.repository;
//MEMO: データベースそのものって思っていればいい。

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 * 受講生情報を扱うリポジトリ
 * 全件検索や単一条件での検索、コース情報の検索が行えクラスです。
 */
@Mapper //MEMO: 「MyBatisが管理しないといけない」という指示。SpringBootにMyBatisのMapperであることを伝えている。
public interface StudentRepository {

  /**
   * 受講生の全件検索を行います。
   * @return 受講生一覧（全件）
   */
  List<Student> searchStudent(); //MEMO: Listで返します。MyBatisがちゃんとListを認識する。

  /**
   * 受講生の検索を行います。 //MEMO: 受講生情報のID検索。IDで単一の受講生を取得してくる。（自分で作れていた！）
   * @return 受講生。ID検索した受講生に関する情報一覧。
   */
  Student searchStudentById(String studentId);

  /**
   * 受講生のコース情報の全件検索を行います。
   * @return 受講生コース情報（全件）。
   */
  List<StudentCourse> searchStudentCourseList();

  /**
   * 受講生IDに紐づくコース情報を検索します。
   * @param studentId 受講生ID
   * @return 受講生IDに紐づく受講生コース情報。
   */
  List<StudentCourse> searchStudentCourseListById(String studentId);

  /**
   * 受講生を新規登録します。IDに関しては自動採番を行う（UUID）。
   * @param student 受講生
   */
  void registerStudent(Student student); //MEMO: INSERTだから登録した後何も返さないのでvoid。
  //MEMO: IDの管理は自分でしたくない。自動採番にして生成したりUUIDにしたり。MAXに＋１するのはOK。（ちなみに講義内ではAUTO_INCREMENTで自動連番にしている。）

  /**
   * 受講生コース情報を新規登録します。IDに関しては自動採番を行う（UUID）。
   * @param studentCourse 受講生コース情報
   */
  void registerStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を更新します。
   * @param student 受講生
   */
  void updateStudent(Student student);

  /**
   * 受講生コース情報を更新します。
   * @param studentCourse 受講生コース情報
   */
  void updateStudentCourse(StudentCourse studentCourse);

  /**
   * 受講生を削除（論理削除）します。
   * @param studentId 受講生ID
   */
  void localDeleteStudent(String studentId);

}