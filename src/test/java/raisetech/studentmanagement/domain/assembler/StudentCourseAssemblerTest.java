package raisetech.studentmanagement.domain.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import raisetech.studentmanagement.data.StudentCourse;
import raisetech.studentmanagement.data.StudentCourseStatus;

class StudentCourseAssemblerTest {

  private StudentCourseAssembler sut;

  @BeforeEach
  void before(){
    sut = new StudentCourseAssembler();
  }

  @Test
  void 受講生コース情報とコース申込状況が正しく紐づくこと() {

    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseName("テストコース1");
    studentCourse1.setCourseId("1");
    StudentCourse studentCourse2 = new StudentCourse();
    studentCourse2.setCourseName("テストコース2");
    studentCourse2.setCourseId("2");
    StudentCourse studentCourse3 = new StudentCourse();
    studentCourse3.setCourseName("テストコース3");
    studentCourse3.setCourseId("3");
    List<StudentCourse> studentCourseList = List.of(studentCourse1, studentCourse2, studentCourse3);

    StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus();
    studentCourseStatus1.setCourseId("1");
    studentCourseStatus1.setStatus("仮申込");
    StudentCourseStatus studentCourseStatus2 = new StudentCourseStatus();
    studentCourseStatus2.setCourseId("2");
    studentCourseStatus2.setStatus("本申込");
    StudentCourseStatus studentCourseStatus3 = new StudentCourseStatus();
    studentCourseStatus3.setCourseId("3");
    studentCourseStatus3.setStatus("受講中");
    List<StudentCourseStatus> studentCourseStatusList = List.of(studentCourseStatus1, studentCourseStatus2, studentCourseStatus3);

    List<StudentCourse> actual = sut.assembleCourseListWithStatus(studentCourseList, studentCourseStatusList);

    assertThat(actual.get(0).getCourseStatus().getStatus()).isEqualTo("仮申込");
    assertThat(actual.get(1).getCourseStatus().getStatus()).isEqualTo("本申込");
    assertThat(actual.get(2).getCourseStatus().getStatus()).isEqualTo("受講中");

  }

  @Test
  void ステータスが見つからない場合はnullが設定されること() {
    StudentCourse studentCourse1 = new StudentCourse();
    studentCourse1.setCourseId("999");
    StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus();
    studentCourseStatus1.setCourseId("1");
    studentCourseStatus1.setStatus("仮申込");

    List<StudentCourse> actual = sut.assembleCourseListWithStatus(List.of(studentCourse1),List.of(studentCourseStatus1));

    assertThat(actual.get(0).getCourseStatus()).isNull();

  }

//  @Test
//  void 空のリストでも動作すること() {
//    // courseList=空, statusList=空
//    // → 空のリストが返る
//  }
//
//  @Test
//  void 複数のコースに同じステータスが紐づくこと() {
//    // 複数のcourseが同じcourseId（実際はありえないが念のため）
//  }
}