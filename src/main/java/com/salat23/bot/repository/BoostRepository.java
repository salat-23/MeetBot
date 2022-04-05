package com.salat23.bot.repository;

import com.salat23.bot.models.Boost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoostRepository extends JpaRepository<Boost, Long> {



}
