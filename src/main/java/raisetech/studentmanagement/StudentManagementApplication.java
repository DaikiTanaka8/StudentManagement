package raisetech.studentmanagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
    title = "受講生管理システム",
    description = "受講生（受講生情報、受講生コース情報、コース申込状況）を管理するAPIです。",
    version = "1.0.0",
    termsOfService = "https://example.com/terms",
    contact = @Contact(
        name = "担当者の名前",
        url = "https://tantosya.com",
        email = "tantosya@example.com"
    ),
    license = @License(
        name = "MIT License",
        url = "https://opensource.org/licenses/MIT"
    )
),
    servers = {
        @Server(url = "http://localhost:8080", description = "ローカル環境"),
        @Server(url = "https://api.example.com", description = "本番環境")
    },
    tags = {
        @Tag(name = "Student", description = "受講生関連のAPI"),
        @Tag(name = "StudentCourse", description = "受講生コース情報関連のAPI"),
        @Tag(name = "StudentCourseStatus", description = "コース申込状況関連のAPI")
    }
)
@MapperScan("raisetech.studentmanagement.repository")
@SpringBootApplication
public class StudentManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }
}
