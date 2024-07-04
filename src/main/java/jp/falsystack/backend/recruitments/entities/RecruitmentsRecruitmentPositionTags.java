package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentsRecruitmentPositionTags extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recruitments_position_tags_id")
    private RecruitmentPositionTags recruitmentPositionTags;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recruitments_id")
    private Recruitments recruitments;

    @Builder
    private RecruitmentsRecruitmentPositionTags(Recruitments recruitments, RecruitmentPositionTags recruitmentPositionTags) {
        this.recruitments = recruitments;
        this.recruitmentPositionTags = recruitmentPositionTags;
    }
}
