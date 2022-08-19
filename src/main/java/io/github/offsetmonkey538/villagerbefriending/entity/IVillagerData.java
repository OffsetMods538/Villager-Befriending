package io.github.offsetmonkey538.villagerbefriending.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import org.jetbrains.annotations.Nullable;

public interface IVillagerData extends Tameable {

    /*
        Getters
    */

    @Override
    @Nullable
    LivingEntity getOwner();

    boolean isStanding();

    boolean isFollowingOwner();


    /*
        Setters
    */

    void setStanding(boolean value);

    void setFollowingOwner(boolean value);
}
