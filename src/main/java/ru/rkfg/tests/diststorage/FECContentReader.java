package ru.rkfg.tests.diststorage;

import java.io.IOException;
import java.io.InputStream;

public class FECContentReader implements RaindropContentReader {
    FEC fec;

    public FECContentReader(InputStream stream, long fileLength, int k, int n) {
        fec = new FEC(stream, fileLength, k, n);
    }

    @Override
    public byte[] readBlock() throws IOException, NoMoreRaindrops {
        return fec.readBlockFromStream();
    }

    @Override
    public void closeStream() throws IOException {
        fec.closeStream();
    }

}
