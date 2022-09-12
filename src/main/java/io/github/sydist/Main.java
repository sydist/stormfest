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
	public static final String MOD_ID = "stormfest";

	private static final String MUSIC_DISC_STORMFEST_SOUNDEVENT_ID = "music_disc.stormfest";
	private static final SoundEvent MUSIC_DISC_STORMFEST_SOUNDEVENT = new SoundEvent(new Identifier(MOD_ID, MUSIC_DISC_STORMFEST_SOUNDEVENT_ID));
    
	private static final Item.Settings DEFAULT_SETTINGS = new Item.Settings().maxCount(1).group(ItemGroup.MISC);
	public static final Item MUSIC_DISC_STORMFEST_ITEM = new MusicDiscItem(1, MUSIC_DISC_STORMFEST_SOUNDEVENT, DEFAULT_SETTINGS.rarity(Rarity.RARE), 166);
	public static final Item ENCHANTED_MUSIC_DISC_ITEM = new EnchantedMusicDiscItem(
		15, 
		MUSIC_DISC_STORMFEST_SOUNDEVENT, 
		DEFAULT_SETTINGS.rarity(Rarity.EPIC), 
		166, 
		context -> ((ServerWorld)context.getWorld()).setWeather(0, 3320, true, true)
	);

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "enchanted_music_disc"), ENCHANTED_MUSIC_DISC_ITEM);
		Registry.register(Registry.ITEM, new Identifier(MOD_ID, "music_disc_stormfest"), MUSIC_DISC_STORMFEST_ITEM);
		FabricLoader.getInstance().getModContainer(MOD_ID).map(
			container -> ResourceManagerHelper.registerBuiltinResourcePack(
				new Identifier(MOD_ID, "programmer_art"),
				container,
				"Stormfest's Programmer Art",
				ResourcePackActivationType.NORMAL
			)
		);
	}
}
