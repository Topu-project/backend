package jp.falsystack.backend.recruitments.usecases.out;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PositionTagResponse {

  private Long id;
  private String positionTagName;

  public static PositionTagResponse from(Long id, String positionTagName) {
    return new PositionTagResponse(id, positionTagName);
  }

}
