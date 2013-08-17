package ru.rkfg.tests.diststorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.ppsrk.gwt.client.LogicException;
import ru.ppsrk.gwt.server.ServerUtils;

public class FileStorage implements Storage {

    private static String storagePath = "storage";

    @Override
    public void storeRaindrop(RainDrop raindrop) {
        String path;
        byte[] hash = raindrop.getHash().getBytes();
        path = storagePath + "/" + byteToString(hash[0]) + "/chunk" + raindrop.getHash().getBase64();
        ServerUtils.createFileDirs(path);
        try {
            FileOutputStream dropStream = new FileOutputStream(path);
            dropStream.write(raindrop.getContent());
            dropStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String byteToString(byte b) {
        return String.valueOf(128 + b);
    }

    @Override
    public RainDrop retrieveRaindrop(RainHash rainHash) throws LogicException, FileNotFoundException {
        byte[] hash = rainHash.getBytes();
        String path = storagePath + "/" + byteToString(hash[0]) + "/chunk" + rainHash.getBase64();
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new LogicException("Non-existing raindrop with hash " + rainHash.getBase64());
        }
        FileInputStream stream = new FileInputStream(file);
        byte[] inputData = new byte[(int) file.length()];
        try {
            stream.read(inputData);
            stream.close();
            return new RainDrop(inputData);
        } catch (IOException e) {
            e.printStackTrace();
            throw new LogicException("Can't read a raindrop with hash " + rainHash.getBase64());
        }
    }
}
