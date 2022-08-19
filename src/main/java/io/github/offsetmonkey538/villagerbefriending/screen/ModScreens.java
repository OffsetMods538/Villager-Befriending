package io.github.offsetmonkey538.villagerbefriending.screen;


import io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.TamedVillagerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import static io.github.offsetmonkey538.villagerbefriending.screen.ModScreenHandlers.TAMED_VILLAGER;

public final class ModScreens {

    public static void register() {
        HandledScreens.register(TAMED_VILLAGER, TamedVillagerScreen::new);
    }
}
