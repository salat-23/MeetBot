package com.salat23.bot.models;

import com.salat23.bot.botapi.notifications.NotificationManager;
import com.salat23.bot.botapi.notifications.NotificationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    private String text;

    public Notification() {}

    public Notification(User recipient, NotificationType type, String text) {
        this.setRecipient(recipient);
        this.setType(type);
        this.setText(text);
        this.setCreationTime(Timestamp.from(Instant.now()));
    }
}
