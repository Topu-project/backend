package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecruitmentPositionTags extends BaseEntity{

    @Id
    @Column(name = "recruitment_position_tags")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String recruitmentPositionTagName;

}
