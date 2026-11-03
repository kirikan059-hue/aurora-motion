package dev.auroramotion;

import java.util.Map;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

public final class AuroraArmor {
    private AuroraArmor() {}

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(AuroraMotion.MOD_ID, path);
    }

    // assets/aurora_motion/equipment/aurora.json
    public static final ResourceKey<EquipmentAsset> ASSET =
            ResourceKey.create(EquipmentAssets.ROOT_ID, id("aurora"));

    // data/aurora_motion/tags/item/repairs_aurora_armor.json
    public static final TagKey<Item> REPAIR =
            TagKey.create(Registries.ITEM, id("repairs_aurora_armor"));

    public static final int BASE_DURABILITY = 42;

    public static final ArmorMaterial MATERIAL = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 3,
                    ArmorType.CHESTPLATE, 8,
                    ArmorType.LEGGINGS, 6,
                    ArmorType.BOOTS, 3),
            25,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            2.0F,
            0.0F,
            REPAIR,
            ASSET);

    public static final Item HELMET = register("aurora_helmet", ArmorType.HELMET);
    public static final Item CHESTPLATE = register("aurora_chestplate", ArmorType.CHESTPLATE);
    public static final Item LEGGINGS = register("aurora_leggings", ArmorType.LEGGINGS);
    public static final Item BOOTS = register("aurora_boots", ArmorType.BOOTS);

    private static Item register(String name, ArmorType type) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, id(name));
        Item item = new Item(new Item.Properties()
                .setId(key)
                .rarity(Rarity.EPIC)
                .humanoidArmor(MATERIAL, type)
                .durability(type.getDurability(BASE_DURABILITY)));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    public static void init() {
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(output -> {
            output.accept(HELMET);
            output.accept(CHESTPLATE);
            output.accept(LEGGINGS);
            output.accept(BOOTS);
        });
    }
}
