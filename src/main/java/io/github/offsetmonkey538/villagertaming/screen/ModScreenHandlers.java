package io.github.offsetmonkey538.villagertaming.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static io.github.offsetmonkey538.villagertaming.entrypoint.VillagerTamingMain.MOD_ID;

public final class ModScreenHandlers {
    public static final ExtendedScreenHandlerType<TamedVillagerScreenHandler> TAMED_VILLAGER = register("tamed_villager", new ExtendedScreenHandlerType<>(TamedVillagerScreenHandler::new));

    private static <T extends ScreenHandler, H extends ScreenHandlerType<T>> H register(String id, H type) {
        return Registry.register(Registry.SCREEN_HANDLER, new Identifier(MOD_ID, id), type);
    }

    public static void register() {
        // Registers everything by loading the class
    }
}
