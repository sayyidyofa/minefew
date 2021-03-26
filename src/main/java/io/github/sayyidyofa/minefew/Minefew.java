package io.github.sayyidyofa.minefew;

import io.github.sayyidyofa.minefew.models.PlayerLog;
import io.github.sayyidyofa.minefew.models.ServerLog;
import io.github.sayyidyofa.minefew.services.WebhookService;
import io.github.sayyidyofa.minefew.utils.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("minefew")
public class Minefew {

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public Minefew() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent serverStartedEvent) {
        WebhookService.tellServerEvent(new ServerLog("Server started", new Date(), serverStartedEvent.getServer().getServerHostname()));
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent serverStoppingEvent) throws IOException, InterruptedException {
        WebhookService.tellServerEvent(new ServerLog("Server stopping", new Date(), serverStoppingEvent.getServer().getServerHostname()));
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber/*(bus = Mod.EventBusSubscriber.Bus.MOD)*/
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent playerLoggedInEvent) {
            WebhookService.tellPlayerAction(new PlayerLog(
                    playerLoggedInEvent.getPlayer().getDisplayName().getString(),
                    new Date(),
                    "player login",
                    Helpers.getIpFromAddress(((ServerPlayerEntity) playerLoggedInEvent.getEntity()).connection.netManager.getRemoteAddress().toString()),
                    ((ServerPlayerEntity) playerLoggedInEvent.getEntity()).server.getServerHostname()
            ));
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent playerLoggedOutEvent) {
            WebhookService.tellPlayerAction(new PlayerLog(
                    playerLoggedOutEvent.getPlayer().getDisplayName().getString(),
                    new Date(),
                    "player logout",
                    Helpers.getIpFromAddress(((ServerPlayerEntity) playerLoggedOutEvent.getEntity()).connection.netManager.getRemoteAddress().toString()),
                    ((ServerPlayerEntity) playerLoggedOutEvent.getEntity()).server.getServerHostname()
            ));
        }
    }
}
