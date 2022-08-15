package io.github.offsetmonkey538.villagertaming.mixin;

import io.github.offsetmonkey538.villagertaming.entity.IVillagerData;
import io.github.offsetmonkey538.villagertaming.entity.goal.VillagerFollowOwnerGoal;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MobEntity implements IVillagerData {

    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    @Unique
    private static Item TAMING_ITEM; //TODO: Add custom item made by combining emeralds, diamonds, and gold. Maybe some other valuable stuff too

    protected VillagerEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableSet;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;[Ljava/lang/Object;)Lcom/google/common/collect/ImmutableSet;"
        ),
        index = 6
    )
    @SuppressWarnings("unchecked")
    private static <E> E[] changeGatherableItems(E[] original) {
        TAMING_ITEM = Items.APPLE;

        E[] result = Arrays.copyOf(original, original.length + 1);
        result[original.length] = (E) TAMING_ITEM;
        return result;
    }

    @Inject(
        method = "loot",
        at = @At("HEAD")
    )
    private void loot(ItemEntity item, CallbackInfo ci) {
        if (!item.getStack().isOf(TAMING_ITEM)) return;
        tame(item.getThrower());
    }

    /*
        Code for taming starts here
    */

    @Inject(
        method = "initDataTracker",
        at = @At("TAIL")
    )
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    @Inject(
        method = "writeCustomDataToNbt",
        at = @At("TAIL")
    )
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("Owner", this.getOwnerUuid());
        }
    }

    @Inject(
        method = "readCustomDataFromNbt",
        at = @At("TAIL")
    )
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        UUID UUID;
        if (nbt.containsUuid("Owner")) {
            UUID = nbt.getUuid("Owner");
        } else {
            String string = nbt.getString("Owner");
            UUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        setOwnerUuid(UUID);
    }

    @Unique
    @Override
    protected void initGoals() {
        this.goalSelector.add(6, new VillagerFollowOwnerGoal((VillagerEntity) (Object)this, this, 0.5, 10.0f, 2.0f, false));
    }

    @Unique
    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Unique
    public void tame(@Nullable UUID player) {
        this.setOwnerUuid(player);
        this.world.sendEntityStatus((VillagerEntity)(Object)this, EntityStatuses.ADD_VILLAGER_HEART_PARTICLES);
    }

    @Unique
    @Override
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    @Unique
    @Override
    public LivingEntity getOwner() {
        try {
            UUID UUID = this.getOwnerUuid();
            if (UUID == null) {
                return null;
            }
            return this.world.getPlayerByUuid(UUID);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}
