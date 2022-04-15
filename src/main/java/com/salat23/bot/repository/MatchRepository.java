package com.salat23.bot.repository;

import com.salat23.bot.models.Match;
import com.salat23.bot.models.User;
import com.salat23.bot.models.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> getAllByKey_From(User key_from);
    Boolean existsByKey_ToAndKey_From(User key_to, User key_from);
    Match getByKey_ToAndKey_From(User key_to, User key_from);

    @Query(nativeQuery = true, value = "select exists (select true from matches where from_id=:senderId and to_id=:receiverId)")
    Boolean isLikedBy(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);

    @Query(nativeQuery = true,
    value = "select * from matches m where to_id = :userId and is_ignored = 'f' limit 1")
    Match getFirstStackMatch(@Param("userId") Long userId);



}