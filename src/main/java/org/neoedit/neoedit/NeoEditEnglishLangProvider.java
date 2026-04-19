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
	}
}