package org.neoedit.neoedit;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

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
					if (NeoEditEditor.editModeEnabled) {
						NeoEditEditor.sendMessage("neoedit.messages.exit_edit_mode");
					}
					else {
						NeoEditEditor.sendMessage("neoedit.messages.enter_edit_mode");
					}
					NeoEditEditor.editModeEnabled = !NeoEditEditor.editModeEnabled;
				}
			}
		});
	}

	public static KeyMapping enterEditModeKey;
}
