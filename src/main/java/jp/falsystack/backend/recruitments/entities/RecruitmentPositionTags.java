package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentPositionTags extends BaseEntity{

    @Id
    @Column(name = "recruitment_position_tags_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String recruitmentPositionTagName;

    @OneToMany(mappedBy = "recruitmentPositionTags")
    private final List<RecruitmentsRecruitmentPositionTags> recruitmentsRecruitmentPositionTags = new ArrayList<>();

    private RecruitmentPositionTags(String recruitmentPositionTagName) {
        this.recruitmentPositionTagName = recruitmentPositionTagName;
    }

    public static RecruitmentPositionTags of(String recruitmentPositionTagName) {
        return new RecruitmentPositionTags(recruitmentPositionTagName);
    }

    public void relateToRecruitmentsRecruitmentPositionTags(RecruitmentsRecruitmentPositionTags recruitmentPositionTags) {
        if (recruitmentPositionTags != null) {
            this.getRecruitmentsRecruitmentPositionTags().add(recruitmentPositionTags);
        }
    }
}
