package jp.falsystack.backend.controller;

import static org.junit.jupiter.api.Assertions.*;

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

  @Test
  @DisplayName("") // TODO:
  void createRecruitments() throws Exception {
    // given

    // when
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/recruitments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new byte[0]))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(MockMvcResultHandlers.print());
    // then
  }
}
