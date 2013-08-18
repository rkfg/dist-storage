package ru.rkfg.tests.diststorage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class PlainContentReader implements RaindropContentReader {

    InputStream stream;
    int actualRead;
    byte[] plainReadBuffer;

    public PlainContentReader(InputStream stream, long fileLength) {
        this.stream = stream;
        plainReadBuffer = new byte[RainDrop.getBestDropSize(fileLength)];
    }

    @Override
    public byte[] readBlock() throws IOException, NoMoreRaindrops {
        Arrays.fill(plainReadBuffer, (byte) 0);
        actualRead = stream.read(plainReadBuffer);
        if (actualRead < 1) {
            stream.close();
            throw new NoMoreRaindrops();
        }
        return Arrays.copyOf(plainReadBuffer, actualRead);
    }

    @Override
    public void closeStream() throws IOException {
        stream.close();
    }

}
