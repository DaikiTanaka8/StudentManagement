package raisetech.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.studentmanagement.controller.converter.StudentConverter;
import raisetech.studentmanagement.data.Student;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.domain.StudentDetail;
import raisetech.studentmanagement.repository.StudentRepository;

@ExtendWith(MockitoExtension.class) //MEMO: これをつけてMockitoが使えるようになる。
class StudentServiceTest {

  @Mock
  private StudentRepository repository; //MEMO:これでリポジトリがMock化される。

  @Mock
  private StudentConverter converter; //MEMO:これでコンバーターがMock化される。

  private StudentService sut; //MEMO: テスト対象は「sut」と書くことが多い。テスト対象が変わらないので、クラス全体でsutを定義しちゃう。

  @BeforeEach //MEMO: テストする度に動く。毎回生成してくれる。
  void before(){
    sut = new StudentService(repository, converter); //MEMO: Mock化されたリポジトリとコンバーターを与える。
  }

  @Test
  //MEMO: publicは省略してしまっていい（動くから）。テストしたいだけだからvoid。
  //MEMO: テストメソッドは日本語で書いていい。
  //MEMO:元々は「全件検索が動作すること」であったが、searchStudentListの中身を見ると、
  // 「searchStudentでリポジトリを呼び出している」「searchStudentCourseでリポジトリを呼び出している」「convertStudentDetailsでコンバートしている」の３つ。
  // →すなわち、リポジトリが呼び出せていることとコンバーターの処理が適切に呼び出していることをテストできれば良い。
  void 受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること(){

    // 事前準備
    List<Student> studentList = new ArrayList<>();
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(repository.searchStudent()).thenReturn(studentList); //MEMO: Mock化されているrepositoryのsearchStudentを呼んだとき、このメソッドで実行されている範囲では、空のstudentListを返しますよ、としている。
    Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList); //MEMO: Mock化されているrepositoryのsearchStudentCourseListを呼んだとき、このメソッドで実行されている範囲では、空のstudentCourseListを返しますよ、としている。

    // List<StudentDetail> expected = new ArrayList<>(); //MEMO: テストの検証をする対象「expected」。検索結果で取れてきたものが適切に取れている。

    // 実行
    sut.searchStudentList(); //MEMO: この中で、repository.searchStudent()、repository.searchStudentCourseList()、converter.convertStudentDetails()が呼ばれる。
    // List<StudentDetail> actual = sut.searchStudentList(); //MEMO: テストの検証をする対象「actual」

    // 検証
    Mockito.verify(repository, times(1)).searchStudent(); //MEMO: 「repository」を１回呼んでます。ドット繋ぎでSearchStudentメソッドを呼んでいる。
    Mockito.verify(repository, times(1)).searchStudentCourseList(); //MEMO: 「repository」を１回呼んでます。ドット繋ぎでSearchStudentCourseメソッドを呼んでいる。
    Mockito.verify(converter, times(1)).convertStudentDetails(studentList, studentCourseList); //MEMO: 「converter」を１回呼んでます。ドット繋ぎでSearchメソッドを呼んでいる。

    // Assertions.assertEquals(expected, actual); //MEMO: expectedとactualがイコールならテスト完了、ということ。

    // 後処理（DBに変更を加える場合は、DBをきれいにする必要がある。）
  }

  @Test
  void 受講生詳細が適切に動作していること(){
    // 事前準備
    Student student = new Student();
    String studentId = "test-id-123";
    student.setStudentId(studentId);
    List<StudentCourse> studentCourseList = new ArrayList<>();
    Mockito.when(repository.searchStudentById(studentId)).thenReturn(student);
    Mockito.when(repository.searchStudentCourseListById(student.getStudentId())).thenReturn(studentCourseList);

    // 実行
    sut.searchStudentById(studentId);

    // 検証
    Mockito.verify(repository, times(1)).searchStudentById(studentId); //MEMO: 「repository」を１回呼んでます。
    Mockito.verify(repository, times(1)).searchStudentCourseListById(student.getStudentId()); //MEMO: 「repository」を１回呼んでます。
  }



}