package org.neoedit.neoedit;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public record FillAreaPayload(BlockPos posA, BlockPos posB, BlockState Block) implements CustomPacketPayload {
	public static final Identifier GIVE_GLOWING_EFFECT_PAYLOAD_ID = Identifier.fromNamespaceAndPath(NeoEdit.MOD_ID, "fill_region");
	public static final CustomPacketPayload.Type<FillAreaPayload> TYPE = new CustomPacketPayload.Type<>(GIVE_GLOWING_EFFECT_PAYLOAD_ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, FillAreaPayload> CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC, FillAreaPayload::posA,
		BlockPos.STREAM_CODEC, FillAreaPayload::posB,
		ByteBufCodecs.fromCodec(BlockState.CODEC), FillAreaPayload::Block,
		FillAreaPayload::new
	);

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
