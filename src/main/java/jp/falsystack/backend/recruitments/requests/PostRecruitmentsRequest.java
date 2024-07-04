package jp.falsystack.backend.recruitments.requests;

import jakarta.validation.constraints.NotNull;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class PostRecruitmentsRequest {

    @NotNull(message = "{notnull}")
    private RecruitmentCategories recruitmentCategories;
    @NotNull(message = "{notnull}")
    private ProgressMethods progressMethods;
    @NotNull(message = "{notnull}")
    private String techStacks;
    @NotNull(message = "{notnull}")
    private String recruitmentPositions;
    @NotNull(message = "{notnull}")
    private Long numberOfPeople;
    @NotNull(message = "{notnull}")
    private Integer progressPeriod;
    @NotNull(message = "{notnull}")
    private LocalDate recruitmentDeadline;
    @NotNull(message = "{notnull}")
    private String contract;
    @NotNull(message = "{notnull}")
    private String subject;
    @NotNull(message = "{notnull}")
    private String content;

    @Builder
    private PostRecruitmentsRequest(
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

    public PostRecruitments toPostRecruitments() {
        return PostRecruitments.builder()
                .recruitmentCategories(this.recruitmentCategories)
                .progressMethods(this.progressMethods)
                .techStacks(this.techStacks)
                .recruitmentPositions(this.recruitmentPositions)
                .numberOfPeople(this.numberOfPeople)
                .progressPeriod(this.progressPeriod)
                .recruitmentDeadline(this.recruitmentDeadline)
                .contract(this.contract)
                .subject(this.subject)
                .content(this.content)
                .build();
    }
}
