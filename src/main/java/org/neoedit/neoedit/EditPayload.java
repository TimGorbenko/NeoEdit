package org.neoedit.neoedit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record EditPayload(List<BlockPos> positions, List<BlockState> blocks) implements CustomPacketPayload {
	public static final Identifier PAYLOAD_ID = Identifier.fromNamespaceAndPath(NeoEdit.MOD_ID, "fill_region");
	public static final CustomPacketPayload.Type<EditPayload> TYPE = new CustomPacketPayload.Type<>(PAYLOAD_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, EditPayload> CODEC = StreamCodec.composite(
		ByteBufCodecs.fromCodec(BlockPos.CODEC.listOf()), EditPayload::positions,
		ByteBufCodecs.fromCodec(BlockState.CODEC.listOf()), EditPayload::blocks,
		EditPayload::new
	);

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
