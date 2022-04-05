package com.salat23.bot.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "matches")
public class Match {

    @EmbeddedId
    private MatchComposite key;

    @Column(name = "is_ignored")
    private Boolean isIgnored;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    public Match() {}

    public Match(User from, User to) {
        this.key = new MatchComposite(from, to);
        this.isIgnored = false;
        this.creationDate = LocalDate.now();
    }

}

