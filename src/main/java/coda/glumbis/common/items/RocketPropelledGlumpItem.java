package coda.glumbis.common.items;

import coda.glumbis.common.entities.RocketPropelledGlumpEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RocketPropelledGlumpItem extends Item {

    public RocketPropelledGlumpItem(Properties p_41383_) {
        super(p_41383_);
    }

    public RocketPropelledGlumpEntity createRPG(Level level, LivingEntity entity) {
        RocketPropelledGlumpEntity glump = new RocketPropelledGlumpEntity(entity, level);
        return glump;
    }

}
