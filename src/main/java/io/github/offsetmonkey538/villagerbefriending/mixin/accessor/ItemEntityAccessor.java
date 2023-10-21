package io.github.offsetmonkey538.villagerbefriending.mixin.accessor;

import java.util.UUID;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {

    @Accessor
    UUID getThrower();
}
