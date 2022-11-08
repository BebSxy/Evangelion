package dev.evangelion.client.modules.visuals;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Animations", description = "Let's you change swing speed and hand progress.", category = Module.Category.VISUALS)
public class ModuleAnimations extends Module
{
    private final ValueBoolean playerAnimations;
    private final ValueCategory handCategory;
    private final ValueBoolean changeMainhand;
    private final ValueNumber mainhand;
    private final ValueBoolean changeOffhand;
    private final ValueNumber offhand;
    public final ValueBoolean changeSwing;
    public final ValueNumber swingSpeed;
    public static ModuleAnimations INSTANCE;
    
    public ModuleAnimations() {
        this.playerAnimations = new ValueBoolean("PlayerAnimations", "Player Animations", "Makes it so that you can disable limb rotation for players.", true);
        this.handCategory = new ValueCategory("Hands", "The category for customizing hands.");
        this.changeMainhand = new ValueBoolean("ChangeMainhand", "Mainhand", "Let's you change mainhand progress.", this.handCategory, true);
        this.mainhand = new ValueNumber("Mainhand", "Mainhand", "The progress for the Mainhand.", this.handCategory, 1.0f, 0.0f, 1.0f);
        this.changeOffhand = new ValueBoolean("ChangeOffhand", "Offhand", "Let's you change offhand progress.", this.handCategory, true);
        this.offhand = new ValueNumber("Offhand", "Offhand", "The progress for the Offhand.", this.handCategory, 1.0f, 0.0f, 1.0f);
        this.changeSwing = new ValueBoolean("ChangeSwingSpeed", "Swing Speed", "Let's you change swing speed.", this.handCategory, true);
        this.swingSpeed = new ValueNumber("SwingSpeed", "Swing Speed", "The speed for the swinging.", this.handCategory, 6, 1, 20);
        ModuleAnimations.INSTANCE = this;
    }
    
    @Override
    public void onTick() {
        super.onTick();
        if (!this.playerAnimations.getValue()) {
            for (final EntityPlayer player : ModuleAnimations.mc.world.playerEntities) {
                player.limbSwing = 0.0f;
                player.limbSwingAmount = 0.0f;
                player.prevLimbSwingAmount = 0.0f;
            }
        }
        if (this.changeMainhand.getValue() && ModuleAnimations.mc.entityRenderer.itemRenderer.equippedProgressMainHand != this.mainhand.getValue().floatValue()) {
            ModuleAnimations.mc.entityRenderer.itemRenderer.equippedProgressMainHand = this.mainhand.getValue().floatValue();
            ModuleAnimations.mc.entityRenderer.itemRenderer.itemStackMainHand = ModuleAnimations.mc.player.getHeldItemMainhand();
        }
        if (this.changeOffhand.getValue() && ModuleAnimations.mc.entityRenderer.itemRenderer.equippedProgressOffHand != this.offhand.getValue().floatValue()) {
            ModuleAnimations.mc.entityRenderer.itemRenderer.equippedProgressOffHand = this.offhand.getValue().floatValue();
            ModuleAnimations.mc.entityRenderer.itemRenderer.itemStackOffHand = ModuleAnimations.mc.player.getHeldItemOffhand();
        }
    }
}
