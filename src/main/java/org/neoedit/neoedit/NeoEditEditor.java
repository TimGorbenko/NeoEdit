package org.neoedit.neoedit;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;
import java.util.Objects;

public final class NeoEditEditor {
	public static void handleKeyboardInput(int action, int key) {
		if (Minecraft.getInstance().player != null && PosA != null && PosB != null) {
			if (action == GLFW.GLFW_PRESS) {
				switch (key) {
					case GLFW.GLFW_KEY_F:
						if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof BlockItem BlockItem) {
							FillAreaPayload fillAreaPayload = new FillAreaPayload(PosA, PosB, BlockItem.getBlock().defaultBlockState());
							ClientPlayNetworking.send(fillAreaPayload);
						}
						break;

					case GLFW.GLFW_KEY_X:
						FillAreaPayload fillAreaPayload = new FillAreaPayload(PosA, PosB, Blocks.AIR.defaultBlockState());
						ClientPlayNetworking.send(fillAreaPayload);
						break;

					case GLFW.GLFW_KEY_LEFT_CONTROL:
						ctrlKeyHeld = true;
						break;

					default:
						break;
				}
			}
			else if (action == GLFW.GLFW_RELEASE) {
				switch (key) {
					case GLFW.GLFW_KEY_LEFT_CONTROL:
						ctrlKeyHeld = false;
						break;

					default:
						break;
				}
			}
		}
	}

	public static void handleMouseInput(int button) {
		assert Minecraft.getInstance().player != null;

		BlockPos targetBlockPosition = null;

		if (Minecraft.getInstance().player.raycastHitResult(5.0f, Objects.requireNonNull(Minecraft.getInstance().getCameraEntity())) instanceof BlockHitResult blockHitResult) {
			if (blockHitResult.getType() == HitResult.Type.BLOCK) {
				if (ctrlKeyHeld) {
					targetBlockPosition = blockHitResult.getBlockPos().offset(blockHitResult.getDirection().getUnitVec3i());
				}
				else {
					targetBlockPosition = blockHitResult.getBlockPos();
				}
			}
		}

		if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			PosA = targetBlockPosition;
		}
		else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			PosB = targetBlockPosition;
		}
	}

	public static void sendMessage(String key) {
		if (Minecraft.getInstance().player != null) {
			MutableComponent prefix = Component.translatable("neoedit.messages.neoedit_message_prefix").withStyle(ChatFormatting.BLUE);
			MutableComponent colon = Component.literal(": ").withStyle(ChatFormatting.WHITE);
			MutableComponent message = Component.translatable(key).withStyle(ChatFormatting.WHITE);
			Minecraft.getInstance().player.sendSystemMessage(prefix.append(colon).append(message));
		}
	}

	public static boolean editModeEnabled;

	private static BlockPos PosA;
	private static BlockPos PosB;

	private static boolean ctrlKeyHeld = false;
}
