package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    private Integer numberOfPeople;

    private Integer progressPeriod;

    private LocalDate recruitmentDeadline;

    private String contract;

    private String subject;

    @Lob
    private String content;

    private Long views;

    @OneToMany(mappedBy = "recruitments", cascade = CascadeType.ALL)
    private List<RecruitmentsTechStack> recruitmentsTechStacks;

    @OneToMany(mappedBy = "recruitments", cascade = CascadeType.ALL)
    private List<RecruitmentsRecruitmentPositionTags> recruitmentsRecruitmentPositionTags;

    @Builder
    private Recruitments(RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, Integer numberOfPeople, Integer progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, Long views, List<RecruitmentsTechStack> recruitmentsTechStacks, List<RecruitmentsRecruitmentPositionTags> recruitmentsRecruitmentPositionTags) {
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
        this.recruitmentsRecruitmentPositionTags = recruitmentsRecruitmentPositionTags == null ? new ArrayList<>() : recruitmentsRecruitmentPositionTags;
    }

    public void relateToRecruitmentsTechStack(RecruitmentsTechStack recruitmentsTechStack) {
        if (recruitmentsTechStack != null) {
            this.recruitmentsTechStacks.add(recruitmentsTechStack);
        }
    }

    public void relateRecruitmentsRecruitmentPositionTags(RecruitmentsRecruitmentPositionTags recruitmentsRecruitmentPositionTags) {
        if (recruitmentsRecruitmentPositionTags != null) {
            this.recruitmentsRecruitmentPositionTags.add(recruitmentsRecruitmentPositionTags);
        }
    }

    public String getRelatedTechStackName(int index) {
        return this.recruitmentsTechStacks.get(index).getTechStackTags().getTechStackTagName();
    }

    public void increasePageViews() {
        this.views += 1;
    }
}