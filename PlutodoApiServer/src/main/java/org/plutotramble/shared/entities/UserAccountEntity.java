package org.plutotramble.shared.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user_account", schema = "public", catalog = "javadevplutodo")
public class UserAccountEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Basic
    @Column(name = "username", nullable = false, length = 30)
    private String username;
    @Basic
    @Column(name = "email_address", nullable = false, length = 50)
    private String emailAddress;
    @Basic
    @Column(name = "password", nullable = false, length = -1)
    private String password;
    @Basic
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
    @Basic
    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;
    @Basic
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;
    @Basic
    @Column(name = "date_last_login", nullable = true)
    private Timestamp dateLastLogin;
    @OneToMany(mappedBy = "userAccount")
    private Collection<CategoryEntity> categories;

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

    public Collection<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(Collection<CategoryEntity> categories) {
        this.categories = categories;
    }
}
