package jp.falsystack.backend.recruitments.repositories;

import jp.falsystack.backend.recruitments.entities.RecruitmentPositionTags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentPositionTagsRepository extends JpaRepository<RecruitmentPositionTags, Long> {
    Optional<RecruitmentPositionTags> findByRecruitmentPositionTagName(String recruitmentPositionTagName);
}
