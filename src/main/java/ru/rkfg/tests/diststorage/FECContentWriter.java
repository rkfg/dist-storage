package ru.rkfg.tests.diststorage;

import java.io.FileOutputStream;
import java.io.IOException;

public class FECContentWriter implements RaindropContentWriter {
    private FEC fec;
    private int n;

    public FECContentWriter(FileOutputStream stream, RainFile rainFile) {
        this.n = rainFile.getN();
        fec = new FEC(stream, rainFile);
    }

    @Override
    public boolean writeBlock(byte[] block, int dataIndex) throws IOException, ExtractionError {
        if (dataIndex == n) {
            throw new ExtractionError("Not enough segments received");
        }
        return fec.writeBlockToStream(block, dataIndex);
    }

    @Override
    public void closeStream() throws IOException {
        fec.closeStream();
    }

}
