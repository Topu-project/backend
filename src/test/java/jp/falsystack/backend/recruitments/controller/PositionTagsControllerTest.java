package jp.falsystack.backend.recruitments.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import jp.falsystack.backend.recruitments.entities.PositionTags;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class PositionTagsControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PositionTagsRepository positionTagsRepository;

  @BeforeEach
  void setUp() throws Exception {
    positionTagsRepository.deleteAll();
  }

  @DisplayName("모집 포지션의 목록을 불러올 수 있다.")
  @Test
  void getPositionTags() throws Exception {
    // given
    // 모집 포지션
    var backendEngineer = PositionTags.of("#Backend");
    var infraEngineer = PositionTags.of("#Infra");

    positionTagsRepository.saveAll(List.of(backendEngineer, infraEngineer));

    // expected
    mockMvc.perform(MockMvcRequestBuilders.get("/positions")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id", Matchers.is(1)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data[0].positionTagName", Matchers.is("#Backend")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id", Matchers.is(2)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.data[1].positionTagName", Matchers.is("#Infra")))
        .andDo(print());
  }
}
