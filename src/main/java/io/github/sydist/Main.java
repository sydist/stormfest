package io.github.sydist;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class Main implements ModInitializer {
	private static final String ID = "stormfest";
	private static final SoundEvent SOUNDEVENT = SoundEvent.of(new Identifier(ID, "music_disc.stormfest"));
	private static final Item.Settings DEFAULT_SETTINGS = new Item.Settings().maxCount(1);

	private static final Item STORMFEST = Registry.register(
			Registries.ITEM,
			new Identifier(ID, "music_disc_stormfest"),
			new MusicDiscItem(
					15,
					SOUNDEVENT,
					DEFAULT_SETTINGS.rarity(Rarity.RARE),
					166));

	private static final Item ENCHANTED_STORMFEST = Registry.register(
			Registries.ITEM,
			new Identifier(ID, "enchanted_music_disc_stormfest"),
			new EnchantedMusicDiscItem(
					15,
					SOUNDEVENT,
					DEFAULT_SETTINGS.rarity(Rarity.EPIC),
					166,
					context -> ((ServerWorld) context.getWorld()).setWeather(0, 3320, true, true),
					STORMFEST));

	@Override
	public void onInitialize() {
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
				.register(content -> content.addAfter(Items.MUSIC_DISC_RELIC, STORMFEST));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
				.register(content -> content.addAfter(Items.MUSIC_DISC_RELIC, ENCHANTED_STORMFEST));

		FabricLoader.getInstance().getModContainer(ID).map(
				container -> ResourceManagerHelper.registerBuiltinResourcePack(
						new Identifier(ID, "programmer_art"),
						container,
						Text.of("Stormfest's Programmer Art"),
						ResourcePackActivationType.NORMAL));
	}
}
