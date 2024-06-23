package jp.falsystack.backend.recruitments.usecases.out;

import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RecruitmentsResponseForTopPage {
    private final RecruitmentCategories recruitmentCategories;
    private final List<String> techStacks;
    private final LocalDate recruitmentDeadline;
    private final String subject;

    @Builder
    private RecruitmentsResponseForTopPage(RecruitmentCategories recruitmentCategories, List<String> techStacks, LocalDate recruitmentDeadline, String subject) {
        this.recruitmentCategories = recruitmentCategories;
        this.techStacks = techStacks;
        this.recruitmentDeadline = recruitmentDeadline;
        this.subject = subject;
    }
}
