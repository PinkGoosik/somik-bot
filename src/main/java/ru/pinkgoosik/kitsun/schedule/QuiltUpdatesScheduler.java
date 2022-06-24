package ru.pinkgoosik.kitsun.schedule;

import discord4j.rest.http.client.ClientException;
import ru.pinkgoosik.kitsun.Bot;
import ru.pinkgoosik.kitsun.feature.KitsunDebugger;
import ru.pinkgoosik.kitsun.util.ServerUtils;

public class QuiltUpdatesScheduler {

    public static void schedule() {
        try {
            ServerUtils.forEach((serverData) -> {
                if(serverData.quiltUpdates.get().enabled) {
                    serverData.quiltUpdates.get().check();
                }
            });
        }
        catch(ClientException e) {
            if(e.getMessage().contains("Missing Permissions")) {

            }
            else {
                String msg = "Failed to schedule quilt updates publishers duo to an exception:\n" + e;
                Bot.LOGGER.error(msg);
                KitsunDebugger.report(msg, e, true);
            }
        }
        catch (Exception e) {
            String msg = "Failed to schedule quilt updates publishers duo to an exception:\n" + e;
            Bot.LOGGER.error(msg);
            KitsunDebugger.report(msg, e, true);
        }
    }
}
