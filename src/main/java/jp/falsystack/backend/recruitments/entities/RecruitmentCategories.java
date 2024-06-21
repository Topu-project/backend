package jp.falsystack.backend.recruitments.entities;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecruitmentCategories {
    PROJECT("プロジェクト"),
    STUDY("勉強会");

    private final String name;
}
