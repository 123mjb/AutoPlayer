package com.chiefminingdad.autoplayer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class DND extends ModItems {
    public DND(net.minecraft.item.Item.Settings settings) {
        super(settings);
    }
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.playSound(SoundEvents.ENTITY_SNIFFER_SNIFFING, 1.0F, 1.0F);
        user.playSound(SoundEvents.ENTITY_FOX_SNIFF, 1.0F, 1.0F);
        user.playSound(SoundEvents.ENTITY_WARDEN_SNIFF, 1.0F, 1.0F);
        return ActionResult.SUCCESS;
    }

    public static final DND SUSPICIOUS_SUBSTANCE= (DND) register("suspicious_substance", DND::new, new net.minecraft.item.Item.Settings());

    public static void initialize() {
        // Get the event for modifying entries in the ingredients group.
        // And register an event handler that adds our suspicious item to the ingredients group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(DND.SUSPICIOUS_SUBSTANCE));
    }
}
