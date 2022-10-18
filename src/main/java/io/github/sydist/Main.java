package io.github.sydist;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	private static final String ID = "stormfest";
	private static final SoundEvent SOUNDEVENT = new SoundEvent(new Identifier(ID, "music_disc.stormfest"));
    private static final Item.Settings DEFAULT_SETTINGS = new Item.Settings().maxCount(1).group(ItemGroup.MISC);
	
	private static final Item STORMFEST = new MusicDiscItem(15, SOUNDEVENT, DEFAULT_SETTINGS.rarity(Rarity.RARE), 166);
	private static final Item ENCHANTED_STORMFEST = new EnchantedMusicDiscItem(15, SOUNDEVENT, DEFAULT_SETTINGS.rarity(Rarity.EPIC), 166, context -> ((ServerWorld)context.getWorld()).setWeather(0, 3320, true, true), STORMFEST);

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(ID, "enchanted_music_disc_stormfest"), ENCHANTED_STORMFEST);
		Registry.register(Registry.ITEM, new Identifier(ID, "music_disc_stormfest"), STORMFEST);
		FabricLoader.getInstance().getModContainer(ID).map(
			container -> ResourceManagerHelper.registerBuiltinResourcePack(
				new Identifier(ID, "programmer_art"),
				container,
				"Stormfest's Programmer Art",
				ResourcePackActivationType.NORMAL
			)
		);
	}
}
