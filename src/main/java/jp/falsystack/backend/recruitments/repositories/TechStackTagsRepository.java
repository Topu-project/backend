package jp.falsystack.backend.recruitments.repositories;

import jp.falsystack.backend.recruitments.entities.TechStackTags;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechStackTagsRepository extends JpaRepository<TechStackTags, Long> {
    Optional<TechStackTags> findByTechStackTagName(String techStackTagName);
}
