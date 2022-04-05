package com.salat23.bot.repository;

import com.salat23.bot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value =
            "select *" +
                    " from users target" +
                    " join boosts target_boost on target.boost_id = target_boost.id" +
                    " join users searcher on searcher.id = :userId" +
                    " where target.is_searchable = true" +
                    " and target.id <> searcher.id" +
                    " and target.gender = searcher.target" +
                    " and target.age <= searcher.target_max_age" +
                    " and target.age >= searcher.target_min_age" +
                    " and searcher.age <= target.target_max_age" +
                    " and searcher.age >= target.target_min_age" +
                    " and target.id not in (select v.to_id from views v where from_id = searcher.id)" +
                    " order by random() * (1 + target_boost.popularity_boost)" +
                    "limit 1")
    User findForm(@Param("userId") Long chatId);

    @Query(nativeQuery = true, value =
            "select *\n" +
                    "from users target\n" +
                    "    join matches m on target.id = m.from_id\n" +
                    "    join users searcher on searcher.id = :userId\n" +
                    "where m.to_id = searcher.id\n" +
                    "and target.id not in (select v.to_id from views v where v.from_id = searcher.id)\n" +
                    "order by m.creation_date\n" +
                    "limit 1")
    User findEarliestLiker(@Param("userId") Long chatId);


}