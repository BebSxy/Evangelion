/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.SharedMonsterAttributes
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.item.ItemTool
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.CombatRules
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 */
package dev.evangelion.api.utilities;

import dev.evangelion.api.utilities.IMinecraft;
import java.util.ConcurrentModificationException;
import java.util.Objects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class DamageUtils
implements IMinecraft {
    public static float calculateDamage(double posX, double posY, double posZ, EntityLivingBase entity) {
        try {
            double distance = entity.getDistance(posX, posY, posZ) / 12.0;
            double value = (1.0 - distance) * (double)DamageUtils.mc.world.getBlockDensity(new Vec3d(posX, posY, posZ), entity.getEntityBoundingBox());
            float damage = (float)((int)((value * value + value) / 2.0 * 7.0 * 12.0 + 1.0)) * (DamageUtils.mc.world.getDifficulty().getId() == 0 ? 0.0f : (DamageUtils.mc.world.getDifficulty().getId() == 2 ? 1.0f : (DamageUtils.mc.world.getDifficulty().getId() == 1 ? 0.5f : 1.5f)));
            damage = CombatRules.getDamageAfterAbsorb((float)damage, (float)entity.getTotalArmorValue(), (float)((float)entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue()));
            damage *= 1.0f - MathHelper.clamp((float)EnchantmentHelper.getEnchantmentModifierDamage((Iterable)entity.getArmorInventoryList(), (DamageSource)DamageSource.causeExplosionDamage((Explosion)new Explosion((World)DamageUtils.mc.world, null, posX, posY, posZ, 6.0f, false, true))), (float)0.0f, (float)20.0f) / 25.0f;
            if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById((int)11)))) {
                damage -= damage / 4.0f;
            }
            return damage;
        }
        catch (NullPointerException | ConcurrentModificationException exception) {
            return 0.0f;
        }
    }

    public static boolean hasDurability(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    public static int getRoundedDamage(ItemStack stack) {
        return (int)((float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f);
    }

    public static boolean shouldBreakArmor(EntityLivingBase entity, float targetPercent) {
        for (ItemStack stack : entity.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) {
                return true;
            }
            float armorPercent = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0f;
            if (!(targetPercent >= armorPercent) || stack.stackSize >= 2) continue;
            return true;
        }
        return false;
    }
}

