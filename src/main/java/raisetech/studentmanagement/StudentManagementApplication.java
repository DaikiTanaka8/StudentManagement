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

@OpenAPIDefinition(info = @Info( //MEMO: OpenAPIDefinition→ドキュメントを自動生成するためにつけたもの。info→APIの基本情報。
    title = "受講生管理システム", //MEMO: APIの名前。
    description = "受講生（受講生情報、受講生コース情報）を管理するAPIです。", //MEMO: APIの説明。
    version = "1.0.0",
    termsOfService = "https://example.com/terms", //MEMO: 利用規約URL。
    contact = @Contact( //MEMO: 担当者情報。
        name = "担当者の名前",
        url = "https://tantosya.com",
        email = "tantosya@example.com"
    ),
    license = @License( //MEMO: ライセンス情報。このAPI（システム）をどういうルールで利用・公開していいか、を示すための利用許諾情報。
        name = "MIT License", //MEMO: ライセンス名。（MIT, Apache 2.0 など）
        url = "https://opensource.org/licenses/MIT" //MEMO: ライセンスの全文のリンク（公式URL）。
    )
),
    servers = { //MEMO: APIサーバー一覧。本番環境、テスト環境、ローカル環境などを記述できる。
        @Server(url = "http://localhost:8080", description = "ローカル環境"),
        @Server(url = "https://api.example.com", description = "本番環境")
    },
    tags = { //MEMO: タグの説明。カテゴリ分け。
        @Tag(name = "Student", description = "受講生関連のAPI"),
        @Tag(name = "StudentCourse", description = "受講生コース情報関連のAPI")
    }
)
@MapperScan("raisetech.studentmanagement.repository")
@SpringBootApplication
public class StudentManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(StudentManagementApplication.class, args);
  }
}
