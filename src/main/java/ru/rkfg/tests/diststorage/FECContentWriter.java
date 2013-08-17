package ru.rkfg.tests.diststorage;

import java.io.FileOutputStream;
import java.io.IOException;

public class FECContentWriter implements RaindropContentWriter {
    private FEC fec;

    public FECContentWriter(FileOutputStream stream, long fileLength) {
        fec = new FEC(stream, fileLength);
    }

    @Override
    public boolean writeBlock(byte[] block, int dataIndex) throws IOException, ExtractionError {
        if (dataIndex == FEC.n) {
            // not enough segments received
            throw new ExtractionError("Not enough segments received");
        }
        return fec.writeBlockToStream(block, dataIndex);
    }

    @Override
    public void closeStream() throws IOException {
        fec.closeStream();
    }

}
