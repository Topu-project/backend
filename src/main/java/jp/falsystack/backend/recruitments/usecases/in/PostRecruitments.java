package jp.falsystack.backend.recruitments.usecases.in;

import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Getter
@NoArgsConstructor
public class PostRecruitments {

  private RecruitmentCategories recruitmentCategories;
  private ProgressMethods progressMethods;
  private Long numberOfPeople;
  private Period progressPeriod;
  private LocalDate recruitmentDeadline;
  private String contract;
  private String subject;
  private String content;

  @Builder
  private PostRecruitments(
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

  public Recruitments toRecruitment() {
    return Recruitments.builder()
            .recruitmentCategories(recruitmentCategories)
            .progressMethods(progressMethods)
            .numberOfPeople(numberOfPeople)
            .progressPeriod(progressPeriod)
            .recruitmentDeadline(recruitmentDeadline)
            .contract(contract)
            .subject(subject)
            .content(content)
            .build();
  }
}
