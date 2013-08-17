package ru.rkfg.tests.diststorage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.onionnetworks.fec.FECCode;
import com.onionnetworks.fec.FECCodeFactory;
import com.onionnetworks.util.Buffer;

public class FEC {

    private FECCode fecCode;
    public static final int k = 100;
    public static final int n = 120;
    private byte[][] sourceData = new byte[k][RainDrop.RAINSIZE];
    private byte[][] targetData = new byte[n][RainDrop.RAINSIZE];
    private Buffer[] sourceBuffer = new Buffer[k];
    private Buffer[] targetBuffer = new Buffer[n];
    private int[] indexes = new int[n];
    private int index = 0;
    private InputStream inputStream;
    private OutputStream outputStream;
    int actualRead;
    private long fileLength;
    private int len = RainDrop.RAINSIZE;

    protected FEC() {
        fecCode = FECCodeFactory.getDefault().createFECCode(k, n);
    }

    public FEC(InputStream stream) {
        this();
        this.inputStream = stream;
        for (int i = 0; i < sourceBuffer.length; i++) {
            sourceBuffer[i] = new Buffer(sourceData[i]);
        }
        for (int i = 0; i < targetBuffer.length; i++) {
            targetBuffer[i] = new Buffer(targetData[i]);
            indexes[i] = i;
        }
    }

    public FEC(FileOutputStream stream, long fileLength) {
        this();
        outputStream = stream;
        this.fileLength = fileLength;
        for (int i = 0; i < targetBuffer.length; i++) {
            targetBuffer[i] = new Buffer(targetData[i]);
        }
    }

    public byte[] readBlockFromStream() throws IOException, NoMoreRaindrops {
        if (index >= targetData.length) {
            index = 0;
        }
        if (index == 0) {
            int i;
            for (i = 0; i < sourceBuffer.length; i++) {
                Arrays.fill(sourceData[i], (byte) 0);
                actualRead = inputStream.read(sourceData[i]);
                if (actualRead < 1) {
                    if (i == 0) {
                        inputStream.close();
                        throw new NoMoreRaindrops();
                    } else {
                        Arrays.fill(sourceData[i], (byte) 0);
                    }
                }
            }
            fecCode.encode(sourceBuffer, targetBuffer, indexes);
        }
        return Arrays.copyOf(targetData[index++], RainDrop.RAINSIZE);
    }

    public boolean writeBlockToStream(byte[] data, int dataIndex) throws IOException {
        if (index == 0) {
            Arrays.fill(indexes, 0);
        }
        System.arraycopy(data, 0, targetData[index], 0, targetData[index].length);
        indexes[index] = dataIndex;
        if (++index == k) {
            fecCode.decode(targetBuffer, indexes);
            for (int i = 0; i < k; i++) {
                if (fileLength > 0) {
                    if (fileLength < targetData[i].length) {
                        len = (int) fileLength;
                    }
                    outputStream.write(targetData[i], 0, len);
                    fileLength -= len;
                }
            }
            index = 0;
            return true;
        }
        return false;
    }

    public void closeStream() throws IOException {
        outputStream.close();
    }

}
