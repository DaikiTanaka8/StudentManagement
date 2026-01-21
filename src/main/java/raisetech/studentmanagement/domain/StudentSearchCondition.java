package raisetech.studentmanagement.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

  @Schema(description = "受講生検索条件")
  @Getter
  @Setter
public class StudentSearchCondition {
    // フェーズ1：まず実装
    @Schema(description = "名前（部分一致）")
    private String name;

    @Schema(description = "地域（完全一致）")
    private String city;

    @Schema(description = "性別（完全一致）")
    private String gender;

    // フェーズ2：後で追加（今はコメントアウトでもOK）
    // @Schema(description = "コース名（部分一致）")
    // private String courseName;

    // @Schema(description = "最小年齢")
    // private Integer minAge;

    // @Schema(description = "最大年齢")
    // private Integer maxAge;

    // @Schema(description = "申込状況（完全一致）")
    // private String status;

    // フェーズ3：さらに将来
    // @Schema(description = "検索条件の結合方法（AND/OR）")
    // private SearchOperator operator;  // AND or OR

}
