package com.hot6.pnureminder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table( name ="matches")
@Builder
public class Matching {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member1_id")
    private Member member1;

    @ManyToOne
    @JoinColumn(name="member2_id")
    private Member member2;
}
