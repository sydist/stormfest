package io.github.sydist;

import java.util.function.Consumer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class EnchantedMusicDiscItem extends MusicDiscItem {
    private Consumer<ItemUsageContext> ability;
    private Item nonEnchantedItem;

    public EnchantedMusicDiscItem(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds,
            Consumer<ItemUsageContext> ability, Item nonEnchantedItem) {
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
        BlockPos blockPos;
        World world = context.getWorld();
        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
        if (!blockState.isOf(Blocks.JUKEBOX) || blockState.get(JukeboxBlock.HAS_RECORD).booleanValue()) {
            return ActionResult.PASS;
        }
        ItemStack itemStack = context.getStack();
        if (!world.isClient) {
            PlayerEntity playerEntity = context.getPlayer();
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof JukeboxBlockEntity) {
                JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity) blockEntity;
                jukeboxBlockEntity.setStack(new ItemStack(nonEnchantedItem));
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState));
                this.ability.accept(context);
            }
            itemStack.decrement(1);
            if (playerEntity != null) {
                playerEntity.incrementStat(Stats.PLAY_RECORD);
            }
        }
        return ActionResult.success(world.isClient);
    }
}
