package ru.rkfg.tests.diststorage.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
@Table(indexes = @Index(name = "rainfiles_selfhash", columnNames = "selfHash"), appliesTo = "RainFileDB")
public class RainFileDB {
    @Id
    @GeneratedValue
    Long id;
    String name;
    Boolean fec;
    @Column(columnDefinition = "BLOB")
    byte[] hashes;
    byte[] selfHash;
    long fileLength;
    int k;
    int n;

    public RainFileDB() {
    }

    public RainFileDB(String name, Boolean fec, byte[] hashes, byte[] selfHash, long fileLength, int k, int n) {
        super();
        this.name = name;
        this.hashes = hashes;
        this.selfHash = selfHash;
        this.fec = fec;
        this.fileLength = fileLength;
        this.k = k;
        this.n = n;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getFec() {
        return fec;
    }

    public void setFec(Boolean fec) {
        this.fec = fec;
    }

    public byte[] getHashes() {
        return hashes;
    }

    public void setHashes(byte[] hashes) {
        this.hashes = hashes;
    }

    public byte[] getSelfHash() {
        return selfHash;
    }

    public void setSelfHash(byte[] selfHash) {
        this.selfHash = selfHash;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

}
