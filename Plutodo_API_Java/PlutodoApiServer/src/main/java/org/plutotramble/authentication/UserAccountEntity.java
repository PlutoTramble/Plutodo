package org.plutotramble.authentication;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_account", schema = "public", catalog = "PlutodoDBDevJava")
public class UserAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "email_address")
    private String emailAddress;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "is_active")
    private boolean isActive;
    @Basic
    @Column(name = "is_verified")
    private boolean isVerified;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;
    @Basic
    @Column(name = "date_last_login")
    private Timestamp dateLastLogin;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateLastLogin() {
        return dateLastLogin;
    }

    public void setDateLastLogin(Timestamp dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccountEntity that = (UserAccountEntity) o;
        return isActive == that.isActive && isVerified == that.isVerified && Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(password, that.password) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateLastLogin, that.dateLastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, emailAddress, password, isActive, isVerified, dateCreated, dateLastLogin);
    }
}
