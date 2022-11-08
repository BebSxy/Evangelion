package dev.evangelion.client.modules.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.combat.ModulePopCounter;
import java.util.Objects;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.enchantment.Enchantment;
import dev.evangelion.api.utilities.DamageUtils;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemTool;
import org.lwjgl.opengl.GL11;
import dev.evangelion.api.utilities.ColorUtils;
import dev.evangelion.api.utilities.RenderUtils;
import java.awt.Color;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.Evangelion;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "NameTags", tag = "Name Tags", description = "Renders a name tag above players.", category = Module.Category.VISUALS)
public class ModuleNameTags extends Module
{
    public static ValueBoolean show_armor;
    public static ValueBoolean percent;
    public static ValueBoolean show_health;
    public static ValueBoolean show_ping;
    public static ValueBoolean show_totems;
    public static ValueBoolean show_invis;
    public static ValueBoolean gamemode;
    public static ValueBoolean entityID;
    public static ValueNumber m_scale;
    public static ValueNumber width;
    public static ValueBoolean outline;
    public static ValueColor color;
    int enchantIgriega;
    
    @Override
    public void onRender3D(final EventRender3D event) {
        for (final EntityPlayer player : ModuleNameTags.mc.world.playerEntities) {
            if (player != null && !player.equals((Object)ModuleNameTags.mc.player) && player.isEntityAlive() && (!player.isInvisible() || ModuleNameTags.show_invis.getValue())) {
                final double x = this.interpolate(player.lastTickPosX, player.posX, event.getPartialTicks()) - ModuleNameTags.mc.getRenderManager().renderPosX;
                final double y = this.interpolate(player.lastTickPosY, player.posY, event.getPartialTicks()) - ModuleNameTags.mc.getRenderManager().renderPosY;
                final double z = this.interpolate(player.lastTickPosZ, player.posZ, event.getPartialTicks()) - ModuleNameTags.mc.getRenderManager().renderPosZ;
                this.renderNameTag(player, x, y, z, event.getPartialTicks());
            }
        }
    }
    
    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final float delta) {
        double tempY = y;
        tempY += (player.isSneaking() ? 0.5 : 0.7);
        final Entity camera = ModuleNameTags.mc.getRenderViewEntity();
        assert camera != null;
        final double originalPositionX = camera.posX;
        final double originalPositionY = camera.posY;
        final double originalPositionZ = camera.posZ;
        camera.posX = this.interpolate(camera.prevPosX, camera.posX, delta);
        camera.posY = this.interpolate(camera.prevPosY, camera.posY, delta);
        camera.posZ = this.interpolate(camera.prevPosZ, camera.posZ, delta);
        final String displayTag = this.getDisplayTag(player);
        final double distance = camera.getDistance(x + ModuleNameTags.mc.getRenderManager().viewerPosX, y + ModuleNameTags.mc.getRenderManager().viewerPosY, z + ModuleNameTags.mc.getRenderManager().viewerPosZ);
        final int width = (int)Evangelion.FONT_MANAGER.getStringWidth(displayTag) / 2;
        double scale = 0.0018 + ModuleNameTags.m_scale.getValue().intValue() / 10000.0f * distance;
        if (distance <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.pushMatrix();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)tempY + 1.4f, (float)z);
        GlStateManager.rotate(-ModuleNameTags.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(ModuleNameTags.mc.getRenderManager().playerViewX, (ModuleNameTags.mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.enableBlend();
        RenderUtils.drawRect(-width - 2 - 1.0f, -(Evangelion.FONT_MANAGER.getHeight() + 1.0f) - 1.0f, width + 3.0f, 2.5f + (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? 0.5f : 0.0f), new Color(0, 0, 0, 90));
        if (ModuleNameTags.outline.getValue()) {
            final Color rainbowCol = ModuleNameTags.color.isRainbow() ? ColorUtils.rainbow(50) : ModuleNameTags.color.getValue();
            final Color rainbowCol2 = ModuleNameTags.color.isRainbow() ? ColorUtils.rainbow(2500) : ModuleNameTags.color.getValue();
            RenderUtils.prepare();
            GL11.glShadeModel(7425);
            GL11.glEnable(2848);
            GL11.glDisable(3553);
            GL11.glLineWidth((float)ModuleNameTags.width.getValue().doubleValue());
            GL11.glBegin(1);
            GL11.glColor3f(rainbowCol.getRed() / 255.0f, rainbowCol.getGreen() / 255.0f, rainbowCol.getBlue() / 255.0f);
            GL11.glVertex2d((double)(-width - 2 - 1.0f), (double)(-(Evangelion.FONT_MANAGER.getHeight() + 1.0f) - 1.0f));
            GL11.glColor3f(rainbowCol2.getRed() / 255.0f, rainbowCol2.getGreen() / 255.0f, rainbowCol2.getBlue() / 255.0f);
            GL11.glVertex2d((double)(width + 3.0f), (double)(-(Evangelion.FONT_MANAGER.getHeight() + 1.0f) - 1.0f));
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glColor3f(rainbowCol.getRed() / 255.0f, rainbowCol.getGreen() / 255.0f, rainbowCol.getBlue() / 255.0f);
            GL11.glVertex2d((double)(-width - 2 - 1.0f), (double)(-(Evangelion.FONT_MANAGER.getHeight() + 1.0f) - 1.0f));
            GL11.glColor3f(rainbowCol2.getRed() / 255.0f, rainbowCol2.getGreen() / 255.0f, rainbowCol2.getBlue() / 255.0f);
            GL11.glVertex2d((double)(-width - 2 - 1.0f), (double)(2.5f + (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? 0.5f : 0.0f)));
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glColor3f(rainbowCol2.getRed() / 255.0f, rainbowCol2.getGreen() / 255.0f, rainbowCol2.getBlue() / 255.0f);
            GL11.glVertex2d((double)(width + 3.0f), (double)(-(Evangelion.FONT_MANAGER.getHeight() + 1.0f) - 1.0f));
            GL11.glColor3f(rainbowCol.getRed() / 255.0f, rainbowCol.getGreen() / 255.0f, rainbowCol.getBlue() / 255.0f);
            GL11.glVertex2d((double)(width + 3.0f), (double)(2.5f + (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? 0.5f : 0.0f)));
            GL11.glEnd();
            GL11.glBegin(1);
            GL11.glColor3f(rainbowCol2.getRed() / 255.0f, rainbowCol2.getGreen() / 255.0f, rainbowCol2.getBlue() / 255.0f);
            GL11.glVertex2d((double)(-width - 2 - 1.0f), (double)(2.5f + (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? 0.5f : 0.0f)));
            GL11.glColor3f(rainbowCol.getRed() / 255.0f, rainbowCol.getGreen() / 255.0f, rainbowCol.getBlue() / 255.0f);
            GL11.glVertex2d((double)(width + 3.0f), (double)(2.5f + (Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? 0.5f : 0.0f)));
            GL11.glEnd();
            GL11.glEnable(3553);
            RenderUtils.release();
        }
        GlStateManager.disableBlend();
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect() && (renderMainHand.getItem() instanceof ItemTool || renderMainHand.getItem() instanceof ItemArmor)) {
            renderMainHand.stackSize = 1;
        }
        if (!renderMainHand.isEmpty && renderMainHand.getItem() != Items.AIR) {
            final String stackName = renderMainHand.getDisplayName();
            final int stackNameWidth = (int)Evangelion.FONT_MANAGER.getStringWidth(stackName) / 2;
            GL11.glPushMatrix();
            GL11.glScalef(0.75f, 0.75f, 0.0f);
            Evangelion.FONT_MANAGER.drawStringWithShadow(stackName, (float)(-stackNameWidth), ModuleNameTags.show_armor.getValue() ? ((float)(int)(-(this.getBiggestArmorTag(player) + 18.0f))) : ((float)(ModuleNameTags.percent.getValue() ? -36 : -26)), Color.WHITE);
            GL11.glScalef(1.5f, 1.5f, 1.0f);
            GL11.glPopMatrix();
        }
        GlStateManager.pushMatrix();
        int xOffset = -8;
        for (final ItemStack stack : player.inventory.armorInventory) {
            if (stack != null) {
                xOffset -= 8;
            }
        }
        xOffset -= 8;
        final ItemStack renderOffhand = player.getHeldItemOffhand().copy();
        if (renderOffhand.hasEffect() && (renderOffhand.getItem() instanceof ItemTool || renderOffhand.getItem() instanceof ItemArmor)) {
            renderOffhand.stackSize = 1;
        }
        if (ModuleNameTags.show_armor.getValue()) {
            this.renderItemStack(renderMainHand, xOffset);
        }
        xOffset += 16;
        for (int i = player.inventory.armorInventory.size(); i > 0; --i) {
            final ItemStack stack2 = (ItemStack)player.inventory.armorInventory.get(i - 1);
            final ItemStack armourStack = stack2.copy();
            if (armourStack.hasEffect() && (armourStack.getItem() instanceof ItemTool || armourStack.getItem() instanceof ItemArmor)) {
                armourStack.stackSize = 1;
            }
            if (ModuleNameTags.show_armor.getValue()) {
                this.renderItemStack(armourStack, xOffset);
            }
            if (ModuleNameTags.percent.getValue()) {
                this.renderPercent(armourStack, xOffset);
            }
            xOffset += 16;
        }
        if (ModuleNameTags.show_armor.getValue()) {
            this.renderItemStack(renderOffhand, xOffset);
        }
        GlStateManager.popMatrix();
        Evangelion.FONT_MANAGER.drawStringWithShadow(this.getChatFormatting(player) + displayTag, (float)(-width), -(Evangelion.FONT_MANAGER.getHeight() - 1.0f) + (float)(Evangelion.MODULE_MANAGER.isModuleEnabled("Font") ? -0.5 : 0.0), Color.WHITE);
        camera.posX = originalPositionX;
        camera.posY = originalPositionY;
        camera.posZ = originalPositionZ;
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.disablePolygonOffset();
        GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderPercent(final ItemStack stack, final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        if (DamageUtils.hasDurability(stack)) {
            final int percent = DamageUtils.getRoundedDamage(stack);
            String color;
            if (percent >= 60) {
                color = this.section_sign() + "a";
            }
            else if (percent >= 25) {
                color = this.section_sign() + "e";
            }
            else {
                color = this.section_sign() + "c";
            }
            Evangelion.FONT_MANAGER.drawStringWithShadow(color + percent + "%", (float)(x * 2), ModuleNameTags.show_armor.getValue() ? ((this.enchantIgriega < -62) ? ((float)this.enchantIgriega) : -62.0f) : -36.0f, Color.WHITE);
        }
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderItemStack(final ItemStack stack, final int x) {
        GlStateManager.pushMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.clear(256);
        RenderHelper.enableStandardItemLighting();
        ModuleNameTags.mc.getRenderItem().zLevel = -150.0f;
        GlStateManager.disableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.disableCull();
        ModuleNameTags.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, -29);
        ModuleNameTags.mc.getRenderItem().renderItemOverlays(ModuleNameTags.mc.fontRenderer, stack, x, -29);
        ModuleNameTags.mc.getRenderItem().zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableCull();
        GlStateManager.enableAlpha();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.disableDepth();
        this.renderEnchantmentText(stack, x);
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.popMatrix();
    }
    
    private void renderEnchantmentText(final ItemStack stack, final int x) {
        int enchantmentY = -37;
        final NBTTagList enchants = stack.getEnchantmentTagList();
        for (int index = 0; index < enchants.tagCount(); ++index) {
            final short id = enchants.getCompoundTagAt(index).getShort("id");
            final short level = enchants.getCompoundTagAt(index).getShort("lvl");
            final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
            if (enc != null) {
                String encName = enc.isCurse() ? (TextFormatting.RED + enc.getTranslatedName((int)level).substring(11).substring(0, 1).toLowerCase()) : enc.getTranslatedName((int)level).substring(0, 1).toLowerCase();
                encName += level;
                Evangelion.FONT_MANAGER.drawStringWithShadow(encName, (float)(x * 2), (float)enchantmentY, Color.WHITE);
                enchantmentY -= 8;
            }
        }
        this.enchantIgriega = enchantmentY;
    }
    
    private float getBiggestArmorTag(final EntityPlayer player) {
        float enchantmentY = 0.0f;
        boolean arm = false;
        for (final ItemStack stack : player.inventory.armorInventory) {
            float encY = 0.0f;
            if (stack != null) {
                final NBTTagList enchants = stack.getEnchantmentTagList();
                for (int index = 0; index < enchants.tagCount(); ++index) {
                    final short id = enchants.getCompoundTagAt(index).getShort("id");
                    final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                    if (enc != null) {
                        encY += 8.0f;
                        arm = true;
                    }
                }
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        final ItemStack renderMainHand = player.getHeldItemMainhand().copy();
        if (renderMainHand.hasEffect()) {
            float encY2 = 0.0f;
            final NBTTagList enchants2 = renderMainHand.getEnchantmentTagList();
            for (int index2 = 0; index2 < enchants2.tagCount(); ++index2) {
                final short id2 = enchants2.getCompoundTagAt(index2).getShort("id");
                final Enchantment enc2 = Enchantment.getEnchantmentByID((int)id2);
                if (enc2 != null) {
                    encY2 += 8.0f;
                    arm = true;
                }
            }
            if (encY2 > enchantmentY) {
                enchantmentY = encY2;
            }
        }
        final ItemStack renderOffHand = player.getHeldItemOffhand().copy();
        if (renderOffHand.hasEffect()) {
            float encY = 0.0f;
            final NBTTagList enchants = renderOffHand.getEnchantmentTagList();
            for (int index = 0; index < enchants.tagCount(); ++index) {
                final short id = enchants.getCompoundTagAt(index).getShort("id");
                final Enchantment enc = Enchantment.getEnchantmentByID((int)id);
                if (enc != null) {
                    encY += 8.0f;
                    arm = true;
                }
            }
            if (encY > enchantmentY) {
                enchantmentY = encY;
            }
        }
        return (arm ? 0 : 20) + enchantmentY;
    }
    
    private String getDisplayTag(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText();
        if (name.contains(ModuleNameTags.mc.getSession().getUsername())) {
            name = "You";
        }
        if (!ModuleNameTags.show_health.getValue()) {
            return name;
        }
        final float health = player.getHealth() + player.getAbsorptionAmount();
        String color;
        if (health > 18.0f) {
            color = TextFormatting.GREEN.toString();
        }
        else if (health > 16.0f) {
            color = TextFormatting.DARK_GREEN.toString();
        }
        else if (health > 12.0f) {
            color = TextFormatting.YELLOW.toString();
        }
        else if (health > 8.0f) {
            color = TextFormatting.GOLD.toString();
        }
        else if (health > 5.0f) {
            color = TextFormatting.RED.toString();
        }
        else {
            color = TextFormatting.DARK_RED.toString();
        }
        String pingStr = "";
        if (ModuleNameTags.show_ping.getValue()) {
            try {
                final int responseTime = Objects.requireNonNull(ModuleNameTags.mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime();
                pingStr = " " + pingStr + responseTime + "ms";
            }
            catch (Exception ex) {}
        }
        String idString = "";
        if (ModuleNameTags.entityID.getValue()) {
            idString = idString + " ID: " + player.getEntityId() + " ";
        }
        String gameModeStr = "";
        if (ModuleNameTags.gamemode.getValue()) {
            if (player.isCreative()) {
                gameModeStr += " [C]";
            }
            else if (player.isSpectator() || player.isInvisible()) {
                gameModeStr += " [I]";
            }
            else {
                gameModeStr += " [S]";
            }
        }
        String healthStr = "";
        if (ModuleNameTags.show_health.getValue()) {
            if (Math.floor(health) == health) {
                healthStr = color + " " + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "dead");
            }
            else {
                healthStr = color + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "dead");
            }
        }
        String popcolor = "";
        if (ModulePopCounter.popCount.get(player.getName()) != null) {
            if (ModulePopCounter.popCount.get(player.getName()) == 1) {
                popcolor = this.section_sign() + "a";
            }
            else if (ModulePopCounter.popCount.get(player.getName()) == 2) {
                popcolor = this.section_sign() + "2";
            }
            else if (ModulePopCounter.popCount.get(player.getName()) == 3) {
                popcolor = this.section_sign() + "e";
            }
            else if (ModulePopCounter.popCount.get(player.getName()) == 4) {
                popcolor = this.section_sign() + "6";
            }
            else if (ModulePopCounter.popCount.get(player.getName()) == 5) {
                popcolor = this.section_sign() + "c";
            }
            else {
                popcolor = this.section_sign() + "4";
            }
        }
        String popStr = "";
        if (ModuleNameTags.show_totems.getValue()) {
            try {
                popStr += ((ModulePopCounter.popCount.get(player.getName()) == null) ? "" : (popcolor + " -" + ModulePopCounter.popCount.get(player.getName())));
            }
            catch (Exception ex2) {}
        }
        return name + idString + gameModeStr + pingStr + healthStr + popStr;
    }
    
    private ChatFormatting getChatFormatting(final EntityPlayer player) {
        if (Evangelion.FRIEND_MANAGER.isFriend(player.getName())) {
            return ChatFormatting.AQUA;
        }
        if (player.isSneaking()) {
            return ChatFormatting.GOLD;
        }
        return ChatFormatting.RESET;
    }
    
    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }
    
    public String section_sign() {
        return "§";
    }
    
    static {
        ModuleNameTags.show_armor = new ValueBoolean("Armor", "ShowArmor", "", false);
        ModuleNameTags.percent = new ValueBoolean("Percent", "Percent", "", false);
        ModuleNameTags.show_health = new ValueBoolean("Health", "Health", "", false);
        ModuleNameTags.show_ping = new ValueBoolean("Ping", "Ping", "", false);
        ModuleNameTags.show_totems = new ValueBoolean("Totems", "Totems", "", false);
        ModuleNameTags.show_invis = new ValueBoolean("Invis", "Invis", "", false);
        ModuleNameTags.gamemode = new ValueBoolean("Gamemode", "Gamemode", "", false);
        ModuleNameTags.entityID = new ValueBoolean("EntityID", "Entity ID", "", false);
        ModuleNameTags.m_scale = new ValueNumber("Scale", "Scale", "", 30, 10, 100);
        ModuleNameTags.width = new ValueNumber("Width", "Width", "", 1.5, 0.1, 3.0);
        ModuleNameTags.outline = new ValueBoolean("Outline", "Outline", "", true);
        ModuleNameTags.color = new ValueColor("Color", "Color", "", new Color(255, 255, 255, 255));
    }
}
