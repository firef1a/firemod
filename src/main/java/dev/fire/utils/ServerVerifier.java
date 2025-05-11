package dev.fire.utils;

import dev.fire.Mod;
import net.minecraft.client.network.ServerInfo;

import java.util.Objects;

public class ServerVerifier {
    public static boolean isPlayingDiamondfire() {
        ServerInfo server = Mod.MC.getCurrentServerEntry();

        if (server == null) return false;
        String address = server.address;
        return address.contains("mcdiamondfire.com") || address.contains("54.39.29.75") || address.contains("51.222.245.178");
    }
}
