package ru.rkfg.tests.diststorage;

import ru.rkfg.jsn.server.Utils;

public class RainHash {
    final private byte[] hash;

    public RainHash(byte[] hash) {
        super();
        this.hash = hash;
    }

    public RainHash(String hash) {
        super();
        this.hash = Utils.b64ToBytes(hash.replace('-', '/').replace('_', '='));
    }

    public byte[] getBytes() {
        return hash;
    }

    public String getBase64() {
        return Utils.bytesToB64(hash).replace('/', '-').replace('=', '_');
    }

    @Override
    public String toString() {
        return getBase64();
    }

}
