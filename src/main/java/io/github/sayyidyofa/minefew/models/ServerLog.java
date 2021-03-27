package io.github.sayyidyofa.minefew.models;

import java.util.Date;

public class ServerLog {
    public ServerLog(String serverEvent, Date timestamp, String serverAddress) {
        this.serverEvent = serverEvent;
        this.timestamp = timestamp;
        this.serverAddress = serverAddress;
    }

    public String serverEvent;
    public Date timestamp;
    public String serverAddress;

    @Override
    public String toString() {
        return '{' +
                "\"serverEvent\": \"" + serverEvent + "\"," +
                "\"timestamp\": \"" + timestamp.toString() + "\"," +
                "\"serverAddress\": \"" + serverAddress + "\"," +
                '}';
    }
}
