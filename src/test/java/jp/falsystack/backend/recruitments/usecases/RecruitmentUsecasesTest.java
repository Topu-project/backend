package jp.falsystack.backend.recruitments.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
class RecruitmentUsecasesTest {

  private final RecruitmentsRepository repository;
  private final TechStackTagsRepository techStacksRepository;
  private final PositionTagsRepository positionsRepository;
  private final RecruitmentUsecases usecase;

  @Transactional
  @Test
  @DisplayName("모집 응모글을 작성하면 모집 응모 전체 목록에 포함된다")
  void post() {
    // given
    var recruitment = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("#Kotlin")
        .recruitmentPositions("#백엔드")
        .numberOfPeople(2)
        .progressPeriod(1)
        .recruitmentDeadline(LocalDate.of(2024, 10, 01))
        .contract("kotlin@gmila.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();

    // when
    usecase.post(recruitment);

    // then
    var recruitments = repository.findAll();
    assertThat(recruitments).hasSize(1);
    assertThat(recruitments.get(0).getRecruitmentCategories()).isEqualTo(
        RecruitmentCategories.PROJECT);
    assertThat(recruitments.get(0).getRelatedTechStackName(0)).isEqualTo("#Kotlin");
    assertThat(recruitments.get(0).getRecruitmentsPositionTags().get(0).getPositionTags()
        .getPositionTagName()).isEqualTo("#백엔드");
    assertThat(recruitments.get(0).getRecruitmentDeadline()).isEqualTo(LocalDate.of(2024, 10, 01));
  }

  @Test
  @DisplayName("모집 응모글을 작성하면 기술스택과 모집 포지션이 등록된다")
  void whenWriteRecruitmentTechStackAndPositionWillBeRegistered() {
    // given
    var recruitment = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("#Kotlin")
        .recruitmentPositions("#백엔드")
        .numberOfPeople(2)
        .progressPeriod(1)
        .recruitmentDeadline(LocalDate.of(2024, 10, 01))
        .contract("kotlin@gmila.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();

    // when
    usecase.post(recruitment);

    // then
    var positions = positionsRepository.findAll();
    var techStacks = techStacksRepository.findAll();

    assertThat(positions).hasSize(1);
    assertThat(positions.get(0).getPositionTagName()).isEqualTo("#백엔드");

    assertThat(techStacks).hasSize(1);
    assertThat(techStacks.get(0).getTechStackTagName()).isEqualTo("#Kotlin");
  }

  @Transactional
  @Test
  @DisplayName("기술스택과 모집포지션은 '#'으로 시작하지 않으면 등록되지 않는다.")
  void notMatchPattern() {
    // given
    var recruitment = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("Kotlin")
        .recruitmentPositions("백엔드")
        .numberOfPeople(2)
        .progressPeriod(1)
        .recruitmentDeadline(LocalDate.of(2024, 10, 01))
        .contract("kotlin@gmila.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();

    // when
    usecase.post(recruitment);

    // then
    var recruitments = repository.findAll();
    assertThat(recruitments).hasSize(1);
    assertThat(recruitments.get(0).getRecruitmentCategories()).isEqualTo(
        RecruitmentCategories.PROJECT);
    assertThat(recruitments.get(0).getRecruitmentsTechStacks()).hasSize(0);
    assertThat(recruitments.get(0).getRecruitmentsPositionTags()).hasSize(0);
    assertThat(recruitments.get(0).getRecruitmentDeadline()).isEqualTo(LocalDate.of(2024, 10, 01));
  }

}