package jp.falsystack.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.TechStackTags;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.repositories.RecruitmentRepositories;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.requests.PostRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitmentsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RecruitmentRepositories recruitmentRepositories;
    @Autowired
    private TechStackTagsRepository techStackTagsRepository;

    @Transactional
    @Test
    @DisplayName("모집 게시글을 등록할 수 있다.")
    void createRecruitments() throws Exception {
        // given
        PostRecruitments request =
                PostRecruitments.builder()
                        .recruitmentCategories(RecruitmentCategories.PROJECT)
                        .progressMethods(ProgressMethods.ALL)
                        .techStacks("#Spring#Java")
                        .numberOfPeople(3L)
                        .progressPeriod(Period.ofMonths(3))
                        .recruitmentDeadline(LocalDate.of(2024, 6, 30))
                        .contract("opentalk@kakao.net")
                        .subject("チームプロジェクトを一緒にする方を募集します")
                        .content("面白いチームプロジェクト")
                        .build();

        String jsonString = objectMapper.writeValueAsString(request);

        // when
        mockMvc
                .perform(
                        MockMvcRequestBuilders.post("/recruitments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());

        // then
        Recruitments findRecruitment = recruitmentRepositories.findAll().get(0);
        assertThat(recruitmentRepositories.count()).isEqualTo(1);
        assertThat(findRecruitment.getContract()).isEqualTo("opentalk@kakao.net");
        assertThat(findRecruitment.getSubject()).isEqualTo("チームプロジェクトを一緒にする方を募集します");
        assertThat(findRecruitment.getContent()).isEqualTo("面白いチームプロジェクト");
        assertThat(findRecruitment.getRelatedTechStackName(0)).isEqualTo("#Spring");
        assertThat(findRecruitment.getRelatedTechStackName(1)).isEqualTo("#Java");

        List<TechStackTags> techStackTags = techStackTagsRepository.findAll();
        assertThat(techStackTags.size()).isEqualTo(2);
        assertThat(techStackTags.get(0).getTechStackTagName()).isEqualTo("#Spring");
        assertThat(techStackTags.get(1).getTechStackTagName()).isEqualTo("#Java");
    }

    @Test
    @DisplayName("필수항목들을 입력하지 않으면 모집 게시글을 등록 할 수 없다.")
    void cannotCreateRecruitments() throws Exception {
        // given
        PostRecruitmentsRequest request = PostRecruitmentsRequest.builder()
                .recruitmentCategories(RecruitmentCategories.PROJECT)
                .progressMethods(null)
                .numberOfPeople(3L)
                .progressPeriod(Period.ofMonths(3))
                .recruitmentDeadline(LocalDate.of(2024, 6, 30))
                .contract("opentalk@kakao.net")
                .subject("チームプロジェクトを一緒にする方を募集します")
                .content("面白いチームプロジェクト")
                .build();

        String jsonString = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/recruitments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("Bad Request"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.validationErrors.progressMethods").value("必須です。値を入力してください。"))
                .andDo(MockMvcResultHandlers.print());

    }
}
