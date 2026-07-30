package org.neoedit.neoedit;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
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

import java.util.ArrayDeque;
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

					case GLFW.GLFW_KEY_R:
						if (ctrlKeyHeld()) {
							redo();
						}
						else {
							undo();
						}
						break;

					default:
						break;
				}
			}
		}
	}

	private static void fillArea(BlockState blockState) {
		ArrayList<BlockEdit> blockEdits = new ArrayList<>();
		for (BlockPos pos : BlockPos.betweenClosed(posA, posB)) {
			blockEdits.add(new BlockEdit(pos.immutable(), blockState));
		}
		performEdit(new Edit(blockEdits));
	}

	public static void handleMouseInput(int button) {
		assert Minecraft.getInstance().player != null;
		BlockPos targetBlockPosition = null;
		if (Minecraft.getInstance().player.raycastHitResult(5.0f, Objects.requireNonNull(Minecraft.getInstance().getCameraEntity())) instanceof BlockHitResult blockHitResult) {
			if (blockHitResult.getType() == HitResult.Type.BLOCK) {
				if (ctrlKeyHeld()) {
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

	private static void performEdit(Edit edit) {
		edit.apply();
		undoStack.push(edit);
		redoStack.clear();
	}

	public static void sendMessage(String key) {
		if (Minecraft.getInstance().player != null) {
			MutableComponent prefix = Component.translatable("neoedit.messages.neoedit_message_prefix").withStyle(ChatFormatting.BLUE);
			MutableComponent colon = Component.literal(": ").withStyle(ChatFormatting.WHITE);
			MutableComponent message = Component.translatable(key).withStyle(ChatFormatting.WHITE);
			Minecraft.getInstance().player.sendSystemMessage(prefix.append(colon).append(message));
		}
	}

	private static void undo() {
		if (undoStack.isEmpty()) {
			return;
		}

		Edit edit = undoStack.pop();
		edit.undo();
		redoStack.push(edit);
	}

	private static void redo() {
		if (redoStack.isEmpty()) {
			return;
		}

		Edit edit = redoStack.pop();
		edit.apply();
		undoStack.push(edit);
	}

	private static boolean ctrlKeyHeld() {
		Window window = Minecraft.getInstance().getWindow();
		return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_CONTROL) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_CONTROL);
	}

	public static KeyMapping enterEditModeKey;

	public static boolean editModeEnabled;

	private static BlockPos posA;
	private static BlockPos posB;

	private static final ArrayDeque<Edit> undoStack = new ArrayDeque<>();
	private static final ArrayDeque<Edit> redoStack = new ArrayDeque<>();
}
