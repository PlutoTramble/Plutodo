package org.plutotramble.Entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "deleted_items", schema = "public", catalog = "PlutodoDBDevJava")
public class DeletedItemsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private UUID id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "table_name")
    private String tableName;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "date_created")
    private Timestamp dateCreated;
    @Basic
    @Column(name = "date_deleted")
    private Timestamp dateDeleted;

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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(Timestamp dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeletedItemsEntity that = (DeletedItemsEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(tableName, that.tableName) && Objects.equals(content, that.content) && Objects.equals(dateCreated, that.dateCreated) && Objects.equals(dateDeleted, that.dateDeleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tableName, content, dateCreated, dateDeleted);
    }
}
