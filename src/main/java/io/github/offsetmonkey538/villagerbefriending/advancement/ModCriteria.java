package io.github.offsetmonkey538.villagerbefriending.advancement;

import net.minecraft.advancement.criterion.Criteria;

public final class ModCriteria {
    private ModCriteria() {

    }

    public static final VillagerBefriendedCriterion VILLAGER_BEFRIENDED = Criteria.register(new VillagerBefriendedCriterion());

    public static void register() {
        // Loads everything because the class gets loaded
    }
}
