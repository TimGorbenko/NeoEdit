package org.neoedit.neoedit.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.lwjgl.glfw.GLFW;
import org.neoedit.neoedit.NeoEditEditor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
	@Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
	private void onButtonPress(long handle, MouseButtonInfo rawButtonInfo, int action, CallbackInfo callbackInfo) {
		if (!NeoEditEditor.editModeEnabled || Minecraft.getInstance().screen != null || rawButtonInfo.button() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE || action != 1) {
			return;
		}

		NeoEditEditor.handleMouseInput(rawButtonInfo.button());
		callbackInfo.cancel();
	}
}
