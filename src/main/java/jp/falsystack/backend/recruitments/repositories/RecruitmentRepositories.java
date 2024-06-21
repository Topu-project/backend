package jp.falsystack.backend.recruitments.repositories;

import jp.falsystack.backend.recruitments.entities.Recruitments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepositories extends JpaRepository<Recruitments, Long> {
}
