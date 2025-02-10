package org.plutotramble.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "task_item", schema = "public", catalog = "javadevplutodo")
public class TaskItemEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 8192)
    private String description;
    @Basic
    @Column(name = "is_finished", nullable = true)
    private Boolean isFinished;
    @Basic
    @Column(name = "date_created", nullable = false)
    private Timestamp dateCreated;
    @Basic
    @Column(name = "date_due", nullable = true)
    private Timestamp dateDue;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private CategoryEntity category;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateDue() {
        return dateDue;
    }

    public void setDateDue(Timestamp dateDue) {
        this.dateDue = dateDue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskItemEntity that = (TaskItemEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(isFinished, that.isFinished) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateDue, that.dateDue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isFinished, dateCreated, dateDue);
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }
}
