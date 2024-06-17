package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentsRecruitmentPositionTags extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitments_position_tags_id")
    private RecruitmentsRecruitmentPositionTags recruitmentsRecruitmentPositionTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitments_id")
    private Recruitments recruitments;
}
