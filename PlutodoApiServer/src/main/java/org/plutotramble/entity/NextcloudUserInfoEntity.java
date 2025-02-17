package org.plutotramble.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "nextcloud_user_info")
public class NextcloudUserInfoEntity {
    @Id
    @ColumnDefault("gen_random_uuid()")
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    private String password;

    @ColumnDefault("now()")
    @Column(name = "date_created", nullable = false)
    private Instant dateCreated;

    @Column(name = "date_last_login")
    private Instant dateLastLogin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccountEntity userAccount;

    @OneToMany(mappedBy = "nextcloudUser")
    private Set<UserAccountEntity> userAccounts = new LinkedHashSet<>();

    @Column(name = "server_url", nullable = false, length = Integer.MAX_VALUE)
    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateLastLogin() {
        return dateLastLogin;
    }

    public void setDateLastLogin(Instant dateLastLogin) {
        this.dateLastLogin = dateLastLogin;
    }

    public UserAccountEntity getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccountEntity userAccount) {
        this.userAccount = userAccount;
    }

    public Set<UserAccountEntity> getUserAccounts() {
        return userAccounts;
    }

    public void setUserAccounts(Set<UserAccountEntity> userAccounts) {
        this.userAccounts = userAccounts;
    }

}