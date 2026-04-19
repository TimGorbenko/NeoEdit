package org.neoedit.neoedit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Queue;

public class NeoEdit implements ModInitializer {
	public static final String MOD_ID = "neoedit";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		PayloadTypeRegistry.serverboundPlay().register(FillAreaPayload.TYPE, FillAreaPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(FillAreaPayload.TYPE, (payload, context) -> {
			ServerLevel level = context.player().level();

			BlockPos posA = payload.posA();
			BlockPos posB = payload.posB();

			int minX = Math.min(posA.getX(), posB.getX());
			int minY = Math.min(posA.getY(), posB.getY());
			int minZ = Math.min(posA.getZ(), posB.getZ());

			int maxX = Math.max(posA.getX(), posB.getX());
			int maxY = Math.max(posA.getY(), posB.getY());
			int maxZ = Math.max(posA.getZ(), posB.getZ());

			Queue<BlockPos> queue = new ArrayDeque<>();

			for (BlockPos pos : BlockPos.betweenClosed(minX, minY, minZ, maxX, maxY, maxZ)) {
				queue.add(pos.immutable());
			}

			level.getServer().execute(() -> {
				int minBlocksPerTick = 100;
				long maxNanosPerTick = 40_000_000;

				ServerTickEvents.END_SERVER_TICK.register(server -> {
					long start = System.nanoTime();
					int i = 0;
					while (!queue.isEmpty() && i < minBlocksPerTick) {
						BlockPos position = queue.poll();
						level.setBlock(position, payload.Block(), 3);
						i++;
					}

					while (!queue.isEmpty()  && (System.nanoTime() - start) < maxNanosPerTick) {
						BlockPos position = queue.poll();
						assert position != null;
						level.setBlock(position, payload.Block(), 3);
						i++;
					}
				});
			});
		});

		LOGGER.info("Initialized NeoEdit successfully");
	}
}