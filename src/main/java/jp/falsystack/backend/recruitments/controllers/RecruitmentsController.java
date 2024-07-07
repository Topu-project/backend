package jp.falsystack.backend.recruitments.controllers;

import jakarta.validation.Valid;
import jp.falsystack.backend.recruitments.controllers.out.RecruitmentsErrorResponse;
import jp.falsystack.backend.recruitments.requests.PostRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.RecruitmentUsecases;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForDetailPage;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForIndexPage;
import jp.falsystack.backend.web.out.TopuServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public TopuServiceResponse<RecruitmentsResponseForDetailPage, RecruitmentsErrorResponse> getRecruitmentById(@PathVariable Long recruitmentId) {
        return TopuServiceResponse.from(recruitmentUsecases.getRecruitmentsById(recruitmentId), null);
    }
}
