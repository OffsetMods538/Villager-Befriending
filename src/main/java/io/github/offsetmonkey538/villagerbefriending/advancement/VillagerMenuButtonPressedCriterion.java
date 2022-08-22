package io.github.offsetmonkey538.villagerbefriending.advancement;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static io.github.offsetmonkey538.villagerbefriending.entrypoint.VillagerBefriendingMain.MOD_ID;
import static io.github.offsetmonkey538.villagerbefriending.screen.tamedvillager.Buttons.*;

public class VillagerMenuButtonPressedCriterion extends AbstractCriterion<VillagerMenuButtonPressedCriterion.Conditions> {
    private static final Identifier ID = new Identifier(MOD_ID, "villager_menu_button_pressed");

    public void trigger(ServerPlayerEntity player, int buttonId) {
        this.trigger(player, conditions -> conditions.matches(buttonId));
    }

    @Override
    protected VillagerMenuButtonPressedCriterion.Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended player, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        final String button = obj.get("button").getAsString();
        final int buttonId = switch (button) {
            case "stand" -> STAND;
            case "follow" -> FOLLOW;
            case "wander" -> WANDER;
            default -> throw new JsonSyntaxException(String.format("Unknown button id '%s'", button));
        };
        return new VillagerMenuButtonPressedCriterion.Conditions(player, buttonId);
    }

    @Override
    public Identifier getId() {
        return ID;
    }


    public static class Conditions extends AbstractCriterionConditions {
        private final int buttonId;

        public Conditions(EntityPredicate.Extended entity, int buttonId) {
            super(ID, entity);
            this.buttonId = buttonId;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);

            jsonObject.addProperty("button",
                switch (this.buttonId) {
                    case STAND -> "stand";
                    case FOLLOW -> "follow";
                    case WANDER -> "wander";
                    default -> throw new JsonSyntaxException(String.format("Unknown button id '%s'", buttonId));
                }
            );

            return jsonObject;
        }

        public boolean matches(int buttonId) {
            return this.buttonId == buttonId;
        }
    }
}
