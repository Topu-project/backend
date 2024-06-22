package jp.falsystack.backend.recruitments.controllers;

import jakarta.validation.Valid;
import jp.falsystack.backend.recruitments.requests.PostRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.RecruitmentUsecases;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
