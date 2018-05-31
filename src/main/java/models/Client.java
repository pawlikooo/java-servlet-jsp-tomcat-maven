package models;

import java.util.Date;

public class Client {
    private String ip;
    private boolean blocked;
    private Date lastAccessDate;
    private String blockMessage;

    public Client(String ip, Date lastAccessDate) {
        this.ip = ip;
        this.lastAccessDate = lastAccessDate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getBlockMessage() {
        return blockMessage;
    }

    public void setBlockMessage(String blockMessage) {
        this.blockMessage = blockMessage;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }
}
