package jp.falsystack.backend.recruitments.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Getter
@NoArgsConstructor
public class Recruitments extends BaseEntity {
    @Id
    @Column(name = "recruitments_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RecruitmentCategories recruitmentCategories;

    @Enumerated(EnumType.STRING)
    private ProgressMethods progressMethods;

    private Long numberOfPeople;

    private Period progressPeriod;

    private LocalDate recruitmentDeadline;

    private String contract;

    private String subject;

    @Lob
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "views_id")
    private Views views;

    @Builder
    private Recruitments(RecruitmentCategories recruitmentCategories, ProgressMethods progressMethods, Long numberOfPeople, Period progressPeriod, LocalDate recruitmentDeadline, String contract, String subject, String content, Views views) {
        this.recruitmentCategories = recruitmentCategories;
        this.progressMethods = progressMethods;
        this.numberOfPeople = numberOfPeople;
        this.progressPeriod = progressPeriod;
        this.recruitmentDeadline = recruitmentDeadline;
        this.contract = contract;
        this.subject = subject;
        this.content = content;
        this.views = views;
    }
}