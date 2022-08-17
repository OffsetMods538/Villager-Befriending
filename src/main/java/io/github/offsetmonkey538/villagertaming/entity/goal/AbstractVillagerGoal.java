package io.github.offsetmonkey538.villagertaming.entity.goal;

import io.github.offsetmonkey538.villagertaming.entity.IVillagerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.VillagerEntity;

public abstract class AbstractVillagerGoal extends Goal {
    protected final VillagerEntity villager;
    protected final IVillagerData villagerData;
    protected final EntityNavigation navigation;
    protected LivingEntity owner;

    public AbstractVillagerGoal(VillagerEntity villager) {
        this.villager = villager;
        this.villagerData = (IVillagerData) villager;
        this.navigation = villager.getNavigation();
    }
}
