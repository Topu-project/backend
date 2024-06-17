package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentsTechStack extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_tags_id")
    private TechStackTags techStackTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruitments_id")
    private Recruitments recruitments;


}
