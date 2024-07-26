package jp.falsystack.backend.recruitments.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForIndexPage;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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

  private final RecruitmentsRepository recruitmentsRepository;
  private final TechStackTagsRepository techStacksRepository;
  private final PositionTagsRepository positionsRepository;

  private final RecruitmentUsecases usecase;

  @BeforeEach
  void setUp() {
    recruitmentsRepository.deleteAll();
    positionsRepository.deleteAll();
    techStacksRepository.deleteAll();
  }

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
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("kotlin@gmila.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();

    // when
    usecase.post(recruitment);

    // then
    var recruitments = recruitmentsRepository.findAll();
    assertThat(recruitments).hasSize(1);
    assertThat(recruitments.getFirst().getRecruitmentCategories()).isEqualTo(
        RecruitmentCategories.PROJECT);
    assertThat(recruitments.getFirst().getRelatedTechStackName(0)).isEqualTo("#Kotlin");
    assertThat(recruitments.getFirst().getRecruitmentsPositionTags().getFirst().getPositionTags()
        .getPositionTagName()).isEqualTo("#백엔드");
    assertThat(recruitments.getFirst().getRecruitmentDeadline()).isEqualTo(
        LocalDate.of(2024, 10, 1));
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
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
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
    assertThat(positions.getFirst().getPositionTagName()).isEqualTo("#백엔드");

    assertThat(techStacks).hasSize(1);
    assertThat(techStacks.getFirst().getTechStackTagName()).isEqualTo("#Kotlin");
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
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("kotlin@gmila.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();

    // when
    usecase.post(recruitment);

    // then
    var recruitments = recruitmentsRepository.findAll();
    assertThat(recruitments).hasSize(1);
    assertThat(recruitments.getFirst().getRecruitmentCategories()).isEqualTo(
        RecruitmentCategories.PROJECT);
    assertThat(recruitments.getFirst().getRecruitmentsTechStacks()).isEmpty();
    assertThat(recruitments.getFirst().getRecruitmentsPositionTags()).isEmpty();
    assertThat(recruitments.getFirst().getRecruitmentDeadline()).isEqualTo(
        LocalDate.of(2024, 10, 1));
  }

  @Test
  @DisplayName("인덱스 페이지용 모집 응모글 목록을 반환할 수 있다")
  void getRecruitmentsForIndexPage() {
    // given
    var backendDeveloper = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("#코틀린")
        .recruitmentPositions("#백엔드")
        .numberOfPeople(1)
        .progressPeriod(2)
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("kotlin@gmail.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();
    usecase.post(backendDeveloper);

    var frontendDeveloper = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.STUDY)
        .progressMethods(ProgressMethods.OFFLINE)
        .techStacks("#리액트")
        .recruitmentPositions("#프런트엔드")
        .numberOfPeople(2)
        .progressPeriod(3)
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("react@gmail.com")
        .subject("프런트엔드 개발자 모집합니다")
        .content("제발 추노하지마세요")
        .build();
    usecase.post(frontendDeveloper);

    var devOps = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("#AWS")
        .recruitmentPositions("#데브옵스")
        .numberOfPeople(3)
        .progressPeriod(5)
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("devops@gmail.com")
        .subject("데브옵스 모집합니다!")
        .content("데브옵스는 추노안하죠?")
        .build();
    usecase.post(devOps);

    var dba = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.STUDY)
        .progressMethods(ProgressMethods.OFFLINE)
        .techStacks("#MySQL")
        .recruitmentPositions("#DBA")
        .numberOfPeople(1)
        .progressPeriod(6)
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("mysql@oracle.com")
        .subject("DBA 모집합니다")
        .content("DBA 없으면 걍 하려구요")
        .build();
    usecase.post(dba);

    // when
    var recruitments = usecase.getRecruitmentsForIndexPage();

    // then
    assertThat(recruitments).hasSize(4);
    assertThat(recruitments.stream()
        .filter(
            recruitment -> recruitment.getRecruitmentCategories() == RecruitmentCategories.STUDY)
        .toList()).hasSize(2);
    assertThat(recruitments.stream().map(RecruitmentsResponseForIndexPage::getTechStacks)
        .toList()).hasSize(4)
        .containsExactlyInAnyOrder(List.of("#MySQL"), List.of("#AWS"), List.of("#코틀린"),
            List.of("#리액트"));
  }

  @Transactional
  @Test
  @DisplayName("모집 응모 ID를 이용하여 모집 응모글을 조회하고 존재할 경우 반환한다")
  void getRecruitmentByID() {
    // given
    var backendDeveloper = PostRecruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ONLINE)
        .techStacks("#코틀린")
        .recruitmentPositions("#백엔드")
        .numberOfPeople(1)
        .progressPeriod(2)
        .recruitmentDeadline(LocalDate.of(2024, 10, 1))
        .contract("kotlin@gmail.com")
        .subject("백엔드 개발자 모집합니다")
        .content("추노하지마세요")
        .build();
    usecase.post(backendDeveloper);
    var savedRecruitment = recruitmentsRepository.findAll().getFirst();

    // when
    var foundRecruitment = usecase.getRecruitmentsById(savedRecruitment.getId());

    // then
    assertThat(foundRecruitment.getRecruitmentCategories()).isEqualTo(
        RecruitmentCategories.PROJECT);
    assertThat(foundRecruitment.getProgressMethods()).isEqualTo(ProgressMethods.ONLINE);
    assertThat(foundRecruitment.getTechStacks().getFirst()).isEqualTo("#코틀린");
    assertThat(foundRecruitment.getRecruitmentPositions().getFirst()).isEqualTo("#백엔드");
    assertThat(foundRecruitment.getNumberOfPeople()).isEqualTo(1);
    assertThat(foundRecruitment.getProgressPeriod()).isEqualTo(2);
    assertThat(foundRecruitment.getRecruitmentDeadline()).isEqualTo(LocalDate.of(2024, 10, 01));
    assertThat(foundRecruitment.getContract()).isEqualTo("kotlin@gmail.com");
    assertThat(foundRecruitment.getSubject()).isEqualTo("백엔드 개발자 모집합니다");
    assertThat(foundRecruitment.getContent()).isEqualTo("추노하지마세요");
  }

}