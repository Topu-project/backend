package jp.falsystack.backend.requests;

import java.time.LocalDate;
import java.time.Period;
import jp.falsystack.backend.entities.ProgressMethods;
import jp.falsystack.backend.entities.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRecruitments {

  // TODO: Validation
  private RecruitmentCategories recruitmentCategories;
  private ProgressMethods progressMethods;
  private Long numberOfPeople;
  private Period progressPeriod;
  private LocalDate recruitmentDeadline;
  private String contract;
  private String subject;
  private String content;

  @Builder
  public PostRecruitments(
      RecruitmentCategories recruitmentCategories,
      ProgressMethods progressMethods,
      Long numberOfPeople,
      Period progressPeriod,
      LocalDate recruitmentDeadline,
      String contract,
      String subject,
      String content) {
    this.recruitmentCategories = recruitmentCategories;
    this.progressMethods = progressMethods;
    this.numberOfPeople = numberOfPeople;
    this.progressPeriod = progressPeriod;
    this.recruitmentDeadline = recruitmentDeadline;
    this.contract = contract;
    this.subject = subject;
    this.content = content;
  }
}
