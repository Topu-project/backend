package jp.falsystack.backend.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.backend.recruitments.entities.enums.ProgressMethods;
import jp.falsystack.backend.recruitments.entities.enums.RecruitmentCategories;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.management.openmbean.SimpleType;
import java.time.LocalDate;
import java.time.Period;

import static javax.management.openmbean.SimpleType.LONG;
import static javax.management.openmbean.SimpleType.STRING;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class RecruitmentsControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("모집 응모글을 작성할 수 있다")
    void postRecruitments() throws Exception {
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
                .andDo(MockMvcRestDocumentation.document("post-recruitments",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("recruitmentCategories").type(STRING).description("모집 카테고리"),
                                PayloadDocumentation.fieldWithPath("progressMethods").type(STRING).description("진행방법"),
                                PayloadDocumentation.fieldWithPath("techStacks").type(STRING).description("기술 스택"),
                                PayloadDocumentation.fieldWithPath("numberOfPeople").type(LONG).description("모집 인원"),
                                PayloadDocumentation.fieldWithPath("progressPeriod").type("Period").description("모집 기간"),
                                PayloadDocumentation.fieldWithPath("recruitmentDeadline").type(SimpleType.DATE).description("마감 날짜"),
                                PayloadDocumentation.fieldWithPath("contract").type(STRING).description("연락처"),
                                PayloadDocumentation.fieldWithPath("subject").type(STRING).description("제목"),
                                PayloadDocumentation.fieldWithPath("content").type(STRING).description("내용")
                        )));
    }

}
