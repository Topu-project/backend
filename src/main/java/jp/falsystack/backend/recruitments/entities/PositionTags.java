package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PositionTags extends BaseEntity {

    @OneToMany(mappedBy = "positionTags")
    private final List<RecruitmentsPositionTags> recruitmentsPositionTags = new ArrayList<>();
    @Id
    @Column(name = "position_tags_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String positionTagName;

    private PositionTags(String positionTagName) {
        this.positionTagName = positionTagName;
    }

    public static PositionTags of(String positionTagName) {
        return new PositionTags(positionTagName);
    }

    public void relateToRecruitmentsRecruitmentPositionTags(
        RecruitmentsPositionTags recruitmentPositionTags) {
        if (recruitmentPositionTags != null) {
            this.getRecruitmentsPositionTags().add(recruitmentPositionTags);
        }
    }
}
