package jp.falsystack.backend.recruitments.controllers;

import java.util.List;
import jp.falsystack.backend.recruitments.controllers.out.RecruitmentsErrorResponse;
import jp.falsystack.backend.recruitments.usecases.PositionTagsUsecase;
import jp.falsystack.backend.recruitments.usecases.out.PositionTagResponse;
import jp.falsystack.backend.web.out.TopuServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionTagsController {

  private final PositionTagsUsecase positionTagsUsecase;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public TopuServiceResponse<List<PositionTagResponse>, RecruitmentsErrorResponse> getPositionTags() {
    return TopuServiceResponse.from(positionTagsUsecase.getPositionTagsResponse(), null);
  }
}
