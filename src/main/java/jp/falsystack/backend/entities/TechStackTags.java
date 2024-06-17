package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TechStackTags extends BaseEntity {
    @Id
    @Column(name = "tech_stack_tags_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String techStackTagName;

}
