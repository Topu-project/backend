package jp.falsystack.backend.recruitments.entities.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProgressMethods {
    ALL("全体"),
    ONLINE("オンライン"),
    OFFLINE("オフライン");

    private final String name;
}
