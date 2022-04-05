package com.salat23.bot.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reported_id")
    private Long reportedId;
    @Column(name = "reporter_id")
    private Long reporterId;

    private String reason;

    @Column(name = "creation_date")
    private LocalDate creationDate;
}
