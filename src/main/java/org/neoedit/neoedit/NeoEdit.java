package org.neoedit.neoedit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NeoEdit implements ModInitializer {
	public static final String MOD_ID = "neoedit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.serverboundPlay().register(EditPayload.TYPE, EditPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(EditPayload.TYPE, (payload, context) -> {
			ServerLevel level = context.player().level();
			for (int i = 0; i < payload.positions().size(); i++) {
				level.setBlock(payload.positions().get(i), payload.blocks().get(i), 3);
			}
		});

		LOGGER.info("Initialized NeoEdit successfully");
	}
}