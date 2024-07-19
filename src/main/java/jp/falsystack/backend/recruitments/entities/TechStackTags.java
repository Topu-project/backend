package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TechStackTags extends BaseEntity {
    @Id
    @Column(name = "tech_stack_tags_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String techStackTagName;

    @OneToMany(mappedBy = "techStackTags")
    private List<RecruitmentsTechStack> recruitmentsTechStacks = new ArrayList<>();

    private TechStackTags(String techStackTagName) {
        this.techStackTagName = techStackTagName;
    }

    public static TechStackTags of(String techStackTagName) {
        return new TechStackTags(techStackTagName);
    }

    public void addRecruitmentsTechStack(RecruitmentsTechStack recruitmentsTechStack) {
        if (recruitmentsTechStacks != null) {
            this.getRecruitmentsTechStacks().add(recruitmentsTechStack);
        }
    }


}
