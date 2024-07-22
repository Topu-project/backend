package jp.falsystack.backend.docs;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import jp.falsystack.backend.recruitments.entities.PositionTags;
import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.RecruitmentsPositionTags;
import jp.falsystack.backend.recruitments.entities.RecruitmentsTechStack;
import jp.falsystack.backend.recruitments.entities.TechStackTags;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.requests.UpdateRecruitmentsRequest;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
class RecruitmentsControllerDocTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RecruitmentsRepository recruitmentsRepository;
  @Autowired
  private PositionTagsRepository positionTagsRepository;
  @Autowired
  private TechStackTagsRepository techStackTagsRepository;

  @BeforeEach
  void setUp() {
    recruitmentsRepository.deleteAll();
    positionTagsRepository.deleteAll();
    techStackTagsRepository.deleteAll();
  }

  @Test
  @DisplayName("모집 응모글을 작성할 수 있다")
  void postRecruitments() throws Exception {
    // given
    PostRecruitments request =
        PostRecruitments.builder()
            .recruitmentCategories(RecruitmentCategories.PROJECT)
            .progressMethods(ProgressMethods.ALL)
            .techStacks("#Spring#Java")
            .recruitmentPositions("#Backend#Frontend#Infra")
            .numberOfPeople(3)
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
            RestDocumentationRequestBuilders.post("/recruitments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andDo(MockMvcRestDocumentation.document("post-recruitments",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            PayloadDocumentation.requestFields(
                PayloadDocumentation.fieldWithPath("recruitmentCategories").type(STRING)
                    .description("모집 카테고리"),
                PayloadDocumentation.fieldWithPath("progressMethods").type(STRING)
                    .description("진행방법"),
                PayloadDocumentation.fieldWithPath("techStacks").type(STRING).description("기술 스택"),
                PayloadDocumentation.fieldWithPath("recruitmentPositions").type(STRING)
                    .description("모집 포지션"),
                PayloadDocumentation.fieldWithPath("numberOfPeople").type(NUMBER)
                    .description("모집 인원"),
                PayloadDocumentation.fieldWithPath("progressPeriod").type(NUMBER)
                    .description("모집 기간"),
                PayloadDocumentation.fieldWithPath("recruitmentDeadline").type(STRING)
                    .description("마감 날짜"),
                PayloadDocumentation.fieldWithPath("contract").type(STRING).description("연락처"),
                PayloadDocumentation.fieldWithPath("subject").type(STRING).description("제목"),
                PayloadDocumentation.fieldWithPath("content").type(STRING).description("내용")
            )));
  }

  @Test
  @DisplayName("인덱스페이지 모집 응모 리스트")
  void getRecruitmentsForIndexPage() throws Exception {
    // given

    // 모집 기술 스택
    Recruitments recruitments1 = Recruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ALL)
        .numberOfPeople(3)
        .progressPeriod(3)
        .recruitmentDeadline(LocalDate.of(2024, 6, 30))
        .contract("opentalk@kakao.net")
        .subject("チームプロジェクトを一緒にする方を募集します")
        .content("面白いチームプロジェクト")
        .build();

    TechStackTags springTag = TechStackTags.of("#Spring");
    TechStackTags javaTag = TechStackTags.of("#Java");

    RecruitmentsTechStack javaRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(javaTag)
        .build();

    RecruitmentsTechStack springRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(springTag)
        .build();

    recruitments1.relateToRecruitmentsTechStack(javaRecruitments);
    recruitments1.relateToRecruitmentsTechStack(springRecruitments);

    // 모집 포지션
    var backendEngineer = PositionTags.of("#Backend");
    var infraEngineer = PositionTags.of("#Infra");

    var backendRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(backendEngineer)
        .build();

    var infraRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(infraEngineer)
        .build();

    recruitments1.relateRecruitmentsRecruitmentPositionTags(backendRecruitments);
    recruitments1.relateRecruitmentsRecruitmentPositionTags(infraRecruitments);
    recruitmentsRepository.save(recruitments1);

    //
    Recruitments recruitments2 = Recruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ALL)
        .numberOfPeople(1)
        .progressPeriod(2)
        .recruitmentDeadline(LocalDate.of(2024, 7, 30))
        .contract("opentalk@kakao.net")
        .subject("チームプロジェクトを一緒にするフロントエンドエンジニアの方を募集します")
        .content("面白いチームプロジェクト")
        .build();

    TechStackTags reactTag = TechStackTags.of("#React");
    TechStackTags typescriptTag = TechStackTags.of("#Typescript");

    RecruitmentsTechStack reactRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments2)
        .techStackTags(reactTag)
        .build();

    RecruitmentsTechStack typescriptRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments2)
        .techStackTags(typescriptTag)
        .build();

    recruitments2.relateToRecruitmentsTechStack(reactRecruitments);
    recruitments2.relateToRecruitmentsTechStack(typescriptRecruitments);

    // 모집 포지션
    var frontendEngineer = PositionTags.of("#Frontend");

    var frontendRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments2)
        .positionTags(frontendEngineer)
        .build();

    recruitments2.relateRecruitmentsRecruitmentPositionTags(frontendRecruitments);
    recruitmentsRepository.save(recruitments2);

    mockMvc.perform(RestDocumentationRequestBuilders.get("/recruitments")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
        .andDo(MockMvcRestDocumentation.document("get-recruitments-for-index-page",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("data.[].recruitmentCategories").type(STRING)
                    .description("모집 카테고리"),
                PayloadDocumentation.fieldWithPath("data.[].techStacks").type(ARRAY)
                    .description("기술 스택"),
                PayloadDocumentation.fieldWithPath("data.[].recruitmentPositions").type(ARRAY)
                    .description("모집 포지션"),
                PayloadDocumentation.fieldWithPath("data.[].recruitmentDeadline").type(STRING)
                    .description("모집 마감일"),
                PayloadDocumentation.fieldWithPath("data.[].subject").type(STRING)
                    .description("모집 게시글 제목"),
                PayloadDocumentation.fieldWithPath("error").type(NULL).description("에러가없으면 null")
            )));
  }

  @Test
  @DisplayName("모집글의 상세페에지를 확인할 수 있다")
  void getRecruitmentsById() throws Exception {
    // given
    // 모집 기술 스택
    var recruitments1 = Recruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ALL)
        .numberOfPeople(3)
        .progressPeriod(3)
        .recruitmentDeadline(LocalDate.of(2024, 6, 30))
        .contract("opentalk@kakao.net")
        .subject("チームプロジェクトを一緒にする方を募集します")
        .content("面白いチームプロジェクト")
        .views(0L)
        .build();

    var javaTag = TechStackTags.of("#Java");
    var springTag = TechStackTags.of("#Spring");

    var javaRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(javaTag)
        .build();

    var springRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(springTag)
        .build();

    recruitments1.relateToRecruitmentsTechStack(javaRecruitments);
    recruitments1.relateToRecruitmentsTechStack(springRecruitments);

    // 모집 포지션
    var backendEngineer = PositionTags.of("#Backend");
    var infraEngineer = PositionTags.of("#Infra");

    var backendRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(backendEngineer)
        .build();

    var infraRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(infraEngineer)
        .build();

    recruitments1.relateRecruitmentsRecruitmentPositionTags(backendRecruitments);
    recruitments1.relateRecruitmentsRecruitmentPositionTags(infraRecruitments);
    Recruitments savedRecruitments = recruitmentsRepository.save(recruitments1);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.get("/recruitments/{recruitmentId}",
                savedRecruitments.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcRestDocumentation.document("get-recruitments-by-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("recruitmentId").description("모집글 ID")
            ),
            PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("data.recruitmentCategories").type(STRING)
                    .description("모집 카테고리"),
                PayloadDocumentation.fieldWithPath("data.progressMethods").type(STRING)
                    .description("진행방법"),
                PayloadDocumentation.fieldWithPath("data.numberOfPeople").type(NUMBER)
                    .description("모집인원"),
                PayloadDocumentation.fieldWithPath("data.progressPeriod").type(NUMBER)
                    .description("진행기간"),
                PayloadDocumentation.fieldWithPath("data.recruitmentDeadline").type(STRING)
                    .description("마감 날짜"),
                PayloadDocumentation.fieldWithPath("data.contract").type(STRING).description("연락처"),
                PayloadDocumentation.fieldWithPath("data.subject").type(STRING).description("제목"),
                PayloadDocumentation.fieldWithPath("data.content").type(STRING).description("내용"),
                PayloadDocumentation.fieldWithPath("data.views").type(NUMBER).description("조회수"),
                PayloadDocumentation.fieldWithPath("data.techStacks").type(ARRAY)
                    .description("기술스택"),
                PayloadDocumentation.fieldWithPath("data.recruitmentPositions").type(ARRAY)
                    .description("모집 포지션"),
                PayloadDocumentation.fieldWithPath("error").type(NULL).description("에러")
            )
        )).andDo(print());
  }

  @Test
  @DisplayName("모집 응모글을 삭제할 수 있다")
  void deleteRecruitmentsById() throws Exception {
    // 모집 기술 스택
    Recruitments recruitments1 = Recruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ALL)
        .numberOfPeople(3)
        .progressPeriod(3)
        .recruitmentDeadline(LocalDate.of(2024, 6, 30))
        .contract("opentalk@kakao.net")
        .subject("チームプロジェクトを一緒にする方を募集します")
        .content("面白いチームプロジェクト")
        .build();

    TechStackTags springTag = TechStackTags.of("#Spring");
    TechStackTags javaTag = TechStackTags.of("#Java");

    RecruitmentsTechStack javaRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(javaTag)
        .build();

    RecruitmentsTechStack springRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(springTag)
        .build();

    recruitments1.relateToRecruitmentsTechStack(javaRecruitments);
    recruitments1.relateToRecruitmentsTechStack(springRecruitments);

    // 모집 포지션
    var backendEngineer = PositionTags.of("#Backend");
    var infraEngineer = PositionTags.of("#Infra");

    var backendRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(backendEngineer)
        .build();

    var infraRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(infraEngineer)
        .build();

    recruitments1.relateRecruitmentsRecruitmentPositionTags(backendRecruitments);
    recruitments1.relateRecruitmentsRecruitmentPositionTags(infraRecruitments);
    var savedRecruitment = recruitmentsRepository.save(recruitments1);

    // when
    mockMvc
        .perform(
            RestDocumentationRequestBuilders.delete("/recruitments/{recruitmentId}",
                savedRecruitment.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcRestDocumentation.document("delete-recruitment-by-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("recruitmentId").description("모집글 ID")
            )))
        .andDo(print());
  }

  @Test
  @DisplayName("모집 응모글을 갱신할 수 있다")
  void updateRecruitmentsById() throws Exception {
    // 모집 기술 스택
    Recruitments recruitments1 = Recruitments.builder()
        .recruitmentCategories(RecruitmentCategories.PROJECT)
        .progressMethods(ProgressMethods.ALL)
        .numberOfPeople(3)
        .progressPeriod(3)
        .recruitmentDeadline(LocalDate.of(2024, 6, 30))
        .contract("opentalk@kakao.net")
        .subject("チームプロジェクトを一緒にする方を募集します")
        .content("面白いチームプロジェクト")
        .build();

    TechStackTags springTag = TechStackTags.of("#Spring");
    TechStackTags javaTag = TechStackTags.of("#Java");

    RecruitmentsTechStack javaRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(javaTag)
        .build();

    RecruitmentsTechStack springRecruitments = RecruitmentsTechStack.builder()
        .recruitments(recruitments1)
        .techStackTags(springTag)
        .build();

    recruitments1.relateToRecruitmentsTechStack(javaRecruitments);
    recruitments1.relateToRecruitmentsTechStack(springRecruitments);

    // 모집 포지션
    var backendEngineer = PositionTags.of("#Backend");
    var infraEngineer = PositionTags.of("#Infra");

    var backendRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(backendEngineer)
        .build();

    var infraRecruitments = RecruitmentsPositionTags.builder()
        .recruitments(recruitments1)
        .positionTags(infraEngineer)
        .build();

    recruitments1.relateRecruitmentsRecruitmentPositionTags(backendRecruitments);
    recruitments1.relateRecruitmentsRecruitmentPositionTags(infraRecruitments);
    var savedRecruitment = recruitmentsRepository.save(recruitments1);

    // 업데이트 객체
    UpdateRecruitmentsRequest request =
        UpdateRecruitmentsRequest.builder()
            .recruitmentCategories(RecruitmentCategories.PROJECT)
            .progressMethods(ProgressMethods.ALL)
            .techStacks("#Spring")
            .recruitmentPositions("#Frontend#Backend#Infra#Cloud")
            .numberOfPeople(5)
            .progressPeriod(3)
            .recruitmentDeadline(LocalDate.of(2024, 7, 30))
            .contract("opentalk@kakao.net")
            .subject("チームプロジェクトを一緒にする方を募集します")
            .content("面白いチームプロジェクト")
            .build();

    String jsonString = objectMapper.writeValueAsString(request);

    // when
    mockMvc
        .perform(
            RestDocumentationRequestBuilders.delete("/recruitments/{recruitmentId}",
                    savedRecruitment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonString))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcRestDocumentation.document("update-recruitment-by-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            pathParameters(
                parameterWithName("recruitmentId").description("모집글 ID")
            ),
            PayloadDocumentation.requestFields(
                PayloadDocumentation.fieldWithPath("recruitmentCategories").type(STRING)
                    .description("모집 카테고리"),
                PayloadDocumentation.fieldWithPath("progressMethods").type(STRING)
                    .description("진행방법"),
                PayloadDocumentation.fieldWithPath("techStacks").type(STRING).description("기술 스택"),
                PayloadDocumentation.fieldWithPath("recruitmentPositions").type(STRING)
                    .description("모집 포지션"),
                PayloadDocumentation.fieldWithPath("numberOfPeople").type(NUMBER)
                    .description("모집 인원"),
                PayloadDocumentation.fieldWithPath("progressPeriod").type(NUMBER)
                    .description("모집 기간"),
                PayloadDocumentation.fieldWithPath("recruitmentDeadline").type(STRING)
                    .description("마감 날짜"),
                PayloadDocumentation.fieldWithPath("contract").type(STRING).description("연락처"),
                PayloadDocumentation.fieldWithPath("subject").type(STRING).description("제목"),
                PayloadDocumentation.fieldWithPath("content").type(STRING).description("내용")
            )));
  }

}
