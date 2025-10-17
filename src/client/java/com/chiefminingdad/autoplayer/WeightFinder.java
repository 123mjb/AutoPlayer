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

    public WeightInfo findBelowWeight(BlockState UnderneathBlock, BlockPos UnderneathBlockPos){
        return WeightSwitches(UnderneathBlock,0,UnderneathBlockPos);
    }
    public WeightInfo findTopWeight(BlockState UnderneathBlock, BlockPos UnderneathBlockPos){
        return WeightSwitches(UnderneathBlock,2,UnderneathBlockPos);
    }
    public WeightInfo findBottomWeight(BlockState UnderneathBlock, BlockPos UnderneathBlockPos){
        return WeightSwitches(UnderneathBlock,1,UnderneathBlockPos);
    }
    public WeightInfo WeightSwitches(BlockState block,int WhichPredicament, BlockPos blockPos){
        if(WhichPredicament==0){
            Block checkblock= block.getBlock();
            if(checkblock==BlueIce)return new WeightInfo(4.376F);
            else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return new WeightInfo(4.157F);
            else if(checkblock==SlimeBlock) return new WeightInfo(3.04F);
            else if(checkblock==HoneyBlock| checkblock== SoulSand) return new WeightInfo(2.508F);
        }
        else if(WhichPredicament==1){
            ItemBlockBreakingSpeed bestItem = getBestInventoryItemForBlock(Player.getInventory(),block,blockPos);
            WeightInfo temp = new WeightInfo();
            temp.BottomBlock = bestItem;
            return temp;
        }else if(WhichPredicament==2){
            ItemBlockBreakingSpeed bestItem = getBestInventoryItemForBlock(Player.getInventory(),block,blockPos);
            WeightInfo temp = new WeightInfo();
            temp.TopBlock = bestItem;
            return temp;
        }
        return new WeightInfo();
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

    public static class WeightInfo{
        ItemBlockBreakingSpeed TopBlock=null;
        ItemBlockBreakingSpeed BottomBlock=null;
        float WalkingTime=-1.0F;
        BlockPos PreviousBlock = null;
        float PreviousWeight = 0F;
        public WeightInfo(ItemBlockBreakingSpeed ItemSpeedTop,ItemBlockBreakingSpeed ItemSpeedBottom, float walkingTime){
            TopBlock = ItemSpeedTop;
            BottomBlock = ItemSpeedBottom;
            WalkingTime = walkingTime;
        }
        public WeightInfo(ItemBlockBreakingSpeed ItemSpeedTop,ItemBlockBreakingSpeed ItemSpeedBottom, float walkingTime,  BlockPos previousPos, float previousweight){
            TopBlock = ItemSpeedTop;
            BottomBlock = ItemSpeedBottom;
            WalkingTime = walkingTime;
            PreviousWeight = previousweight;
            PreviousBlock = previousPos;
        }
        public WeightInfo(float walkingTime){
            WalkingTime = walkingTime;
        }
        public WeightInfo(){
        }

        public float Total(){
            return TopBlock.getFullSpeed()+BottomBlock.getFullSpeed()+WalkingTime;
        }

        public boolean lessThan(WeightInfo other){
            return this.Total()<other.Total();
        }

        public WeightInfo append(WeightInfo newLocation, BlockPos oldPos){
            WeightInfo temp = newLocation;
            temp.PreviousWeight = Total();
            temp.PreviousBlock = oldPos;
            return temp;
        }

        public void merge(WeightInfo otherweights){
            if(TopBlock == null) TopBlock = otherweights.TopBlock;
            if(BottomBlock == null) BottomBlock = otherweights.BottomBlock;
            if(WalkingTime==-1.0F) WalkingTime = otherweights.WalkingTime;
        }
    }
    public static class WorstWeight extends WeightInfo{
        @Override
        public float Total() {
            return Float.MAX_VALUE;
        }
    }
}
