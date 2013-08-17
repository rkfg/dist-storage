package ru.rkfg.tests.diststorage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
@Table(indexes = @Index(name = "raindrops_hash", columnNames = "hash"), appliesTo = "RainDropDB")
public class RainDropDB {
    @Id
    @GeneratedValue
    Long id;
    @Column(columnDefinition = "BLOB")
    byte[] content;
    byte[] hash;

    public RainDropDB() {
    }

    public RainDropDB(byte[] content, byte[] hash) {
        super();
        this.content = content;
        this.hash = hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

}
