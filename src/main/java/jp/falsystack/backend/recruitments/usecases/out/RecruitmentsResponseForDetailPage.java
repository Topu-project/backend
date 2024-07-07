package jp.falsystack.backend.recruitments.usecases.out;

import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class RecruitmentsResponseForDetailPage {
    private final RecruitmentCategories recruitmentCategories;
    private final ProgressMethods progressMethods;
    private final Integer numberOfPeople;
    private final Integer progressPeriod;
    private final LocalDate recruitmentDeadline;
    private final String contract;
    private final String subject;
    private final String content;
    private final Long views;
    private final List<String> techStacks;
    private final List<String> recruitmentPositions;

    @Builder
    public RecruitmentsResponseForDetailPage(RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, Integer numberOfPeople, Integer progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, Long views, List<String> techStacks, List<String> recruitmentPositions) {
        this.recruitmentCategories = recruitmentCategories;
        this.progressMethods = progressMethods;
        this.numberOfPeople = numberOfPeople;
        this.progressPeriod = progressPeriod;
        this.recruitmentDeadline = recruitmentDeadline;
        this.contract = contract;
        this.subject = subject;
        this.content = content;
        this.views = views;
        this.techStacks = techStacks;
        this.recruitmentPositions = recruitmentPositions;
    }
}
