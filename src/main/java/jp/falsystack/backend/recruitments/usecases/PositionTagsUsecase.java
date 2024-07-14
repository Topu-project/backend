package jp.falsystack.backend.recruitments.usecases;

import java.util.List;
import jp.falsystack.backend.recruitments.repositories.PositionTagsRepository;
import jp.falsystack.backend.recruitments.usecases.out.PositionTagResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionTagsUsecase {

  private final PositionTagsRepository repository;

  public List<PositionTagResponse> getPositionTagsResponse() {
    return repository
        .findAll()
        .stream()
        .map(positionTags ->
            PositionTagResponse.from(positionTags.getId(), positionTags.getPositionTagName()))
        .toList();
  }

}
