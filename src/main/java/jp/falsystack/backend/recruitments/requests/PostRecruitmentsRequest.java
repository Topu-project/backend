package jp.falsystack.backend.recruitments.requests;

import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Getter
@NoArgsConstructor
public class PostRecruitmentsRequest {
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
    public PostRecruitmentsRequest(
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

    public PostRecruitments toPostRecruitments() {
        return PostRecruitments.builder()
                .recruitmentCategories(this.recruitmentCategories)
                .progressMethods(this.progressMethods)
                .numberOfPeople(this.numberOfPeople)
                .progressPeriod(this.progressPeriod)
                .recruitmentDeadline(this.recruitmentDeadline)
                .contract(this.contract)
                .subject(this.subject)
                .content(this.content)
                .build();
    }
}
