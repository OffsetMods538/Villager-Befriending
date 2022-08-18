package io.github.offsetmonkey538.villagertaming.entrypoint;

import io.github.offsetmonkey538.villagertaming.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class VillagerTamingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreens.register();
    }
}
