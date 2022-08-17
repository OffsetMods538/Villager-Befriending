package io.github.offsetmonkey538.villagertaming.entity.goal;

import io.github.offsetmonkey538.villagertaming.entity.IVillagerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.passive.VillagerEntity;

public abstract class AbstractVillagerGoal extends Goal {
    protected final VillagerEntity entity;
    protected final IVillagerData villagerData;
    protected final EntityNavigation navigation;
    protected LivingEntity owner;

    public AbstractVillagerGoal(VillagerEntity entity, IVillagerData villagerData) {
        this.entity = entity;
        this.villagerData = villagerData;
        this.navigation = entity.getNavigation();
    }
}
