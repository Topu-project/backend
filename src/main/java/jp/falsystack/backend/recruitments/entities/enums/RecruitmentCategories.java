package jp.falsystack.backend.recruitments.entities.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecruitmentCategories {
    PROJECT("プロジェクト"),
    STUDY("勉強会");

    private final String name;
}
