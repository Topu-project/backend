package jp.falsystack.backend.controller;

import jp.falsystack.backend.requests.PostRecruitments;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/recruitments")
@RestController
public class RecruitmentsController {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void recruitments(@RequestBody PostRecruitments postRecruitments) {
    System.out.println("RecruitmentsController.recruitments");
  }
}
