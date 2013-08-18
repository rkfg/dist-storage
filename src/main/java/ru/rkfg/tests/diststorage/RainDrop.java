package ru.rkfg.tests.diststorage;

import ru.rkfg.jsn.server.CryptoUtils;

public class RainDrop {
    private byte[] content;
    private RainHash hash = null;
    private static final int KiB = 1024;
    private static final int MiB = 1048576;
    private static final long GiB = 1073741824;

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

    // from transmission torrent client
    public static int getBestDropSize(long length) {
        if (length >= (2 * GiB))
            return 2 * MiB;
        if (length >= (1 * GiB))
            return 1 * MiB;
        if (length >= (512 * MiB))
            return 512 * KiB;
        if (length >= (350 * MiB))
            return 256 * KiB;
        if (length >= (150 * MiB))
            return 128 * KiB;
        if (length >= (50 * MiB))
            return 64 * KiB;
        return 32 * KiB; /* less than 50 meg */
    }

}
