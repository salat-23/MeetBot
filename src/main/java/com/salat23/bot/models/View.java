package com.salat23.bot.models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "views")
public class View {

    @EmbeddedId
    private ViewComposite key;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    public View() {}

    public View(User from, User to) {
        this.key = new ViewComposite(from, to);
        this.creationDate = LocalDate.now();
    }

}

