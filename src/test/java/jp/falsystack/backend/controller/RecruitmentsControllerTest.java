package jp.falsystack.backend.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.Period;
import jp.falsystack.backend.entities.ProgressMethods;
import jp.falsystack.backend.entities.RecruitmentCategories;
import jp.falsystack.backend.requests.PostRecruitments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitmentsControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("") // TODO:
  void createRecruitments() throws Exception {
    // given
    PostRecruitments postRecruitments =
        PostRecruitments.builder()
            .recruitmentCategories(RecruitmentCategories.PROJECT)
            .progressMethods(ProgressMethods.ALL)
            .numberOfPeople(3L)
            .progressPeriod(Period.ofMonths(3))
            .recruitmentDeadline(LocalDate.of(2024, 6, 30))
            .contract("opentalk@kakao.net")
            .subject("チームプロジェクトを一緒にする方を募集します。")
            .content("面白いチームプロジェクト")
            .build();

    String jsonString = objectMapper.writeValueAsString(postRecruitments);

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/recruitments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(MockMvcResultHandlers.print());
    // then
  }
}
