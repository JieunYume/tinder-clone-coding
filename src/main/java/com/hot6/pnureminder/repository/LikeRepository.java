package com.hot6.pnureminder.repository;

import com.hot6.pnureminder.entity.Like;
import com.hot6.pnureminder.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<List<Like>> findByToMemberId(Long toMemberId);
    Optional<List<Like>> findByFromMember(Member fromMember);
}
