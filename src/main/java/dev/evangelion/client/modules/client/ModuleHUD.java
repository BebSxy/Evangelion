package dev.evangelion.client.modules.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.utilities.ColorUtils;
import dev.evangelion.api.utilities.TPSUtils;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.client.events.EventRender2D;
import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterModule(name="HUD", description="Render information on screen.", category=Module.Category.CLIENT)
public class ModuleHUD
        extends Module {
    public static ModuleHUD INSTANCE;
    ValueBoolean direction = new ValueBoolean("Direction", "Direction", "", true);
    ValueBoolean potionEffects = new ValueBoolean("PotionEffects", "Potion Effects", "", true);
    public ValueEnum effectHud = new ValueEnum("EffectHUD", "Effect HUD", "", effectHuds.Hide);
    ValueBoolean serverBrand = new ValueBoolean("ServerBrand", "Server Brand", "", true);
    ValueBoolean tps = new ValueBoolean("TPS", "TPS", "", true);
    ValueBoolean fps = new ValueBoolean("FPS", "FPS", "", true);
    ValueBoolean speed = new ValueBoolean("Speed", "Speed", "", true);
    ValueBoolean ping = new ValueBoolean("Ping", "Ping", "", true);
    ValueBoolean packetPS = new ValueBoolean("PacketsPS", "Packets/s", "", false);
    ValueEnum ordering = new ValueEnum("Ordering", "Ordering", "", orderings.Length);
    ValueBoolean coords = new ValueBoolean("Coords", "Coords", "", true);
    ValueBoolean netherCoords = new ValueBoolean("NetherCoords", "Nether Coords", "", true);
    ValueBoolean durability = new ValueBoolean("Durability", "Durability", "", true);
    ValueBoolean arrayList = new ValueBoolean("ArrayList", "Array List", "", true);
    ValueEnum modulesColor = new ValueEnum("ModulesColor", "Modules Color", "Color mode for array list.", modulesColors.Normal);
    ValueEnum rendering = new ValueEnum("Rendering", "Rendering", "", renderings.Up);
    DecimalFormat format = new DecimalFormat("#.#");
    ArrayList<Module> modules;
    private int packets;
    TimerUtils packetTimer = new TimerUtils();
    private int maxFPS = 0;
    int compAdd;
    ArrayList<String> comps;
    ChatFormatting reset = ChatFormatting.RESET;
    ChatFormatting gray = ChatFormatting.GRAY;

    public ModuleHUD() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onSend(EventPacketSend event) {
        ++this.packets;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.packetTimer.hasTimeElapsed(1000L)) {
            this.packets = 0;
            this.packetTimer.reset();
        }
        if (Minecraft.getDebugFPS() > this.maxFPS) {
            this.maxFPS = Minecraft.getDebugFPS();
        }
    }

    @Override
    public void onRender2D(EventRender2D event) {
        super.onRender2D(event);
        if (this.nullCheck()) {
            return;
        }
        Color durabilityColor = Color.WHITE;
        if (this.isItemTool(ModuleHUD.mc.player.getHeldItemMainhand().getItem())) {
            float green = ((float)ModuleHUD.mc.player.getHeldItemMainhand().getMaxDamage() - (float)ModuleHUD.mc.player.getHeldItemMainhand().getItemDamage()) / (float)ModuleHUD.mc.player.getHeldItemMainhand().getMaxDamage();
            float red = 1.0f - green;
            durabilityColor = new Color(red, green, 0.0f);
        }
        this.modules = new ArrayList();
        this.compAdd = 0;
        this.comps = new ArrayList();
        float sWidth = new ScaledResolution(mc).getScaledWidth();
        float sHeight = new ScaledResolution(mc).getScaledHeight();
        if (this.direction.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow(this.getDirectionName() + (Object)this.gray + " [" + (Object)this.reset + this.getFacing(ModuleHUD.mc.player.getHorizontalFacing().getName()) + (Object)this.gray + "]", 2.0f, sHeight - 12.0f - (float)(this.coords.getValue() ? 10 : 0), Color.WHITE);
        }
        if (this.coords.getValue()) {
            Evangelion.FONT_MANAGER.drawStringWithShadow((Object)this.gray + "XYZ" + (Object)this.reset + " " + this.format.format(ModuleHUD.mc.player.posX) + (Object)this.gray + ", " + (Object)this.reset + this.format.format(ModuleHUD.mc.player.posY) + (Object)this.gray + ", " + (Object)this.reset + this.format.format(ModuleHUD.mc.player.posZ) + (this.netherCoords.getValue() ? (Object)this.gray + " [" + (Object)this.reset + this.format.format(ModuleHUD.mc.player.dimension == -1 ? ModuleHUD.mc.player.posX * 8.0 : ModuleHUD.mc.player.posX / 8.0) + (Object)this.gray + ", " + (Object)this.reset + this.format.format(ModuleHUD.mc.player.dimension == -1 ? ModuleHUD.mc.player.posZ * 8.0 : ModuleHUD.mc.player.posZ / 8.0) + (Object)this.gray + "]" : ""), 2.0f, sHeight - 12.0f, Color.WHITE);
        }
        if (this.arrayList.getValue()) {
            for (Module module : Evangelion.MODULE_MANAGER.getModules()) {
                if (!module.isToggled() || !module.isDrawn()) continue;
                this.modules.add(module);
            }
            if (!this.modules.isEmpty()) {
                float x;
                float y = 0;
                String string;
                int addY = 0;
                if (this.ordering.getValue().equals((Object)orderings.Length)) {
                    for (Module m : this.modules.stream().sorted(Comparator.comparing(s -> Float.valueOf(Evangelion.FONT_MANAGER.getStringWidth(s.getTag() + (s.getHudInfo().isEmpty() ? "" : (Object)this.gray + " [" + (Object)ChatFormatting.WHITE + s.getHudInfo() + (Object)this.gray + "]")) * -1.0f))).collect(Collectors.toList())) {
                        string = m.getTag() + (m.getHudInfo().isEmpty() ? "" : (Object)this.gray + " [" + (Object)ChatFormatting.WHITE + m.getHudInfo() + (Object)this.gray + "]");
                        x = sWidth - 2.0f - Evangelion.FONT_MANAGER.getStringWidth(string);
                        float f = this.rendering.getValue().equals((Object)renderings.Up) ? (float)(2 + addY * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.player.getActivePotionEffects().isEmpty() ? 25 : 0)) : (y = sHeight - 12.0f - (float)(addY * 10));
                        Evangelion.FONT_MANAGER.drawStringWithShadow(string, x, y, this.modulesColor.getValue().equals((Object)modulesColors.Normal) ? ModuleColor.getColor() : (this.modulesColor.getValue().equals((Object)modulesColors.Random) ? m.getRandomColor() : ColorUtils.rainbow(addY)));
                        ++addY;
                    }
                } else {
                    for (Module m : this.modules.stream().sorted(Comparator.comparing(s -> s.getName())).collect(Collectors.toList())) {
                        string = m.getTag() + (m.getHudInfo().isEmpty() ? "" : (Object)this.gray + " [" + (Object)ChatFormatting.WHITE + m.getHudInfo() + (Object)this.gray + "]");
                        x = sWidth - 2.0f - Evangelion.FONT_MANAGER.getStringWidth(string);
                        float f = this.rendering.getValue().equals((Object)renderings.Up) ? (float)(2 + addY * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.player.getActivePotionEffects().isEmpty() ? 25 : 0)) : (y = sHeight - 12.0f - (float)(addY * 10));
                        Evangelion.FONT_MANAGER.drawStringWithShadow(string, x, y, this.modulesColor.getValue().equals((Object)modulesColors.Normal) ? ModuleColor.getColor() : (this.modulesColor.getValue().equals((Object)modulesColors.Random) ? m.getRandomColor() : ColorUtils.rainbow(addY)));
                        ++addY;
                    }
                }
            }
        }
        if (this.potionEffects.getValue()) {
            int[] potCount = new int[]{0};
            try {
                ModuleHUD.mc.player.getActivePotionEffects().forEach(effect -> {
                    String name = I18n.format((String)effect.getPotion().getName(), (Object[])new Object[0]);
                    double duration = (float)effect.getDuration() / 19.99f;
                    int amplifier = effect.getAmplifier() + 1;
                    int potionColor = effect.getPotion().getLiquidColor();
                    double p1 = duration % 60.0;
                    DecimalFormat format2 = new DecimalFormat("00");
                    String seconds = format2.format(p1);
                    String s = name + " " + amplifier + (Object)ChatFormatting.WHITE + " " + (int)duration / 60 + ":" + seconds;
                    Evangelion.FONT_MANAGER.drawStringWithShadow(s, sWidth - 2.0f - Evangelion.FONT_MANAGER.getStringWidth(s), this.rendering.getValue().equals((Object)renderings.Down) ? (float)(2 + potCount[0] * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.player.getActivePotionEffects().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(potCount[0] * 10), new Color(potionColor));
                    potCount[0] = potCount[0] + 1;
                    ++this.compAdd;
                });
            }
            catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            }
        }
        if (this.fps.getValue()) {
            this.comps.add((Object)this.gray + "FPS " + (Object)this.reset + Minecraft.getDebugFPS() + (Object)this.gray + " [" + (Object)this.reset + this.maxFPS + (Object)this.gray + "]");
        }
        if (this.tps.getValue()) {
            this.comps.add((Object)this.gray + "TPS " + (Object)this.reset + String.format("%.2f", TPSUtils.getTickRate()));
        }
        if (this.ping.getValue()) {
            this.comps.add((Object)this.gray + "Ping " + (Object)this.reset + this.getPing());
        }
        if (this.packetPS.getValue()) {
            this.comps.add((Object)this.gray + "Packets/s " + (Object)this.reset + this.packets);
        }
        if (this.speed.getValue()) {
            DecimalFormat df = new DecimalFormat("#.#");
            double d = Minecraft.getMinecraft().player.posX - Minecraft.getMinecraft().player.prevPosX;
            double deltaZ = Minecraft.getMinecraft().player.posZ - Minecraft.getMinecraft().player.prevPosZ;
            float tickRate = Minecraft.getMinecraft().timer.tickLength / 1000.0f;
            String speedText = df.format((double)(MathHelper.sqrt((double)(d * d + deltaZ * deltaZ)) / tickRate) * 3.6);
            this.comps.add((Object)this.gray + "Speed " + (Object)this.reset + speedText + "km/h");
        }
        if (this.serverBrand.getValue()) {
            this.comps.add((Object)this.gray + this.getServerBrand());
        }
        if (this.durability.getValue()) {
            this.comps.add((Object)this.gray + "Durability " + (Object)this.reset + (ModuleHUD.mc.player.getHeldItemMainhand().getMaxDamage() - ModuleHUD.mc.player.getHeldItemMainhand().getItemDamage()));
        }
        if (!this.comps.isEmpty()) {
            for (String string : this.comps.stream().sorted(Comparator.comparing(s -> Float.valueOf(Evangelion.FONT_MANAGER.getStringWidth((String)s) * -1.0f))).collect(Collectors.toList())) {
                if (string.startsWith((Object)this.gray + "Durability") && !this.isItemTool(ModuleHUD.mc.player.getHeldItemMainhand().getItem())) continue;
                Evangelion.FONT_MANAGER.drawStringWithShadow(string, sWidth - 2.0f - Evangelion.FONT_MANAGER.getStringWidth(string), this.rendering.getValue().equals((Object)renderings.Down) ? (float)(2 + this.compAdd * 10 + (this.effectHud.getValue().equals((Object)effectHuds.Shift) && !ModuleHUD.mc.player.getActivePotionEffects().isEmpty() ? 25 : 0)) : sHeight - 12.0f - (float)(this.compAdd * 10), string.startsWith((Object)this.gray + "Durability") ? durabilityColor : Color.WHITE);
                ++this.compAdd;
            }
        }
    }

    public boolean isItemTool(Item item) {
        return item instanceof ItemArmor || item == Items.DIAMOND_SWORD || item == Items.DIAMOND_PICKAXE || item == Items.DIAMOND_AXE || item == Items.DIAMOND_SHOVEL || item == Items.DIAMOND_HOE || item == Items.IRON_SWORD || item == Items.IRON_PICKAXE || item == Items.IRON_AXE || item == Items.IRON_SHOVEL || item == Items.IRON_HOE || item == Items.GOLDEN_SWORD || item == Items.GOLDEN_PICKAXE || item == Items.GOLDEN_AXE || item == Items.GOLDEN_SHOVEL || item == Items.GOLDEN_HOE || item == Items.STONE_SWORD || item == Items.STONE_PICKAXE || item == Items.STONE_AXE || item == Items.STONE_SHOVEL || item == Items.STONE_HOE || item == Items.WOODEN_SWORD || item == Items.WOODEN_PICKAXE || item == Items.WOODEN_AXE || item == Items.WOODEN_SHOVEL || item == Items.WOODEN_HOE;
    }

    private String getDirectionName() {
        return ModuleHUD.mc.player.getHorizontalFacing().getName().substring(0, 1).toUpperCase() + ModuleHUD.mc.player.getHorizontalFacing().getName().substring(1).toLowerCase();
    }

    private String getFacing(String input) {
        switch (input.toLowerCase()) {
            case "north": {
                return "-Z";
            }
            case "east": {
                return "+X";
            }
            case "south": {
                return "+Z";
            }
        }
        return "-X";
    }

    private int getPing() {
        int p;
        if (ModuleHUD.mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(ModuleHUD.mc.player.getName()) == null) {
            p = -1;
        } else {
            ModuleHUD.mc.player.getName();
            p = Objects.requireNonNull(mc.getConnection().getPlayerInfo(ModuleHUD.mc.player.getName())).getResponseTime();
        }
        return p;
    }

    private String getServerBrand() {
        String getServerBrand;
        String s = mc.getCurrentServerData() == null ? "Vanilla" : ((getServerBrand = ModuleHUD.mc.player.getServerBrand()) == null ? "Vanilla" : getServerBrand);
        return s;
    }

    public static enum renderings {
        Up,
        Down;

    }

    public static enum modulesColors {
        Normal,
        Random,
        Rainbow;

    }

    public static enum orderings {
        Length,
        ABC;

    }

    public static enum effectHuds {
        Show,
        Hide,
        Shift;

    }
}

