package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentsTechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tech_stack_tags_id")
    private TechStackTags techStackTags;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recruitments_id")
    private Recruitments recruitments;

    @Builder
    private RecruitmentsTechStack(TechStackTags techStackTags, Recruitments recruitments) {
        this.techStackTags = techStackTags;
        this.recruitments = recruitments;
    }
}
