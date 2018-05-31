package services;

import models.Client;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAccessService implements AccessService {
    private static final int BLOCK_INTERVAL_MS = 500;
    private static final ConcurrentHashMap<String, Client> clientsList = new ConcurrentHashMap<>();

    @Override
    public Client getAccess(String ip) {
        Client client = clientsList.get(ip);
        if (client == null) {
            client = new Client(ip, new Date());
            client.setBlocked(false);
        } else {
            checkAccess(client);
            client.setLastAccessDate(new Date());
        }
        clientsList.put(ip, client);
        return client;
    }

    private void checkAccess(Client client) {
        Date current = new Date();
        if (current.getTime() - client.getLastAccessDate().getTime() < BLOCK_INTERVAL_MS) {
            client.setBlocked(true);
            client.setBlockMessage("Blocked to prevent abuse the API for");
        } else {
            client.setBlocked(false);
            client.setBlockMessage(null);
        }
    }
}
