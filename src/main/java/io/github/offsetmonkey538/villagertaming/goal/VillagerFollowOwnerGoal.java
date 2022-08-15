package io.github.offsetmonkey538.villagertaming.goal;

import io.github.offsetmonkey538.villagertaming.mixin.VillagerEntityMixin;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class VillagerFollowOwnerGoal extends Goal {
    public static final int TELEPORT_DISTANCE = 12;
    private static final int HORIZONTAL_RANGE = 2;
    private static final int HORIZONTAL_VARIATION = 3;
    private static final int VERTICAL_VARIATION = 1;
    private final VillagerEntity entity;
    private final Tameable tameable;
    private LivingEntity owner;
    private final WorldView world;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPathfindingPenalty;
    private final boolean leavesAllowed;

    public VillagerFollowOwnerGoal(VillagerEntity entity, Tameable tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed) {
        this.entity = entity;
        this.tameable = tameable;
        this.world = entity.world;
        this.speed = speed;
        this.navigation = entity.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        if (!(entity.getNavigation() instanceof MobNavigation) && !(entity.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = (LivingEntity) this.tameable.getOwner();
        if (livingEntity == null) {
            return false;
        }
        if (livingEntity.isSpectator()) {
            return false;
        }
        // if (this.entity.isSitting()) {
        //     return false;
        // }
        if (this.entity.squaredDistanceTo(livingEntity) < (double)(this.minDistance * this.minDistance)) {
            return false;
        }
        this.owner = livingEntity;
        return true;
    }

    @Override
    public boolean shouldContinue() {
        if (this.navigation.isIdle()) {
            return false;
        }
        // if (this.entity.isSitting()) {
        //     return false;
        // }
        return !(this.entity.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
    }

    @Override
    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.entity.getPathfindingPenalty(PathNodeType.WATER);
        this.entity.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.owner = null;
        this.navigation.stop();
        this.entity.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
    }

    @Override
    public void tick() {
        this.entity.getLookControl().lookAt(this.owner, 10.0f, this.entity.getMaxLookPitchChange());
        if (--this.updateCountdownTicks > 0) {
            return;
        }
        this.updateCountdownTicks = this.getTickCount(10);
        if (this.entity.isLeashed() || this.entity.hasVehicle()) {
            return;
        }
        if (this.entity.squaredDistanceTo(this.owner) >= 144.0) {
            this.tryTeleport();
        } else {
            this.navigation.startMovingTo(this.owner, this.speed);
        }
    }

    private void tryTeleport() {
        BlockPos blockPos = this.owner.getBlockPos();
        for (int i = 0; i < 10; ++i) {
            int j = this.getRandomInt(-3, 3);
            int k = this.getRandomInt(-1, 1);
            int l = this.getRandomInt(-3, 3);
            boolean bl = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
            if (!bl) continue;
            return;
        }
    }

    private boolean tryTeleportTo(int x, int y, int z) {
        if (Math.abs((double)x - this.owner.getX()) < 2.0 && Math.abs((double)z - this.owner.getZ()) < 2.0) {
            return false;
        }
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        this.entity.refreshPositionAndAngles((double)x + 0.5, y, (double)z + 0.5, this.entity.getYaw(), this.entity.getPitch());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
        if (pathNodeType != PathNodeType.WALKABLE) {
            return false;
        }
        BlockState blockState = this.world.getBlockState(pos.down());
        if (!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock) {
            return false;
        }
        BlockPos blockPos = pos.subtract(this.entity.getBlockPos());
        return this.world.isSpaceEmpty(this.entity, this.entity.getBoundingBox().offset(blockPos));
    }

    private int getRandomInt(int min, int max) {
        return this.entity.getRandom().nextInt(max - min + 1) + min;
    }
}
