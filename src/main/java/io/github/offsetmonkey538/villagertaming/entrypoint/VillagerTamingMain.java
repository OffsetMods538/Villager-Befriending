package io.github.offsetmonkey538.villagertaming.entrypoint;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.passive.VillagerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VillagerTamingMain implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("villagertaming");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}
