package io.github.offsetmonkey538.villagerbefriending.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;

public class ModItems {

    public static final TotemOfBefriendingItem TOTEM_OF_BEFRIENDING_ITEM = register(
        new TotemOfBefriendingItem(new FabricItemSettings()
            .group(ItemGroup.MISC)
            .rarity(Rarity.RARE)
            .maxCount(1)),
        "totem_of_befriending");

    // Advancement icon
    public static final Item HEART = register(new Item(new FabricItemSettings()), "heart");

    private static <T extends Item> T register(T item, String id) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, id), item);
    }

    public static void register() {
        // Loads all items because the class gets loaded
    }
}
