package jp.falsystack.backend.recruitments.repositories;

import java.util.Optional;
import jp.falsystack.backend.recruitments.entities.PositionTags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionTagsRepository extends JpaRepository<PositionTags, Long> {

  Optional<PositionTags> findByPositionTagName(String positionTagName);
}
