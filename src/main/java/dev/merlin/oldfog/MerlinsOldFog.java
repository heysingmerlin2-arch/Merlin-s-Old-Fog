package dev.merlin.oldfog;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MerlinsOldFog implements ClientModInitializer {
	public static final String MOD_ID = "merlins_old_fog";
	public static final Logger LOGGER = LoggerFactory.getLogger("Merlin's Old Fog");

	@Override
	public void onInitializeClient() {
		OldFogConfig.load();
		LOGGER.info("Merlin's Old Fog loaded - classic 1.8.9 fog enabled.");
	}
}
