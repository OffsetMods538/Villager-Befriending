package io.github.offsetmonkey538.villagertaming.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import org.jetbrains.annotations.Nullable;

public interface IVillagerData extends Tameable {

    @Override
    @Nullable
    LivingEntity getOwner();

    boolean isStanding();

    void setStanding(boolean value);
}
