package ru.rkfg.tests.diststorage;

import ru.rkfg.jsn.server.CryptoUtils;

public class RainDrop {
    public static int RAINSIZE = 32 * 1024;
    private byte[] content;
    private RainHash hash = null;

    public RainDrop(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public RainHash getHash() {
        if (hash == null) {
            hash = new RainHash(CryptoUtils.getHash(content));
        }
        return hash;
    }

}
