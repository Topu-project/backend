package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Recruitments extends BaseEntity {
    @Id
    @Column(name = "recruitments_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitmentCategories recruitmentCategories;

    @Enumerated(EnumType.STRING)
    private ProgressMethods progressMethods;

    private Long numberOfPeople;

    private Period progressPeriod;

    private LocalDate recruitmentDeadline;

    private String contract;

    private String subject;

    @Lob
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "views_id")
    private Views views;

    @OneToMany(mappedBy = "recruitments", cascade = CascadeType.ALL)
    private List<RecruitmentsTechStack> recruitmentsTechStacks;

    @Builder
    private Recruitments(RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, Long numberOfPeople, Period progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, Views views, List<RecruitmentsTechStack> recruitmentsTechStacks) {
        this.recruitmentCategories = recruitmentCategories;
        this.progressMethods = progressMethods;
        this.numberOfPeople = numberOfPeople;
        this.progressPeriod = progressPeriod;
        this.recruitmentDeadline = recruitmentDeadline;
        this.contract = contract;
        this.subject = subject;
        this.content = content;
        this.views = views;
        this.recruitmentsTechStacks = recruitmentsTechStacks == null ? new ArrayList<>() : recruitmentsTechStacks;
    }

    public void addRecruitmentsTechStack(RecruitmentsTechStack recruitmentsTechStack) {
        if (recruitmentsTechStack != null) {
            this.recruitmentsTechStacks.add(recruitmentsTechStack);
        }
    }

    public String getRelatedTechStackName(int index) {
        return this.recruitmentsTechStacks.get(index).getTechStackTags().getTechStackTagName();
    }
}