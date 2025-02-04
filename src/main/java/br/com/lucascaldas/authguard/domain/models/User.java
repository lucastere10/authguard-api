package br.com.lucascaldas.authguard.domain.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

import br.com.lucascaldas.authguard.domain.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "USER_DATA")
public class User  {

    @Id
    @Column(name = "USER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "USER_NAME", nullable = false)
    private String name;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String password;

    @Column(name = "USER_EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @Column(name = "USER_REGISTER_DATA", nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp registerDate;

    @Column(name = "USER_UPDATE_DATA", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updateDate;

}
