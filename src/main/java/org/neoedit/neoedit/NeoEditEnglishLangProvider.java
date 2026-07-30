package org.neoedit.neoedit;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class NeoEditEnglishLangProvider extends FabricLanguageProvider {
	protected NeoEditEnglishLangProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, "en_us", registryLookup);
	}

	@Override
	public void generateTranslations(HolderLookup.Provider holderLookup, TranslationBuilder translationBuilder) {
		translationBuilder.add("key.category.neoedit.neoedit", "NeoEdit");
		translationBuilder.add("key.neoedit.enter_edit_mode", "Enter Edit Mode");

		translationBuilder.add("neoedit.messages.neoedit_message_prefix", "[NeoEdit]");
		translationBuilder.add("neoedit.messages.enter_edit_mode", "Entered Edit Mode");
		translationBuilder.add("neoedit.messages.exit_edit_mode", "Exited Edit Mode");

		translationBuilder.add("neoedit.messages.set_pos_a", "Set position A to ");
		translationBuilder.add("neoedit.messages.set_pos_b", "Set position B to ");

		translationBuilder.add("neoedit.messages.fill_area_1", "Filled ");
		translationBuilder.add("neoedit.messages.fill_area_2", " blocks with ");
		translationBuilder.add("neoedit.messages.clear_area_1", "Cleared ");
		translationBuilder.add("neoedit.messages.clear_area_2", " blocks");
	}
}