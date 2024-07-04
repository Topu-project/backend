package jp.falsystack.backend.recruitments.usecases;

import jp.falsystack.backend.recruitments.entities.*;
import jp.falsystack.backend.recruitments.repositories.RecruitmentPositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentRepositories;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForTopPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RecruitmentUsecases {

    private final RecruitmentRepositories recruitmentRepositories;
    private final TechStackTagsRepository techStackTagsRepository;
    private final RecruitmentPositionTagsRepository recruitmentPositionTagsRepository;

    public void post(PostRecruitments postRecruitment) {

        Pattern pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯ㄱ-ㅎ]*)");
        Matcher matcher = pattern.matcher(postRecruitment.getTechStacks());
        Matcher positionMatcher = pattern.matcher(postRecruitment.getRecruitmentPositions());

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

        while (positionMatcher.find()) {
            RecruitmentPositionTags recruitmentPositionTags = recruitmentPositionTagsRepository.findByRecruitmentPositionTagName(positionMatcher.group())
                    .orElse(RecruitmentPositionTags.of(positionMatcher.group()));

            RecruitmentsRecruitmentPositionTags recruitmentsRecruitmentPositionTags = RecruitmentsRecruitmentPositionTags.builder()
                    .recruitments(recruitments)
                    .recruitmentPositionTags(recruitmentPositionTags)
                    .build();

            recruitmentPositionTags.relateToRecruitmentsRecruitmentPositionTags(recruitmentsRecruitmentPositionTags);
            recruitments.relateRecruitmentsRecruitmentPositionTags(recruitmentsRecruitmentPositionTags);
        }

        recruitmentRepositories.save(recruitments);
    }

    @Transactional
    public List<RecruitmentsResponseForTopPage> getRecruitments() {
        return recruitmentRepositories.findAll().stream().map(recruitments ->
                        RecruitmentsResponseForTopPage.builder()
                                .recruitmentCategories(recruitments.getRecruitmentCategories())
                                .techStacks(recruitments.getRecruitmentsTechStacks().stream()
                                        .map(recruitmentsTechStack -> {
                                            System.out.println("recruitmentsTechStack = " + recruitmentsTechStack);
                                            return recruitmentsTechStack.getTechStackTags().getTechStackTagName();
                                        }).toList())
                                .recruitmentDeadline(recruitments.getRecruitmentDeadline())
                                .subject(recruitments.getSubject())
                                .build())
                .toList();
    }
}
