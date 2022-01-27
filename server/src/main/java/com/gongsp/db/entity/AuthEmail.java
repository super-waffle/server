package com.gongsp.db.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DynamicInsert
@Table(name="tb_auth_email")
public class AuthEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer authSeq;
    private String authEmail;
    private String authCode;
    private LocalDateTime authDate;
}
