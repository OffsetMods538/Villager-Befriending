package io.github.offsetmonkey538.villagerbefriending.entrypoint;

import io.github.offsetmonkey538.villagerbefriending.item.ModItems;
import io.github.offsetmonkey538.villagerbefriending.screen.ModScreenHandlers;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerBefriendingMain implements ModInitializer {
	public static final String MOD_ID = "villagerbefriending";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.register();
		ModScreenHandlers.register();
	}
}
