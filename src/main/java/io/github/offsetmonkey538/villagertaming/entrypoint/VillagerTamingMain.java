package io.github.offsetmonkey538.villagertaming.entrypoint;

import io.github.offsetmonkey538.villagertaming.item.ModItems;
import io.github.offsetmonkey538.villagertaming.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerTamingMain implements ModInitializer {
	public static final String MOD_ID = "villagertaming";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.register();
		ModScreenHandlers.register();
	}
}
