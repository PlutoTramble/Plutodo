package org.plutotramble.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "category", schema = "public", catalog = "javadevplutodo")
public class CategoryEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Basic
    @Column(name = "name", nullable = false, length = 30)
    private String name;
    @Basic
    @Column(name = "color", nullable = true, length = 7)
    private String color;
    @Basic
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;
    @OneToMany(mappedBy = "category")
    private Collection<CalendarEventEntity> calendarEvents;
    @ManyToOne
    @JoinColumn(name = "user_account_id", referencedColumnName = "id", nullable = false)
    private UserAccountEntity userAccount;
    @OneToMany(mappedBy = "category")
    private Collection<TaskItemEntity> taskItems;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryEntity that = (CategoryEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(dateCreated, that.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, dateCreated);
    }

    public Collection<CalendarEventEntity> getCalendarEvents() {
        return calendarEvents;
    }

    public void setCalendarEvents(Collection<CalendarEventEntity> calendarEvents) {
        this.calendarEvents = calendarEvents;
    }

    public UserAccountEntity getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccountEntity userAccount) {
        this.userAccount = userAccount;
    }

    public Collection<TaskItemEntity> getTaskItems() {
        return taskItems;
    }

    public void setTaskItems(Collection<TaskItemEntity> taskItems) {
        this.taskItems = taskItems;
    }
}
