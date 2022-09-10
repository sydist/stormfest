package io.github.sydist;

import java.util.Optional;
import java.util.function.Consumer;

import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.world.WorldEvents;

public class EnchantedMusicDiscItem extends MusicDiscItem {
    private Consumer<ItemUsageContext> ability;

    public EnchantedMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds, Consumer<ItemUsageContext> ability) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
        this.ability = ability;
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        var world = context.getWorld();
        var blockPos = context.getBlockPos();
        var block = world.getBlockState(blockPos);
        
        if (!block.isOf(Blocks.JUKEBOX) || block.get(JukeboxBlock.HAS_RECORD).booleanValue())
            return ActionResult.PASS;
        
        if (!world.isClient) {
            var jukebox = (JukeboxBlock)Blocks.JUKEBOX;
            var server = (ServerWorld)world;
            var player = context.getPlayer();
            context.getStack().decrement(1);

            jukebox.setRecord(player, server, blockPos, block, new ItemStack(Main.MUSIC_DISC_STORMFEST_ITEM));
            server.syncWorldEvent(null, WorldEvents.MUSIC_DISC_PLAYED, blockPos, Item.getRawId(this));
            ability.accept(context);

            Optional.ofNullable(player).ifPresent(entity -> entity.increaseStat(Stats.PLAY_RECORD, 1));
        }
        
        return ActionResult.success(world.isClient);
    }
}
