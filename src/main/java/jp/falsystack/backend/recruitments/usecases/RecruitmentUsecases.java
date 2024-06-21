package jp.falsystack.backend.recruitments.usecases;

import jp.falsystack.backend.recruitments.repositories.RecruitmentRepositories;
import jp.falsystack.backend.recruitments.usecases.in.PostRecruitments;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentUsecases {

    private final RecruitmentRepositories recruitmentRepositories;

    public void post(PostRecruitments postRecruitment) {
        recruitmentRepositories.save(postRecruitment.toRecruitment());
    }
}
