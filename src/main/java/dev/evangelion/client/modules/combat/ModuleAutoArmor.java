package dev.evangelion.client.modules.combat;

import dev.evangelion.Evangelion;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.Item;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import dev.evangelion.client.modules.client.ModuleMiddleClick;
import net.minecraft.init.Items;
import dev.evangelion.api.utilities.DamageUtils;
import net.minecraft.client.gui.GuiScreen;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "AutoArmor", tag = "Auto Armor", description = "Automatically put armor on.", category = Module.Category.COMBAT)
public class ModuleAutoArmor extends Module
{
    ValueNumber delay;
    ValueBoolean elytraPriority;
    ValueBoolean autoMend;
    ValueNumber armorHP;
    ValueNumber enemyRange;
    ValueNumber pearlRange;
    private TimerUtils cooldown;
    
    public ModuleAutoArmor() {
        this.delay = new ValueNumber("Delay", "Delay", "Delay to put armor on.", 100, 0, 500);
        this.elytraPriority = new ValueBoolean("ElytraPriority", "Elytra Priority", "Prioritize elytra if it is equipped.", false);
        this.autoMend = new ValueBoolean("AutoMend", "Auto Mend", "Automatically remove armor when mending.", false);
        this.armorHP = new ValueNumber("ArmorHeath", "Armor Health", "Health for armor to remove it when mending.", 70.0f, 20.0f, 100.0f);
        this.enemyRange = new ValueNumber("EnemyRange", "Enemy Range", "Put on armor if enemy is near.", 7.0f, 0.0f, 20.0f);
        this.pearlRange = new ValueNumber("PearlRange", "Pearl Range", "Put on armor if pear is near.", 7.0f, 0.0f, 20.0f);
        this.cooldown = new TimerUtils();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck() || ModuleAutoArmor.mc.currentScreen instanceof GuiScreen) {
            return;
        }
        final ItemStack helmet = ModuleAutoArmor.mc.player.inventory.getStackInSlot(39);
        final ItemStack chestplate = ModuleAutoArmor.mc.player.inventory.getStackInSlot(38);
        final ItemStack pants = ModuleAutoArmor.mc.player.inventory.getStackInSlot(37);
        final ItemStack boots = ModuleAutoArmor.mc.player.inventory.getStackInSlot(36);
        final boolean allGood = DamageUtils.getRoundedDamage(helmet) >= this.armorHP.getValue().floatValue() && DamageUtils.getRoundedDamage(chestplate) >= this.armorHP.getValue().floatValue() && DamageUtils.getRoundedDamage(pants) >= this.armorHP.getValue().floatValue() && DamageUtils.getRoundedDamage(boots) >= this.armorHP.getValue().floatValue();
        if (this.autoMend.getValue() && this.isSafe() && ((ModuleAutoArmor.mc.player.getHeldItemMainhand().getItem().equals(Items.EXPERIENCE_BOTTLE) && ModuleAutoArmor.mc.gameSettings.keyBindUseItem.isKeyDown()) || ModuleMiddleClick.INSTANCE.xping) && !allGood) {
            this.saveArmor(helmet, 5);
            this.saveArmor(chestplate, 6);
            this.saveArmor(pants, 7);
            this.saveArmor(boots, 8);
        }
        else {
            this.updateArmor(helmet.getItem(), EntityEquipmentSlot.HEAD, 5);
            if (!this.elytraPriority.getValue() || chestplate.getItem() != Items.ELYTRA) {
                this.updateArmor(chestplate.getItem(), EntityEquipmentSlot.CHEST, 6);
            }
            this.updateArmor(pants.getItem(), EntityEquipmentSlot.LEGS, 7);
            this.updateArmor(boots.getItem(), EntityEquipmentSlot.FEET, 8);
        }
    }
    
    public void saveArmor(final ItemStack stack, final int armorSlot) {
        final ArrayList<Integer> emptySlots = (ArrayList<Integer>)this.emptySlots();
        if (!(stack.getItem() instanceof ItemAir) && DamageUtils.getRoundedDamage(stack) >= this.armorHP.getValue().floatValue() && !emptySlots.isEmpty()) {
            ModuleAutoArmor.mc.playerController.windowClick(ModuleAutoArmor.mc.player.inventoryContainer.windowId, armorSlot, 0, ClickType.QUICK_MOVE, (EntityPlayer)ModuleAutoArmor.mc.player);
        }
    }
    
    public void updateArmor(final Item item, final EntityEquipmentSlot type, final int newSlot) {
        if (item instanceof ItemAir || !(item instanceof ItemArmor)) {
            final int newItem = this.getArmorSlot(type);
            if (newItem != -1) {
                this.moveItem(newItem, newSlot);
            }
        }
    }
    
    public int getArmorSlot(final EntityEquipmentSlot type) {
        for (int i = 0; i < 36; ++i) {
            final ItemStack stack = ModuleAutoArmor.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() instanceof ItemArmor) {
                if (((ItemArmor)stack.getItem()).armorType.equals((Object)type)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public ArrayList emptySlots() {
        final ArrayList<Integer> emptySlots = new ArrayList<Integer>();
        for (int i = 0; i < 36; ++i) {
            final ItemStack stack = ModuleAutoArmor.mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty || stack.getItem() == Items.AIR) {
                emptySlots.add(i);
            }
        }
        return emptySlots;
    }
    
    public void moveItem(final int slot, final int newSlot) {
        if (this.cooldown.hasTimeElapsed(this.delay.getValue().intValue())) {
            ModuleAutoArmor.mc.playerController.windowClick(ModuleAutoArmor.mc.player.inventoryContainer.windowId, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ModuleAutoArmor.mc.player);
            ModuleAutoArmor.mc.playerController.windowClick(ModuleAutoArmor.mc.player.inventoryContainer.windowId, newSlot, 0, ClickType.PICKUP, (EntityPlayer)ModuleAutoArmor.mc.player);
            ModuleAutoArmor.mc.playerController.windowClick(ModuleAutoArmor.mc.player.inventoryContainer.windowId, (slot < 9) ? (slot + 36) : slot, 0, ClickType.PICKUP, (EntityPlayer)ModuleAutoArmor.mc.player);
            ModuleAutoArmor.mc.playerController.updateController();
            this.cooldown.reset();
        }
    }
    
    public boolean isSafe() {
        final EntityLivingBase target = getTarget(this.enemyRange.getValue().floatValue());
        final EntityEnderPearl pearl = getPearl(this.pearlRange.getValue().floatValue());
        return target == null && pearl == null;
    }
    
    public static EntityEnderPearl getPearl(final float range) {
        EntityEnderPearl targetPearl = null;
        for (final Entity e : ModuleAutoArmor.mc.world.loadedEntityList) {
            if (!(e instanceof EntityEnderPearl)) {
                continue;
            }
            final EntityEnderPearl pearl = (EntityEnderPearl)e;
            if (ModuleAutoArmor.mc.player.getDistance(pearl.posX, pearl.posY, pearl.posZ) > range) {
                continue;
            }
            if (targetPearl == null) {
                targetPearl = pearl;
            }
            else {
                if (ModuleAutoArmor.mc.player.getDistance(pearl.posX, pearl.posY, pearl.posZ) >= ModuleAutoArmor.mc.player.getDistance(targetPearl.posX, targetPearl.posY, targetPearl.posZ)) {
                    continue;
                }
                targetPearl = pearl;
            }
        }
        return targetPearl;
    }
    
    public static EntityLivingBase getTarget(final float range) {
        EntityLivingBase targetEntity = null;
        for (final Entity e : new ArrayList<Entity>(ModuleAutoArmor.mc.world.loadedEntityList)) {
            if (!(e instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase entity = (EntityLivingBase)e;
            if (ModuleAutoArmor.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) > range) {
                continue;
            }
            if (Evangelion.FRIEND_MANAGER.isFriend(e.getName())) {
                continue;
            }
            if (entity == ModuleAutoArmor.mc.player && entity.getName().equals(ModuleAutoArmor.mc.player.getName())) {
                continue;
            }
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            if (entity.isDead) {
                continue;
            }
            if (entity.getHealth() <= 0.0f) {
                continue;
            }
            if (targetEntity == null) {
                targetEntity = entity;
            }
            else {
                if (ModuleAutoArmor.mc.player.getDistance(entity.posX, entity.posY, entity.posZ) >= ModuleAutoArmor.mc.player.getDistance(targetEntity.posX, targetEntity.posY, targetEntity.posZ)) {
                    continue;
                }
                targetEntity = entity;
            }
        }
        return targetEntity;
    }
}
