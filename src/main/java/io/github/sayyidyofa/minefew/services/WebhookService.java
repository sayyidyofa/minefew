package io.github.sayyidyofa.minefew.services;

import io.github.sayyidyofa.minefew.models.PlayerLog;
import io.github.sayyidyofa.minefew.models.ServerLog;


import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class WebhookService {
    private static final String whereToFire = System.getenv("HOOK_WHERE_TO_FIRE");
    private static final String authKey = System.getenv("HOOK_AUTH_KEY");

    public static void tellPlayerAction(PlayerLog playerLog) {
        try {
            URL url = new URL(whereToFire);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            byte[] out = playerLog.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Authorization", "Bearer " + authKey);
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static void tellServerEvent(ServerLog serverLog) {
        try {
            URL url = new URL(whereToFire);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);

            byte[] out = serverLog.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/json");
            http.setRequestProperty("Authorization", "Bearer " + authKey);
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
