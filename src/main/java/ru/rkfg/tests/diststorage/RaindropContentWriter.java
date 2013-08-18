package ru.rkfg.tests.diststorage;

import java.io.IOException;

public interface RaindropContentWriter {
    /**
     * Writes block to the stream or internal storage
     * 
     * @param block
     * @param dataIndex
     * @return n if you should skip next (n-x) blocks where x is number of already written blocks in the segment of n blocks, 0 if you
     *         should just proceed
     * @throws IOException
     * @throws ExtractionError
     */
    public boolean writeBlock(byte[] block, int dataIndex) throws IOException, ExtractionError;

    public void closeStream() throws IOException;
}
