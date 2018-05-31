package services;

import models.Client;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class SimpleAccessService implements AccessService {
    private static final Logger logger = Logger.getLogger("AccessService");
    private static final int BLOCK_INTERVAL_MS = 500;
    private static final ConcurrentHashMap<String, Client> clientsList = new ConcurrentHashMap<>();

    @Override
    public Client getAccess(String ip) {
        Client client = clientsList.get(ip);
        if (client == null) {
            logger.info(String.format("client %s first visit", ip));
            client = new Client(ip, new Date());
            client.setBlocked(false);
        } else {
            logger.info(String.format("client %s re-entry", ip));
            checkAccess(client);
            client.setLastAccessDate(new Date());
        }
        clientsList.put(ip, client);
        return client;
    }

    private void checkAccess(Client client) {
        Date current = new Date();
        long inteval = current.getTime() - client.getLastAccessDate().getTime();
        if (inteval < BLOCK_INTERVAL_MS) {
            logger.info(String.format("client %s blocked, last access was %s ms ago", client.getIp(), inteval));
            client.setBlocked(true);
            client.setBlockMessage("Blocked to prevent abuse this API");
        } else {
            client.setBlocked(false);
            client.setBlockMessage(null);
        }
    }
}
