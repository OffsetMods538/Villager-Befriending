package io.github.offsetmonkey538.villagerbefriending.mixin;

import io.github.offsetmonkey538.villagerbefriending.advancement.ModCriteria;
import io.github.offsetmonkey538.villagerbefriending.entity.IVillagerData;
import io.github.offsetmonkey538.villagerbefriending.entity.goal.VillagerFollowOwnerGoal;
import io.github.offsetmonkey538.villagerbefriending.item.ModItems;
import io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.TamedVillagerScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MobEntity implements IVillagerData {

    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Boolean> STANDING = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final TrackedData<Boolean> FOLLOWING_OWNER = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Unique
    private static Item TAMING_ITEM;

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
        TAMING_ITEM = ModItems.TOTEM_OF_BEFRIENDING_ITEM;

        E[] result = Arrays.copyOf(original, original.length + 1);
        result[original.length] = (E) TAMING_ITEM;
        return result;
    }

    @Inject(
        method = "loot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void tame(ItemEntity item, CallbackInfo ci) {
        if (!item.getStack().isOf(TAMING_ITEM)) return;
        PlayerEntity player = item.getWorld().getPlayerByUuid(item.getThrower());
        if (player == null || hasOwner() || isBaby() || !player.hasStatusEffect(StatusEffects.HERO_OF_THE_VILLAGE)) {
            ci.cancel();
            return;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) ModCriteria.VILLAGER_BEFRIENDED.trigger(serverPlayer);
        this.setOwnerUuid(item.getThrower());
        this.setStanding(false);
        this.setFollowingOwner(true);
        this.world.sendEntityStatus((VillagerEntity)(Object)this, EntityStatuses.ADD_VILLAGER_HEART_PARTICLES);
    }

    @Inject(
        method = "interactMob",
        at = @At("HEAD"),
        cancellable = true
    )
    private void openScreen(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!player.equals(getOwner()) || !player.isSneaking()) return;
        player.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                buf.writeVarInt(VillagerEntityMixin.this.getId());
            }

            @Override
            public Text getDisplayName() {
                return Text.translatable(String.format("entity.%s.villager.command_menu", MOD_ID), VillagerEntityMixin.this.getName());
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                return new TamedVillagerScreenHandler(syncId, inv, (VillagerEntity) (Object) VillagerEntityMixin.this);
            }
        });
        cir.setReturnValue(ActionResult.success(this.world.isClient));
    }

    @Inject(
        method = "initDataTracker",
        at = @At("TAIL")
    )
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(STANDING, false);
        this.dataTracker.startTracking(FOLLOWING_OWNER, false);
    }

    @Inject(
        method = "writeCustomDataToNbt",
        at = @At("TAIL")
    )
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        final NbtCompound tamedVillagerData = new NbtCompound();

        if (this.hasOwner()) {
            tamedVillagerData.putUuid("Owner", this.getOwnerUuid());
        }
        tamedVillagerData.putBoolean("IsStanding", this.isStanding());
        tamedVillagerData.putBoolean("IsFollowingOwner", this.isFollowingOwner());

        nbt.put("TamedVillagerData", tamedVillagerData);
    }

    @Inject(
        method = "readCustomDataFromNbt",
        at = @At("TAIL")
    )
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("TamedVillagerData")) {
            final NbtCompound tamedVillagerData = nbt.getCompound("TamedVillagerData");

            UUID UUID;
            if (tamedVillagerData.containsUuid("Owner")) {
                UUID = tamedVillagerData.getUuid("Owner");
            } else {
                String string = tamedVillagerData.getString("Owner");
                UUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
            }
            setOwnerUuid(UUID);

            if (tamedVillagerData.contains("IsStanding")) {
                setStanding(tamedVillagerData.getBoolean("IsStanding"));
            }

            if (tamedVillagerData.contains("IsFollowingOwner")) {
                setFollowingOwner(tamedVillagerData.getBoolean("IsFollowingOwner"));
            }
            return;
        }
        setStanding(false);
        setFollowingOwner(false);
    }

    @Unique
    @Override
    protected void initGoals() {
        this.goalSelector.add(6, new VillagerFollowOwnerGoal((VillagerEntity) (Object)this, 0.5, 10.0f, 2.0f, false));
    }


    /*
        Getters
    */

    @Unique
    public boolean hasOwner() {
        return this.getOwnerUuid() != null;
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

    @Unique
    @Override
    public boolean isStanding() {
        return this.dataTracker.get(STANDING);
    }

    @Unique
    @Override
    public boolean isFollowingOwner() {
        return this.dataTracker.get(FOLLOWING_OWNER);
    }


    /*
        Setters
    */

    @Unique
    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Unique
    @Override
    public void setStanding(boolean value) {
        this.dataTracker.set(STANDING, value);
    }

    @Unique
    @Override
    public void setFollowingOwner(boolean value) {
        this.dataTracker.set(FOLLOWING_OWNER, value);
    }
}
