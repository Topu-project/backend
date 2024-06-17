package jp.falsystack.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Views extends BaseEntity{
    @Id
    @Column(name = "views_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pageViews;
}
