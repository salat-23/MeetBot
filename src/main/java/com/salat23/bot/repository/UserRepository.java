package com.salat23.bot.repository;

import com.salat23.bot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
                    " and target.id not in (select m.from_id from matches m where m.to_id = searcher.id and m.is_ignored = true)" +
                    " order by random() * (1 + target_boost.popularity_boost)" +
                    "limit 1")
    User findForm(@Param("userId") Long chatId);

    @Query(nativeQuery = true, value = "select count(*) from users")
    Integer getTotalUsersCount();
    @Query(nativeQuery = true, value = "select count(*) from users where state<>'RESTORED_USER'")
    Integer getTotalActiveUsersCount();
    @Query(nativeQuery = true, value = "select count(*) from users where gender='MAN'")
    Integer getMaleUsersCount();
    @Query(nativeQuery = true, value = "select count(*) from users where gender='WOMAN'")
    Integer getFemaleUsersCount();

    @Query(nativeQuery = true, value = "" +
            "select count(*) from users where registration_date >= :date and gender = 'MAN'")
    Integer getRegisteredMaleAfter(@Param("date") LocalDate date);

    @Query(nativeQuery = true, value = "" +
            "select count(*) from users where registration_date >= :date and gender = 'WOMAN'")
    Integer getRegisteredFemaleAfter(@Param("date") LocalDate date);

    @Query(nativeQuery = true, value = "select registration_date from users where registration_date is not null")
    List<String> getRegistrationDates();

    @Query(nativeQuery = true, value =
            "select *\n" +
                    "from users target\n" +
                    "    join matches m on target.id = m.from_id\n" +
                    "    join users searcher on searcher.id = :userId\n" +
                    "where m.to_id = searcher.id\n" +
                    "and m.is_ignored = false\n" +
                    "order by m.creation_date\n" +
                    "limit 1")
    User findEarliestLiker(@Param("userId") Long chatId);


}