package io.github.sayyidyofa.minefew.models;

import java.util.Date;

public class PlayerLog {
    public String playerName;
    public Date timestamp;
    public String action;
    public String playerAddress;
    public String serverAddress;

    public PlayerLog(String playerName, Date timestamp, String action, String remoteAddress, String serverAddress) {
        this.playerName = playerName;
        this.timestamp = timestamp;
        this.action = action;
        this.playerAddress = remoteAddress;
        this.serverAddress = serverAddress;
    }
}
