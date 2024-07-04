package jp.falsystack.backend.recruitments.usecases.in;

import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostRecruitments {

  private RecruitmentCategories recruitmentCategories;
  private ProgressMethods progressMethods;
  private String techStacks;
  private String recruitmentPositions;
  private Long numberOfPeople;
  private Integer progressPeriod;
  private LocalDate recruitmentDeadline;
  private String contract;
  private String subject;
  private String content;

  @Builder
  private PostRecruitments(
      RecruitmentCategories recruitmentCategories,
      ProgressMethods progressMethods,
      String techStacks,
      String recruitmentPositions,
      Long numberOfPeople,
      Integer progressPeriod,
      LocalDate recruitmentDeadline,
      String contract,
      String subject,
      String content) {
    this.recruitmentCategories = recruitmentCategories;
    this.progressMethods = progressMethods;
    this.techStacks = techStacks;
    this.recruitmentPositions = recruitmentPositions;
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
