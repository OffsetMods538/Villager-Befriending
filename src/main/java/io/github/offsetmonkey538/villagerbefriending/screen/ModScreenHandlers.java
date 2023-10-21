package io.github.offsetmonkey538.villagerbefriending.screen;

import io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.TamedVillagerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;

public final class ModScreenHandlers {
    public static final ExtendedScreenHandlerType<TamedVillagerScreenHandler> TAMED_VILLAGER = register("tamed_villager", new ExtendedScreenHandlerType<>(TamedVillagerScreenHandler::new));

    private static <T extends ScreenHandler, H extends ScreenHandlerType<T>> H register(String id, H type) {
        return Registry.register(Registries.SCREEN_HANDLER, new Identifier(MOD_ID, id), type);
    }

    public static void register() {
        // Registers everything by loading the class
    }
}
