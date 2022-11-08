package dev.evangelion.client.modules.visuals;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.entity.effect.EntityLightningBolt;
import dev.evangelion.client.events.EventDeath;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "KillEffects", tag = "Kill Effects", description = "Render a nice effect when you kill someone.", category = Module.Category.VISUALS)
public class ModuleKillEffects extends Module
{
    ValueBoolean lightning;
    ValueBoolean lightningSound;
    
    public ModuleKillEffects() {
        this.lightning = new ValueBoolean("Lightning", "Lightning", "", false);
        this.lightningSound = new ValueBoolean("LightningSound", "LightningSound", "", false);
    }
    
    @SubscribeEvent
    public void onEntityDeath(final EventDeath event) {
        if (ModuleKillEffects.mc.player == null || ModuleKillEffects.mc.world == null) {
            return;
        }
        if (this.lightning.getValue()) {
            final EntityLightningBolt bolt = new EntityLightningBolt((World)ModuleKillEffects.mc.world, 0.0, 0.0, 0.0, false);
            if (this.lightningSound.getValue()) {
                ModuleKillEffects.mc.world.playSound(event.getEntity().getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 1.0f, 1.0f, false);
            }
            bolt.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, 0.0f, 0.0f);
            ModuleKillEffects.mc.world.spawnEntity((Entity)bolt);
        }
    }
}
