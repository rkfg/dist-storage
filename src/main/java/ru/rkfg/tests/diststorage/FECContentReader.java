package ru.rkfg.tests.diststorage;

import java.io.IOException;
import java.io.InputStream;

public class FECContentReader implements RaindropContentReader {
    FEC fec;

    public FECContentReader(InputStream stream) {
        fec = new FEC(stream);
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
