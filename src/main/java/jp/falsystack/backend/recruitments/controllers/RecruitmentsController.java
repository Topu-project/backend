package jp.falsystack.backend.recruitments.controllers;

import jakarta.validation.Valid;
import java.util.List;
import jp.falsystack.backend.recruitments.controllers.out.RecruitmentsErrorResponse;
import jp.falsystack.backend.recruitments.requests.PostRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.RecruitmentUsecases;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForDetailPage;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForIndexPage;
import jp.falsystack.backend.web.out.TopuServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/recruitments")
@RestController
@RequiredArgsConstructor
public class RecruitmentsController {

  private final RecruitmentUsecases recruitmentUsecases;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void recruitments(@RequestBody @Valid PostRecruitmentsRequest postRecruitmentsRequest) {
    recruitmentUsecases.post(postRecruitmentsRequest.toPostRecruitments());
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public TopuServiceResponse<List<RecruitmentsResponseForIndexPage>, RecruitmentsErrorResponse> recruitments() {
    return TopuServiceResponse.from(recruitmentUsecases.getRecruitmentsForIndexPage(), null);
  }

  @GetMapping("/{recruitmentId}")
  @ResponseStatus(HttpStatus.OK)
  public TopuServiceResponse<RecruitmentsResponseForDetailPage, RecruitmentsErrorResponse>
  getRecruitmentById(@PathVariable Long recruitmentId) {
    return TopuServiceResponse.from(recruitmentUsecases.getRecruitmentsById(recruitmentId), null);
  }

  @DeleteMapping("/{recruitmentId}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteRecruitmentById(@PathVariable Long recruitmentId) {
    recruitmentUsecases.deleteRecruitmentById(recruitmentId);
  }
}
