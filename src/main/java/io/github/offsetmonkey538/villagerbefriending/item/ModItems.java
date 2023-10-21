package io.github.offsetmonkey538.villagerbefriending.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.*;

public class ModItems {

    public static final TotemOfBefriendingItem TOTEM_OF_BEFRIENDING_ITEM = register(
        new TotemOfBefriendingItem(new FabricItemSettings()
            .rarity(Rarity.RARE)
            .maxCount(1)),
        "totem_of_befriending");

    private static <T extends Item> T register(T item, String id) {
        return Registry.register(Registries.ITEM, new Identifier(MOD_ID, id), item);
    }

    public static void register() {
        // Loads all items because the class gets loaded

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(TOTEM_OF_BEFRIENDING_ITEM));
    }
}
