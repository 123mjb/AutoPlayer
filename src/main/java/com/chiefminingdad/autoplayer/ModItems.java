package com.chiefminingdad.autoplayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.function.Function;

public abstract class ModItems extends Item{
	public ModItems(Settings settings) {
		super(settings);
	}

	public static Item register(String name, Function<Item.Settings, Item> ItemFactory, Item.Settings settings) {
		// Create the item key.
		RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(AutoPlayer.MOD_ID, name));

		// Create the item instance.
		Item item = ItemFactory.apply(settings.registryKey(itemKey));

		// Register the item.
		Registry.register(Registries.ITEM,itemKey,item);

		return item;
	}

	// public static final Item SUSPICIOUS_SUBSTANCE = register("suspicious_substance", Item::new, new Item.Settings());

	public static void initialize() {
		// Get the event for modifying entries in the ingredients group.
		// And register an event handler that adds our suspicious item to the ingredients group.
		//ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
		//		.register((itemGroup) -> itemGroup.add(ModItems.SUSPICIOUS_SUBSTANCE));
	}

	public abstract ActionResult use(World world, PlayerEntity user, Hand hand);
}
