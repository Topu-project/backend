package jp.falsystack.backend.recruitments.usecases;

import jp.falsystack.backend.recruitments.entities.*;
import jp.falsystack.backend.recruitments.entities.exception.RecruitmentsNotFoundException;
import jp.falsystack.backend.recruitments.repositories.RecruitmentPositionTagsRepository;
import jp.falsystack.backend.recruitments.repositories.RecruitmentsRepository;
import jp.falsystack.backend.recruitments.repositories.TechStackTagsRepository;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForDetailPage;
import jp.falsystack.backend.recruitments.usecases.out.RecruitmentsResponseForIndexPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class RecruitmentUsecases {

    private final RecruitmentsRepository recruitmentRepositories;
    private final TechStackTagsRepository techStackTagsRepository;
    private final RecruitmentPositionTagsRepository recruitmentPositionTagsRepository;
    private final RecruitmentsRepository recruitmentsRepository;

    public void post(PostRecruitments postRecruitment) {
        Recruitments recruitments = postRecruitment.toRecruitment();

        Matcher techstackMatcher = getMatcher(postRecruitment.getTechStacks());
        establishRelationshipWithRecruitmentsTechStack(techstackMatcher, recruitments);

        Matcher positionMatcher = getMatcher(postRecruitment.getRecruitmentPositions());
        establishRelationshipWithRecruitmentsRecruitmentPositionTags(positionMatcher, recruitments);

        recruitmentRepositories.save(recruitments);
    }

    private void establishRelationshipWithRecruitmentsRecruitmentPositionTags(Matcher positionMatcher, Recruitments recruitments) {
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
    }

    private void establishRelationshipWithRecruitmentsTechStack(Matcher techstackMatcher, Recruitments recruitments) {
        while (techstackMatcher.find()) {
            TechStackTags techStackTags = techStackTagsRepository.findByTechStackTagName(techstackMatcher.group())
                    .orElse(TechStackTags.of(techstackMatcher.group()));

            RecruitmentsTechStack recruitmentsTechStack = RecruitmentsTechStack.builder()
                    .recruitments(recruitments)
                    .techStackTags(techStackTags)
                    .build();

            techStackTags.addRecruitmentsTechStack(recruitmentsTechStack);
            recruitments.relateToRecruitmentsTechStack(recruitmentsTechStack);
        }
    }

    private Matcher getMatcher(String src) {
        Pattern pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯ㄱ-ㅎ]*)");
        return pattern.matcher(src);
    }

    @Transactional
    public List<RecruitmentsResponseForIndexPage> getRecruitmentsForIndexPage() {
        return recruitmentRepositories.findAll().stream().map(recruitments ->
                        RecruitmentsResponseForIndexPage.builder()
                                .recruitmentCategories(recruitments.getRecruitmentCategories())
                                .techStacks(recruitments.getRecruitmentsTechStacks().stream()
                                        .map(recruitmentsTechStack ->
                                                recruitmentsTechStack.getTechStackTags().getTechStackTagName()).toList())
                                .recruitmentPositions(recruitments.getRecruitmentsRecruitmentPositionTags().stream()
                                        .map(recruitmentsRecruitmentPositionTags ->
                                                recruitmentsRecruitmentPositionTags.getRecruitmentPositionTags()
                                                        .getRecruitmentPositionTagName()).toList())
                                .recruitmentDeadline(recruitments.getRecruitmentDeadline())
                                .subject(recruitments.getSubject())
                                .build())
                .toList();
    }

    public RecruitmentsResponseForDetailPage getRecruitmentsById(Long recruitmentId) {
        // get recruitments
        var foundRecruitments = recruitmentRepositories.findById(recruitmentId)
                .orElseThrow(RecruitmentsNotFoundException::new);

        // add count to views
        foundRecruitments.increasePageViews();
        var updatedRecruitments = recruitmentsRepository.save(foundRecruitments);

        // return recruitments
        return RecruitmentsResponseForDetailPage.builder()
                .recruitmentCategories(updatedRecruitments.getRecruitmentCategories())
                .progressMethods(updatedRecruitments.getProgressMethods())
                .numberOfPeople(updatedRecruitments.getNumberOfPeople())
                .progressPeriod(updatedRecruitments.getProgressPeriod())
                .recruitmentDeadline(updatedRecruitments.getRecruitmentDeadline())
                .subject(updatedRecruitments.getSubject())
                .contract(updatedRecruitments.getContract())
                .content(updatedRecruitments.getContent())
                .views(updatedRecruitments.getViews())
                .techStacks(updatedRecruitments.getRecruitmentsTechStacks().stream()
                        .map(recruitmentsTechStack ->
                                recruitmentsTechStack.getTechStackTags().getTechStackTagName()).toList())
                .recruitmentPositions(updatedRecruitments.getRecruitmentsRecruitmentPositionTags().stream()
                        .map(recruitmentsRecruitmentPositionTags ->
                                recruitmentsRecruitmentPositionTags.getRecruitmentPositionTags()
                                        .getRecruitmentPositionTagName()).toList())
                .build();
    }
}
