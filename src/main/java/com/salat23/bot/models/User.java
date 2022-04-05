package com.salat23.bot.models;

import com.salat23.bot.botapi.UserState;
import com.salat23.bot.botapi.notifications.NotificationManager;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    private Long id;

    private String name;

    private Integer age;

    private String bio;

    private String photo;

    @OneToOne
    private Location location;

    @OneToOne
    @JoinColumn(name = "last_suggestion")
    private User lastSuggestion;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Enumerated(EnumType.STRING)
    private GenderEnum target;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserState state;

    @OneToOne
    private Boost boost;

    @Column(name = "target_min_age")
    private Integer targetMinAge;

    @Column(name = "target_max_age")
    private Integer targetMaxAge;

    @Column(name = "is_searchable", columnDefinition = "")
    private Boolean isSearchable;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    public User() {
        this.state = UserState.NEW_USER;
    }

    public User(Long chatId) {
        this.id = chatId;
        this.state = UserState.NEW_USER;
    }
}
