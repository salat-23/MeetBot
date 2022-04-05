package com.salat23.bot.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class ViewComposite implements Serializable {

    @ManyToOne
    private User from;

    @ManyToOne
    private User to;

    public ViewComposite() {}

    public ViewComposite(User from, User to) {
        this.setFrom(from);
        this.setTo(to);
    }

}
