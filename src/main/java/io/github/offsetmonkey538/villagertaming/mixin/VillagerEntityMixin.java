package io.github.offsetmonkey538.villagertaming.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
public abstract class VillagerEntityMixin extends Entity implements Tameable {

    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);

    @Unique
    private static Item TAMING_ITEM;

    public VillagerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
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
        tame(item.getOwner());
    }

    /*
        Code for taming things starts here
     */

    @Inject(
        method = "initDataTracker",
        at = @At("TAIL")
    )
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }


    @Unique
    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Unique
    public void tame(@Nullable UUID player) {
        this.setOwnerUuid(player);
        this.world.sendEntityStatus(this, EntityStatuses.ADD_POSITIVE_PLAYER_REACTION_PARTICLES);
    }

    @Unique
    @Override
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    @Unique
    @Override
    public Entity getOwner() {
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
