package com.hot6.pnureminder.entity;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table( name ="likes")
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember; // 좋아요를 받는 멤버

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember; // 좋아요를 주는 멤버

}