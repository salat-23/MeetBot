package com.salat23.bot.repository;

import com.salat23.bot.models.User;
import com.salat23.bot.models.View;
import com.salat23.bot.models.ViewComposite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViewRepository extends JpaRepository<View, ViewComposite> {


    List<View> getAllByKey_From(User key_from);

}
