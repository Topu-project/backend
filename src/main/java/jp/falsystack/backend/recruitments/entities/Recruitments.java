package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private List<RecruitmentsPositionTags> recruitmentsPositionTags;

    @Builder
    private Recruitments(RecruitmentCategories recruitmentCategories,
        ProgressMethods progressMethods, Integer numberOfPeople, Integer progressPeriod,
        LocalDate recruitmentDeadline, String contract, String subject, String content, Long views,
        List<RecruitmentsTechStack> recruitmentsTechStacks,
        List<RecruitmentsPositionTags> recruitmentsPositionTags) {
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
        this.recruitmentsPositionTags = recruitmentsPositionTags
            == null ? new ArrayList<>() : recruitmentsPositionTags;
    }

    public void relateToRecruitmentsTechStack(RecruitmentsTechStack recruitmentsTechStack) {
        if (recruitmentsTechStack != null) {
            this.recruitmentsTechStacks.add(recruitmentsTechStack);
        }
    }

    public void relateRecruitmentsRecruitmentPositionTags(
        RecruitmentsPositionTags recruitmentsPositionTags) {
        if (recruitmentsPositionTags != null) {
            this.recruitmentsPositionTags.add(recruitmentsPositionTags);
        }
    }

    public String getRelatedTechStackName(int index) {
        return this.recruitmentsTechStacks.get(index).getTechStackTags().getTechStackTagName();
    }

    public void increasePageViews() {
        this.views += 1;
    }
}