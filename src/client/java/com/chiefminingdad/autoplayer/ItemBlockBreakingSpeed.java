package com.chiefminingdad.autoplayer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;

public class ItemBlockBreakingSpeed{
    ItemStack Item;
    BlockState Blck;
    WorldView World;
    PlayerEntity Player;
    BlockPos Pos;
    public ItemBlockBreakingSpeed(WorldView world, BlockState block, ItemStack item, PlayerEntity player, BlockPos pos){
        World = world;Blck = block; Item = item;Player=player; Pos = pos;
    }
    public ItemBlockBreakingSpeed(){

    }

    @Override
    public String toString() {
        return "com.chiefminingdad.autoplayer.ItemBlockBreakingSpeed{" +
                (Item!=null? ("Item=" + Item):"") +
                (Blck!=null?(", Blck=" + Blck):"") +
                (Player!=null?(", Player=" + Player):"") +
                ", Pos=" + Pos +
                '}';
    }

    public boolean canHarvest(@NotNull BlockState state, ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(state);
    }

    public float getSimpleSpeed(){
        int i = canHarvest(this.Blck,this.Item) ? 30 : 100;
        float h = this.Blck.getHardness(World,Pos);
        float f = this.Item.getMiningSpeedMultiplier(this.Blck);
        if (f > 1.0F) {
            f += (float)this.Player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
        }
        if (h == 0F) return 0F;
        if (h == -1F) return Float.MAX_VALUE;
        return f/i/h;
    }
    public float getFullSpeed(){
        float f = getSimpleSpeed();
        if (StatusEffectUtil.hasHaste(Player)) {
            f *= 1.0F + (StatusEffectUtil.getHasteAmplifier(Player) + 1) * 0.2F;
        }

        if (Player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float g = switch (Player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            f *= g;
        }

        f *= (float)Player.getAttributeValue(EntityAttributes.BLOCK_BREAK_SPEED);
//            TODO: Have a check to see if the block would be broken while someone is in water.
//            if (Player.isSubmergedIn(FluidTags.WATER)) {
//                f *= (float)Player.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).getValue();
//            }
        return f;
    }
    public boolean SimpleBetterThan(@NotNull ItemBlockBreakingSpeed other){
        return (this.getSimpleSpeed()<other.getSimpleSpeed());
    }
}