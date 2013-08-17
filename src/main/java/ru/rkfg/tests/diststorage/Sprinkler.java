package ru.rkfg.tests.diststorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import ru.ppsrk.gwt.client.LogicException;

public class Sprinkler {

    InputStream stream;
    private String filename;
    final private List<RainHash> raindropHashes = new LinkedList<RainHash>();
    private boolean doFEC;
    private Storage storage;
    RaindropContentReader reader;
    private long fileLength;

    public Sprinkler(String filename, Storage storage, boolean doFEC) throws LogicException, IOException {
        File file = new File(filename);
        stream = new FileInputStream(file);
        fileLength = file.length();
        this.filename = file.getName();
        this.doFEC = doFEC;
        this.storage = storage;
        if (doFEC) {
            reader = new FECContentReader(stream);
        } else {
            reader = new PlainContentReader(stream);
        }
    }

    public RainFile storeFile() throws IOException {
        try {
            while (true) {
                RainDrop result = new RainDrop(reader.readBlock());
                System.out.println("Adding: " + result.getHash().getBase64());
                raindropHashes.add(result.getHash());
                storage.storeRaindrop(result);
            }
        } catch (NoMoreRaindrops e) {
        }
        return new RainFile(filename, doFEC, raindropHashes, fileLength);
    }
}
