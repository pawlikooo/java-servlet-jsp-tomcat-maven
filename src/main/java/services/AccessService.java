package services;

import models.Client;

public interface AccessService {
    Client getAccess(String ip);
}
