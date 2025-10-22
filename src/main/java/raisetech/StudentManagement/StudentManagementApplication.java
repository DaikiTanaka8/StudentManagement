package raisetech.StudentManagement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class StudentManagementApplication {

  private String name = "Daiki Tanaka";
  private String age = "31";

  private Map<String, String>studentMap = new ConcurrentHashMap<>();


	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApplication.class, args);
	}
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


  @GetMapping("/studentInfo")
  public String getStudentInfo() {
    return name + " " + age + "歳";
  }

  @PostMapping("/studentInfo")
  public void setStudentInfo(@RequestParam String name,@RequestParam String age){
    this.name = name;
    this.age = age;
  }

  @PostMapping("/studentName")
  public void updateStudentName(@RequestParam String name){
    this.name = name;
  }

}
