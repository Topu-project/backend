package jp.falsystack.backend.docs;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.falsystack.backend.recruitments.entities.PositionTags;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class PositionTagsControllerDocTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PositionTagsRepository repository;

  @BeforeEach
  void beforeEach() {
    repository.deleteAll();
  }

  @Test
  @DisplayName("모집 포지션의 목록을 확인할 수 있다.")
  void getPositionTagsName() throws Exception {
    // given
    var frontend = PositionTags.of("#Frontend");
    var backend = PositionTags.of("#Backend");
    var fullstack = PositionTags.of("#Fullstack");

    repository.saveAll(List.of(frontend, backend, fullstack));

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.get("/positions")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcRestDocumentation.document("get-position-tag-names",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            PayloadDocumentation.responseFields(
                PayloadDocumentation.fieldWithPath("data.[].id").type(JsonFieldType.NUMBER)
                    .description("포지션 ID"),
                PayloadDocumentation.fieldWithPath("data.[].positionTagName")
                    .type(JsonFieldType.STRING).description("포지션 이름"),
                PayloadDocumentation.fieldWithPath("error").type(JsonFieldType.NULL)
                    .description("에러")
            )))
        .andDo(print());

  }
}
