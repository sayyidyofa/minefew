package io.github.sayyidyofa.minefew.services;

import io.github.sayyidyofa.minefew.models.PlayerLog;
import io.github.sayyidyofa.minefew.models.ServerLog;
import io.github.sayyidyofa.minefew.utils.Helpers;
import kong.unirest.Unirest;

import java.net.SocketTimeoutException;

public class WebhookService {
    private static final String whereToFire = System.getenv("HOOK_WHERE_TO_FIRE");
    private static final String authKey = System.getenv("HOOK_AUTH_KEY");

    public static void tellPlayerAction(PlayerLog playerLog) {
        try {
            Unirest.post(whereToFire)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + authKey)
                    .body(Helpers.serialize(playerLog)).asEmpty();
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }

    public static void tellServerEvent(ServerLog serverLog) {
        try {
            Unirest.post(whereToFire)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + authKey)
                    .body(Helpers.serialize(serverLog)).asEmpty();
        } catch (Exception exception) {
            System.out.println(exception.toString());
        }
    }
}
