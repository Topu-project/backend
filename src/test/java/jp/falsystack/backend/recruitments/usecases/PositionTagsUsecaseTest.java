package jp.falsystack.backend.recruitments.usecases;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import jp.falsystack.backend.recruitments.entities.PositionTags;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.out.PositionTagResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = AutowireMode.ALL)
class PositionTagsUsecaseTest {

  private final RecruitmentsRepository recruitmentsRepository;
  private final PositionTagsRepository positionTagsRepository;
  private final TechStackTagsRepository techStackTagsRepository;

  private final PositionTagsUsecase usecase;

  @BeforeEach
  void setUp() {
    recruitmentsRepository.deleteAll();
    positionTagsRepository.deleteAll();
    techStackTagsRepository.deleteAll();
  }

  @Test
  @DisplayName("포지션 태그 전체 목록을 반환할 수 있다")
  void getPositionTagsResponse() {
    //given
    var spring = PositionTags.of("#Spring");
    var java = PositionTags.of("#Java");
    var react = PositionTags.of("#React");
    var javascript = PositionTags.of("#Javascript");
    var typescript = PositionTags.of("#Typescript");
    var mysql = PositionTags.of("#MySQL");
    var docker = PositionTags.of("#Docker");

    positionTagsRepository.saveAll(
        List.of(spring, java, react, javascript, typescript, mysql, docker));

    //when
    var response = usecase.getPositionTagsResponse();

    //then
    var tagNames = response.stream().map(PositionTagResponse::getPositionTagName).toList();

    assertThat(response).hasSize(7);
    assertThat(tagNames).containsExactly("#Spring", "#Java", "#React", "#Javascript", "#Typescript",
        "#MySQL", "#Docker");
  }
}