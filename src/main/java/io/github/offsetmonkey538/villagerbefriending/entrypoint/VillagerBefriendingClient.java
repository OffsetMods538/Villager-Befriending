package io.github.offsetmonkey538.villagerbefriending.entrypoint;

import io.github.offsetmonkey538.villagerbefriending.screen.ModScreens;
import net.fabricmc.api.ClientModInitializer;

public class VillagerBefriendingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModScreens.register();
    }
}
