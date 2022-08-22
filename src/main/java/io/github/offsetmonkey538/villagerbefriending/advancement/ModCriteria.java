package io.github.offsetmonkey538.villagerbefriending.advancement;

import net.minecraft.advancement.criterion.Criteria;

public final class ModCriteria {
    private ModCriteria() {

    }

    public static final VillagerBefriendedCriterion VILLAGER_BEFRIENDED = Criteria.register(new VillagerBefriendedCriterion());
    public static final VillagerMenuButtonPressedCriterion VILLAGER_MENU_BUTTON_PRESSED = Criteria.register(new VillagerMenuButtonPressedCriterion());

    public static void register() {
        // Loads everything because the class gets loaded
    }
}
