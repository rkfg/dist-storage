package ru.rkfg.tests.diststorage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;

import ru.ppsrk.gwt.client.ClientAuthenticationException;
import ru.ppsrk.gwt.client.LogicException;
import ru.ppsrk.gwt.server.HibernateCallback;
import ru.ppsrk.gwt.server.HibernateUtil;
import ru.rkfg.tests.diststorage.domain.RainFileDB;

public class RainFile {
    final String filename;
    final List<RainHash> hashList;
    long fileLength;
    final boolean fec;
    private int k;
    private int n;
    RainHash selfHash = null;

    public RainFile(final String filename, boolean fec, List<RainHash> hashList, final long fileLength, final int k, final int n) {
        super();
        this.filename = filename;
        this.hashList = hashList;
        this.fec = fec;
        this.fileLength = fileLength;
        this.k = k;
        this.n = n;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            digest.update(filename.getBytes("utf-8"));
            final ByteArrayOutputStream hashConcat = new ByteArrayOutputStream(hashList.size() * 32);
            for (RainHash hash : hashList) {
                hashConcat.write(hash.getBytes());
                digest.update(hash.getBytes());
            }
            selfHash = new RainHash(digest.digest());
            HibernateUtil.exec(new HibernateCallback<Void>() {

                @Override
                public Void run(Session session) throws LogicException, ClientAuthenticationException {
                    try {
                        RainFileDB rainFileDB = (RainFileDB) session.createQuery("from RainFileDB where selfHash = :sh")
                                .setBinary("sh", selfHash.getBytes()).uniqueResult();
                        if (rainFileDB != null) {
                            session.delete(rainFileDB);
                        }
                        rainFileDB = new RainFileDB(filename, RainFile.this.fec, hashConcat.toByteArray(), selfHash.getBytes(), fileLength,
                                k, n);
                        session.merge(rainFileDB);
                        return null;
                    } catch (NonUniqueResultException e) {
                        throw new LogicException("Non-unique selfHash = " + selfHash.getBase64());
                    }
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (LogicException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientAuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public RainFile(RainFileDB rainFileDB) throws IOException {
        filename = rainFileDB.getName();
        selfHash = new RainHash(rainFileDB.getSelfHash());
        hashList = new LinkedList<RainHash>();
        fileLength = rainFileDB.getFileLength();
        k = rainFileDB.getK();
        n = rainFileDB.getN();
        fec = rainFileDB.getFec();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(rainFileDB.getHashes());
        while (byteArrayInputStream.available() > 0) {
            byte[] hash = new byte[32];
            byteArrayInputStream.read(hash);
            RainHash newRainHash = new RainHash(hash);
            System.out.println("Read hash: " + newRainHash.getBase64());
            hashList.add(newRainHash);
        }
    }

    public String getFilename() {
        return filename;
    }

    public List<RainHash> getHashList() {
        return hashList;
    }

    public RainHash getSelfHash() {
        return selfHash;
    }

    @Override
    public String toString() {
        return "Filename: " + filename + "; Hash: " + selfHash;
    }

    public Boolean getFec() {
        return fec;
    }

    public long getFileLength() {
        return fileLength;
    }

    public int getK() {
        return k;
    }

    public int getN() {
        return n;
    }
}
