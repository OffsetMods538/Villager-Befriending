package io.github.offsetmonkey538.villagerbefriending.advancement;

import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;

public class EmptyConditions extends AbstractCriterionConditions {
    public EmptyConditions(Identifier id, EntityPredicate.Extended entity) {
        super(id, entity);
    }
}
