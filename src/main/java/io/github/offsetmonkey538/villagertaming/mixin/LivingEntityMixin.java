package io.github.offsetmonkey538.villagertaming.mixin;

import io.github.offsetmonkey538.villagertaming.entity.IVillagerData;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
        method = "isImmobile",
        at = @At("TAIL"),
        cancellable = true
    )
    @SuppressWarnings("ConstantConditions")
    public void villagertaming$makeStandingVillagersImmobile(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof VillagerEntity villagerEntity) {
            if (((IVillagerData)villagerEntity).isStanding()) {
                cir.setReturnValue(true);
            }
        }
    }
}
