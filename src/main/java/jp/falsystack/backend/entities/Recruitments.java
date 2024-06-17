package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

}