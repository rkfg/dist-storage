package ru.rkfg.tests.diststorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class PlainContentReader implements RaindropContentReader {

    InputStream stream;
    int actualRead;
    int tailSize;
    byte[] plainReadBuffer = new byte[RainDrop.RAINSIZE];

    public PlainContentReader(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public byte[] readBlock() throws IOException, NoMoreRaindrops {
        Arrays.fill(plainReadBuffer, (byte) 0);
        actualRead = stream.read(plainReadBuffer);
        if (actualRead < 1) {
            stream.close();
            throw new NoMoreRaindrops();
        }
        if (actualRead < RainDrop.RAINSIZE) {
            tailSize = actualRead;
        }
        return Arrays.copyOf(plainReadBuffer, actualRead);
    }

    @Override
    public void closeStream() throws IOException {
        stream.close();
    }

}
