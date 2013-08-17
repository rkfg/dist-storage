package ru.rkfg.tests.diststorage;

import org.hibernate.Session;

import ru.ppsrk.gwt.client.ClientAuthenticationException;
import ru.ppsrk.gwt.client.LogicException;
import ru.ppsrk.gwt.server.HibernateCallback;
import ru.ppsrk.gwt.server.HibernateUtil;
import ru.rkfg.tests.diststorage.domain.RainDropDB;

public class H2Storage implements Storage {

    @Override
    public void storeRaindrop(final RainDrop raindrop) {
        try {
            HibernateUtil.exec(new HibernateCallback<Void>() {

                @Override
                public Void run(Session session) throws LogicException, ClientAuthenticationException {
                    RainDropDB newDrop = new RainDropDB(raindrop.getContent(), raindrop.getHash().getBytes());
                    session.merge(newDrop);
                    return null;
                }
            });
        } catch (LogicException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientAuthenticationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public RainDrop retrieveRaindrop(RainHash hash) {
        // TODO Auto-generated method stub
        return null;
    }

}
