package ru.rkfg.tests.diststorage;

import java.io.IOException;

public interface RaindropContentReader {

    public byte[] readBlock() throws IOException, NoMoreRaindrops;

    public void closeStream() throws IOException;
}
