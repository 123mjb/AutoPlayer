package com.chiefminingdad.autoplayer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class WeightFinder{
    ClientPlayerEntity Player;
    BlockManager BM;
    World world;
    public WeightFinder(ClientPlayerEntity player, BlockManager bM, World wrld){
        Player = player;
        BM = bM;
        world = wrld;
    }

    private final Block BlueIce = Blocks.BLUE_ICE;
    private final Block Ice = Blocks.ICE;
    private final Block FrostedIce = Blocks.FROSTED_ICE;
    private final Block PackedIce = Blocks.PACKED_ICE;
    private final Block SlimeBlock = Blocks.SLIME_BLOCK;
    private final Block HoneyBlock = Blocks.HONEY_BLOCK;
    private final Block SoulSand = Blocks.SOUL_SAND;

    public float FindBelowWeight(BlockState UnderneathBlock, BlockPos UnderneathBlockPos){
        return WeightSwitches(UnderneathBlock,0,UnderneathBlockPos);
    }
    public float WeightSwitches(BlockState block,int WhichPredicament, BlockPos blockPos){
        if(WhichPredicament==0){
            Block checkblock= block.getBlock();
            if(checkblock==BlueIce)return 4.376F;
            else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return 4.157F;
            else if(checkblock==SlimeBlock) return 3.04F;
            else if(checkblock==HoneyBlock| checkblock== SoulSand) return 2.508F;
        }
        else if(WhichPredicament==1){
            ItemBlockBreakingSpeed bestItem = getBestInventoryItemForBlock(Player.getInventory(),block,blockPos);
            return bestItem.getFullSpeed();
        }
        return -1;
    }

    public ItemBlockBreakingSpeed getBestInventoryItemForBlock(PlayerInventory inventory,BlockState block,BlockPos blockpos){
        ItemBlockBreakingSpeed Best = null;
        for (int i = 0;i<inventory.size();i++) {
            ItemBlockBreakingSpeed newSpeed = new ItemBlockBreakingSpeed(world, block,inventory.getStack(i),this.Player,blockpos);
            if(Best==null) {Best = newSpeed;continue;}
            if(newSpeed.SimpleBetterThan(Best)){Best = newSpeed;}
        }
        return Best;
    }
    public class ItemBlockBreakingSpeed{
        ItemStack Item;
        BlockState Blck;
        WorldView World;
        PlayerEntity Player;
        BlockPos Pos;
        public ItemBlockBreakingSpeed(WorldView world, BlockState block, ItemStack item, PlayerEntity player,BlockPos pos){
            World = world;Blck = block; Item = item;Player=player; Pos = pos;
        }

        public float getSimpleSpeed(){
            int i = canHarvest(this.Blck,this.Item) ? 30 : 100;
            float h = this.Blck.getHardness(World,Pos);
            float f = this.Item.getMiningSpeedMultiplier(this.Blck);
            if (f > 1.0F) {
                f += (float)this.Player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
            }
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
        public boolean SimpleBetterThan(ItemBlockBreakingSpeed other){
            return (this.getSimpleSpeed()<other.getSimpleSpeed());
        }
    }
    public boolean canHarvest(BlockState state,ItemStack stack) {
        return !state.isToolRequired() || stack.isSuitableFor(state);
    }
}
