package ru.rkfg.tests.diststorage;

import java.io.FileNotFoundException;

import ru.ppsrk.gwt.client.LogicException;

public interface Storage {
    public void storeRaindrop(RainDrop raindrop);

    public RainDrop retrieveRaindrop(RainHash hash) throws LogicException, FileNotFoundException;
}
