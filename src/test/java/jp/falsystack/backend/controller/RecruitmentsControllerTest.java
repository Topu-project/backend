package jp.falsystack.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.backend.recruitments.entities.RecruitmentPositionTags;
import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.RecruitmentsTechStack;
import jp.falsystack.backend.recruitments.entities.TechStackTags;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.repositories.RecruitmentPositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.requests.PostRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private RecruitmentsRepository recruitmentRepositories;
    @Autowired
    private TechStackTagsRepository techStackTagsRepository;
    @Autowired
    private RecruitmentPositionTagsRepository recruitmentPositionTagsRepository;

    @BeforeEach
    void setUp() throws Exception {
        // TODO: deleteAll 과 deleteAllInBatch 차이점 공부
        recruitmentRepositories.deleteAll();
    }

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
                        .recruitmentPositions("#Frontend#Backend#Infra")
                        .numberOfPeople(3L)
                        .progressPeriod(3)
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

        List<RecruitmentPositionTags> positions = recruitmentPositionTagsRepository.findAll();
        assertThat(positions.size()).isEqualTo(3);
        assertThat(positions.get(0).getRecruitmentPositionTagName()).isEqualTo("#Frontend");
        assertThat(positions.get(1).getRecruitmentPositionTagName()).isEqualTo("#Backend");
        assertThat(positions.get(2).getRecruitmentPositionTagName()).isEqualTo("#Infra");
    }

    @Test
    @DisplayName("필수항목들을 입력하지 않으면 모집 게시글을 등록 할 수 없다.")
    void cannotCreateRecruitments() throws Exception {
        // given
        PostRecruitmentsRequest request = PostRecruitmentsRequest.builder()
                .recruitmentCategories(RecruitmentCategories.PROJECT)
                .progressMethods(null)
                .techStacks("#Spring#Java")
                .numberOfPeople(3L)
                .progressPeriod(3)
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

    @Test
    @DisplayName("모집 게시글의 목록을 취득할 수 있다.")
    void getRecruitments() throws Exception {
        // given
        TechStackTags techStackTags = TechStackTags.of("#Spring");

        var recruitments = new ArrayList<Recruitments>();
        for (int i = 0; i < 3; i++) {
            var recruitment = Recruitments.builder()
                    .recruitmentCategories(RecruitmentCategories.PROJECT)
                    .progressMethods(ProgressMethods.ALL)
                    .numberOfPeople(i + 1L)
                    .progressPeriod(i + 1)
                    .recruitmentDeadline(LocalDate.of(2024, 6, 30))
                    .contract("opentalk@kakao.net")
                    .subject("테스트 데이터 : " + i)
                    .content("테스트 컨튼츠 : " + i)
                    .build();

            RecruitmentsTechStack recruitmentsTechStack = RecruitmentsTechStack.builder()
                    .recruitments(recruitment)
                    .techStackTags(techStackTags)
                    .build();

            recruitment.relateToRecruitmentsTechStack(recruitmentsTechStack);
            techStackTags.addRecruitmentsTechStack(recruitmentsTechStack);
            recruitments.add(recruitment);
        }
        recruitmentRepositories.saveAll(recruitments);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/recruitments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()", Matchers.is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].recruitmentCategories", Matchers.is("PROJECT")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].techStacks[0]", Matchers.is("#Spring")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].recruitmentDeadline", Matchers.is("2024-06-30")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].subject", Matchers.is("테스트 데이터 : 0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error", Matchers.nullValue()))
                .andDo(MockMvcResultHandlers.print());
    }
}
