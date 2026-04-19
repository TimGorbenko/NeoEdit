package org.neoedit.neoedit.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;
import org.neoedit.neoedit.NeoEditEditor;
import org.neoedit.neoedit.NeoEditClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
	@Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
	private void onKeyPress(long handle, int action, KeyEvent event, CallbackInfo callbackInfo) {
		int key = event.key();
		if (!NeoEditEditor.editModeEnabled || key == GLFW.GLFW_KEY_ESCAPE) {
			return;
		}
		NeoEditEditor.handleKeyboardInput(action, key);

		KeyMapping[] allowedKeys = {
			Minecraft.getInstance().options.keyUp,
			Minecraft.getInstance().options.keyDown,
			Minecraft.getInstance().options.keyLeft,
			Minecraft.getInstance().options.keyRight,
			Minecraft.getInstance().options.keyJump,
			Minecraft.getInstance().options.keyShift,
			Minecraft.getInstance().options.keyInventory,
			Minecraft.getInstance().options.keySprint,

			NeoEditClient.enterEditModeKey
		};
		for (KeyMapping keyMapping : allowedKeys) {
			if (keyMapping.matches(event)) {
				return;
			}
		}

		callbackInfo.cancel();
	}
}
