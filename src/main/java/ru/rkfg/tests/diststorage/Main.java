package ru.rkfg.tests.diststorage;

import java.io.File;
import java.io.IOException;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import ru.ppsrk.gwt.client.ClientAuthenticationException;
import ru.ppsrk.gwt.client.LogicException;
import ru.ppsrk.gwt.server.HibernateUtil;
import ru.ppsrk.gwt.server.ServerUtils;

public class Main {

    static int i = 0;
    static long[] profile = new long[100];

    /**
     * @param args
     * @throws IOException
     * @throws ClientAuthenticationException
     * @throws LogicException
     */
    public static void main(String[] args) throws IOException, LogicException, ClientAuthenticationException {
        if (args.length == 0) {
            System.out.println("Specify the file to store.");
            return;
        }
        Security.addProvider(new BouncyCastleProvider());
        HibernateUtil.initSessionFactory("hibernate.cfg.xml");
        Storage storage = new FileStorage();
        profile();
        if (args[0].equals("--extract")) {
            RainHash hash = new RainHash(args[1]);
            String filename = args[2];
            Condenser condenser = new Condenser(hash, storage, filename);
            condenser.extractFile();
        } else {
            for (String filename : args) {
                Sprinkler sprinkler;
                String path = ServerUtils.expandHome(filename);
                File file = new File(path);
                if (!file.isDirectory() && file.exists()) {
                    sprinkler = new Sprinkler(path, storage, file.length() > RainDrop.RAINSIZE * FEC.k);
                    RainFile rainFile = sprinkler.storeFile();
                    System.out.println(rainFile);
                }
            }
        }
        profile();
        printProfileResult();
    }

    private static void profile() {
        profile[i++] = System.currentTimeMillis();
    }

    private static void printProfileResult() {
        for (int j = 1; j < i; j++) {
            System.out.println((j - 1) + "—" + j + ": " + (profile[j] - profile[j - 1]));
        }
    }
}
