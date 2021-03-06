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
    private int dropSize;
    private int k;
    private int n;

    public Sprinkler(String filename, Storage storage, boolean doFEC, int k, int n) throws LogicException, IOException {
        File file = new File(filename);
        stream = new FileInputStream(file);
        fileLength = file.length();
        dropSize = RainDrop.getBestDropSize(fileLength);
        this.filename = file.getName();
        this.doFEC = doFEC;
        this.storage = storage;
        this.k = k;
        this.n = n;
        if (doFEC) {
            reader = new FECContentReader(stream, fileLength, k, n);
        } else {
            reader = new PlainContentReader(stream, fileLength);
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
        return new RainFile(filename, doFEC, raindropHashes, fileLength, k, n);
    }

    public int getDropSize() {
        return dropSize;
    }

    public void setDropSize(int dropSize) {
        this.dropSize = dropSize;
    }
}
