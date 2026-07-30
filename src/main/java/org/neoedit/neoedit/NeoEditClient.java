package org.neoedit.neoedit;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Objects;

public final class NeoEditClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyMapping.Category neoeditCategory = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(NeoEdit.MOD_ID, "neoedit"));
		enterEditModeKey = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.neoedit.enter_edit_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, neoeditCategory)
		);

		ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
			while (enterEditModeKey.consumeClick()) {
				if (minecraft.player != null) {
					if (editModeEnabled) {
						sendMessage("neoedit.messages.exit_edit_mode");
					}
					else {
						sendMessage("neoedit.messages.enter_edit_mode");
					}
					editModeEnabled = !editModeEnabled;
				}
			}
		});
	}

	public static void handleKeyboardInput(int action, int key) {
		if (Minecraft.getInstance().player != null && posA != null && posB != null) {
			if (action == GLFW.GLFW_PRESS) {
				switch (key) {
					case GLFW.GLFW_KEY_F:
						ItemStack itemStack = Minecraft.getInstance().player.getMainHandItem();
						if (itemStack.isEmpty()) {
							fillArea(Blocks.AIR.defaultBlockState());
						}
						else if (itemStack.getItem() instanceof BlockItem blockItem) {
							fillArea(blockItem.getBlock().defaultBlockState());
						}
						break;

					case GLFW.GLFW_KEY_LEFT_CONTROL:
						ctrlKeyHeld = true;
						break;

					default:
						break;
				}
			}
			else if (action == GLFW.GLFW_RELEASE) {
                if (key == GLFW.GLFW_KEY_LEFT_CONTROL) {
                    ctrlKeyHeld = false;
                }
			}
		}
	}

	private static void fillArea(BlockState blockState) {
		ArrayList<BlockEdit> edits = new ArrayList<>();
		for (BlockPos pos : BlockPos.betweenClosed(posA, posB)) {
			edits.add(new BlockEdit(pos.immutable(), blockState));
		}
		Edit fillAreaEdit = new Edit(edits);
		fillAreaEdit.apply();
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
			posA = targetBlockPosition;
		}
		else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			posB = targetBlockPosition;
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

	public static KeyMapping enterEditModeKey;

	public static boolean editModeEnabled;

	private static BlockPos posA;
	private static BlockPos posB;

	private static boolean ctrlKeyHeld = false;
}
