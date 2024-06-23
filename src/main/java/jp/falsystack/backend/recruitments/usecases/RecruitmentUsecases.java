package jp.falsystack.backend.recruitments.usecases;

import jp.falsystack.backend.recruitments.entities.Recruitments;
import jp.falsystack.backend.recruitments.entities.RecruitmentsTechStack;
import jp.falsystack.backend.recruitments.entities.TechStackTags;
import jp.falsystack.backend.recruitments.repositories.RecruitmentRepositories;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RecruitmentUsecases {

    private final RecruitmentRepositories recruitmentRepositories;
    private final TechStackTagsRepository techStackTagsRepository;

    public void post(PostRecruitments postRecruitment) {

        Pattern pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯ㄱ-ㅎ]*)");
        Matcher matcher = pattern.matcher(postRecruitment.getTechStacks());

        Recruitments recruitments = postRecruitment.toRecruitment();

        while (matcher.find()) {
            TechStackTags techStackTags = techStackTagsRepository.findByTechStackTagName(matcher.group())
                    .orElse(TechStackTags.of(matcher.group()));

            RecruitmentsTechStack recruitmentsTechStack = RecruitmentsTechStack.builder()
                    .recruitments(recruitments)
                    .techStackTags(techStackTags)
                    .build();

            techStackTags.addRecruitmentsTechStack(recruitmentsTechStack);
            recruitments.addRecruitmentsTechStack(recruitmentsTechStack);
        }

        recruitmentRepositories.save(recruitments);
    }
}
