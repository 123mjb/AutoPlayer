package com.chiefminingdad.autoplayer.Weight;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BreakingSpeedFinder {

    public boolean canHarvest(@NotNull BlockState state, ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(state);
    }

    public float getSimpleSpeed(BlockState block, ItemStack item, BlockPos pos){
        MinecraftClient client = MinecraftClient.getInstance();
        World world = client.world;
        ClientPlayerEntity player = client.player;
        int i = canHarvest(block,item) ? 30 : 100;
        float h = block.getHardness(world, pos);
        float f = item.getMiningSpeedMultiplier(block);
        if (f > 1.0F) {
            assert player != null;
            f += (float)player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
        }
        if (h == 0F) return 0;
        if (h == -1F) return Float.MAX_VALUE;
        return f/i/h;
    }

    public float getFullSpeed(BlockState block, ItemStack item, BlockPos pos){
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        float f = getSimpleSpeed(block, item, pos);
        assert player != null;
        if (StatusEffectUtil.hasHaste(player)) {
            f *= 1.0F + (StatusEffectUtil.getHasteAmplifier(player) + 1) * 0.2F;
        }

        if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
            float g = switch (Objects.requireNonNull(player.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) {
                case 0 -> 0.3F;
                case 1 -> 0.09F;
                case 2 -> 0.0027F;
                default -> 8.1E-4F;
            };
            f *= g;
        }

        f *= (float)player.getAttributeValue(EntityAttributes.BLOCK_BREAK_SPEED);
//            TODO: Have a check to see if the block would be broken while someone is in water.
//            if (Player.isSubmergedIn(FluidTags.WATER)) {
//                f *= (float)Player.getAttributeInstance(EntityAttributes.SUBMERGED_MINING_SPEED).getValue();
//            }
        return f;
    }
}
