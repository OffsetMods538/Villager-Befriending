package io.github.offsetmonkey538.villagerbefriending.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;

public class VillagerBefriendedCriterion extends AbstractCriterion<EmptyConditions> {
    private static final Identifier ID = new Identifier(MOD_ID, "villager_befriended");

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    protected EmptyConditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended player, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new EmptyConditions(ID, player);
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
