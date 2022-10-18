package io.github.sydist;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;

public class EnchantedMusicDiscItem extends MusicDiscItem {
    private Consumer<ItemUsageContext> ability;
    private Item nonEnchantedItem;

    public EnchantedMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds, Consumer<ItemUsageContext> ability, Item nonEnchantedItem) {
        super(comparatorOutput, sound, settings, lengthInSeconds);
        this.ability = ability;
        this.nonEnchantedItem = nonEnchantedItem;
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }
    
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        BlockState block = world.getBlockState(blockPos);
        
        if (!block.isOf(Blocks.JUKEBOX) || block.get(JukeboxBlock.HAS_RECORD).booleanValue())
            return ActionResult.PASS;
        
        if (!world.isClient) {
            JukeboxBlock jukebox = (JukeboxBlock)Blocks.JUKEBOX;
            ServerWorld server = (ServerWorld)world;
            PlayerEntity player = context.getPlayer();
            context.getStack().decrement(1);

            jukebox.setRecord(player, server, blockPos, block, new ItemStack(nonEnchantedItem));
            server.syncWorldEvent(null, WorldEvents.MUSIC_DISC_PLAYED, blockPos, Item.getRawId(this));
            this.ability.accept(context);

            Optional.ofNullable(player).ifPresent(entity -> entity.increaseStat(Stats.PLAY_RECORD, 1));
        }
        
        return ActionResult.success(world.isClient);
    }
}
