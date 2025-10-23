package raisetech.StudentManagement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  @Autowired
  private StudentRepository repository;

  private Map<String, String>studentMap = new ConcurrentHashMap<>();

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}


  // 下記は講義18の講義内のコード＋講義20で編集
  @GetMapping("/student")
  public String getStudent(@RequestParam String name) {
    Student student = repository.searchByName(name);
    return student.getName() + " " + student.getAge() + "歳";
  }

  @PostMapping("/student")
  public void registerStudent(@RequestParam String name,@RequestParam int age){
    repository.registerStudent(name ,age);
  }

  @PatchMapping("/student")
  public void updateStudentName(@RequestParam String name,@RequestParam int age){
    repository.updateStudent(name, age);
  }

  @DeleteMapping("/student")
  public void deleteStudent(@RequestParam String name){
    repository.deleteStudent(name);
  }

  // 下記は講義20の課題のコード
  @GetMapping("/students")
  public String getAllStudent() {
    StringBuilder result = new StringBuilder();
    List<Student> students = repository.getAllStudent();
    for (Student student : students){
      result.append(student.getName())
          .append("　")
          .append(student.getAge())
          .append("歳\n");
    }
    return result.toString();
  }


  // 下記は講義18の課題のコード
  @PostMapping("/studentMap")
  public void addStudentMap(@RequestParam String nameMap,@RequestParam String ageMap){
    studentMap.put(nameMap, ageMap);
  }

  @GetMapping("/studentMap")
  public String getStudentMap() {
    StringBuilder result = new StringBuilder();
    for (String key : studentMap.keySet()){
      result.append(key)
          .append("さんは")
          .append(studentMap.get(key))
          .append("歳です。\n");
    }
    return result.toString();
  }

  @PutMapping("/studentMap/{nameMap}")
  public void updateStudentAge(@PathVariable String nameMap,@RequestParam String ageMap){
    studentMap.put(nameMap, ageMap);
  }
}
