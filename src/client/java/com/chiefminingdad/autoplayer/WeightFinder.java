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
import org.jetbrains.annotations.NotNull;

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
    public WeightInfo WeightSwitches(@NotNull BlockState block, int WhichPredicament, BlockPos blockPos){
        Block checkblock= block.getBlock();
        if(WhichPredicament==0){

            if(checkblock==BlueIce)return new WeightInfo(4.376F);
            else if(checkblock==Ice|checkblock==FrostedIce|checkblock==PackedIce)return new WeightInfo(4.157F);
            else if(checkblock==SlimeBlock) return new WeightInfo(3.04F);
            else if(checkblock==HoneyBlock| checkblock== SoulSand) return new WeightInfo(2.508F);
            else if(checkblock == Blocks.AIR)return new WeightInfo(6F);// Where to put Placing logic?
            else return new WeightInfo(4.317F);
        }
        else if(WhichPredicament==1){
            WeightInfo temp = new WeightInfo();
            if(checkblock == Blocks.AIR){
                temp.BottomBlock = new AirBreaking();
            }
            else {
                temp.BottomBlock = getBestInventoryItemForBlock(Player.getInventory(), block, blockPos);
            }
            return temp;
        }else if(WhichPredicament==2){
            WeightInfo temp = new WeightInfo();
            if(checkblock == Blocks.AIR){
                temp.TopBlock = new AirBreaking();
            }else {
                temp.TopBlock = getBestInventoryItemForBlock(Player.getInventory(), block, blockPos);
            }
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
        public ItemBlockBreakingSpeed(){

        }

        public float getSimpleSpeed(){
            int i = canHarvest(this.Blck,this.Item) ? 30 : 100;
            float h = this.Blck.getHardness(World,Pos);
            float f = this.Item.getMiningSpeedMultiplier(this.Blck);
            if (f > 1.0F) {
                f += (float)this.Player.getAttributeValue(EntityAttributes.MINING_EFFICIENCY);
            }
            if (h == 0F) return 0;
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
    public class AirBreaking extends  ItemBlockBreakingSpeed{
        public AirBreaking() {

        }

        @Override
        public float getFullSpeed() {
            return 0F;
        }
        @Override
        public float getSimpleSpeed() {
            return 0F;
        }
    }

    public boolean canHarvest(@NotNull BlockState state, ItemStack stack) {
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

        public boolean lessThan(@NotNull WeightInfo other){
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
        public boolean isUnattainable(){
            return false;
        }
    }
    public static class StarterWeight extends WeightInfo{
        @Override
        public float Total() {
            return 0.0F;
        }
    }

    public static class WorstWeight extends WeightInfo{
        @Override
        public float Total() {
            return Float.MAX_VALUE;
        }
    }
    public static class UnattainableWeight extends WorstWeight{
        @Override
        public boolean isUnattainable() {
            return true;
        }
    }
}
