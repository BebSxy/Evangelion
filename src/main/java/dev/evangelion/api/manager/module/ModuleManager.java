package dev.evangelion.api.manager.module;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import dev.evangelion.client.events.EventDeath;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import dev.evangelion.client.events.EventRender3D;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.events.EventRender2D;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import dev.evangelion.client.events.EventMotion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import dev.evangelion.client.values.Value;
import java.util.function.Function;
import java.util.Comparator;
import dev.evangelion.client.modules.client.ModuleFont;
import dev.evangelion.client.modules.client.ModuleHUDEditor;
import dev.evangelion.client.modules.client.ModuleGUI;
import dev.evangelion.client.modules.client.ModuleCommands;
import dev.evangelion.client.modules.client.ModuleColor;
import dev.evangelion.client.modules.client.ModuleHUD;
import dev.evangelion.client.modules.client.ModuleMiddleClick;
import dev.evangelion.client.modules.client.ModuleStreamerMode;
import dev.evangelion.client.modules.client.ModuleRotations;
import dev.evangelion.client.modules.visuals.esp.ModuleESP;
import dev.evangelion.client.modules.visuals.ModuleViewClip;
import dev.evangelion.client.modules.visuals.ModuleNoRender;
import dev.evangelion.client.modules.visuals.ModuleNameTags;
import dev.evangelion.client.modules.visuals.ModuleModelChanger;
import dev.evangelion.client.modules.visuals.ModuleGlintModify;
import dev.evangelion.client.modules.visuals.ModuleGameSettings;
import dev.evangelion.client.modules.visuals.ModuleFullBright;
import dev.evangelion.client.modules.visuals.ModuleBlockOverlay;
import dev.evangelion.client.modules.visuals.ModuleCrosshair;
import dev.evangelion.client.modules.visuals.ModuleAnimations;
import dev.evangelion.client.modules.visuals.chams.ModuleChams;
import dev.evangelion.client.modules.visuals.ModuleArrows;
import dev.evangelion.client.modules.visuals.ModuleHitmarkers;
import dev.evangelion.client.modules.visuals.waypoints.ModuleWaypoints;
import dev.evangelion.client.modules.visuals.ModuleKillEffects;
import dev.evangelion.client.modules.visuals.ModuleTrajectories;
import dev.evangelion.client.modules.movement.ModuleAnchor;
import dev.evangelion.client.modules.movement.ModuleSprint;
import dev.evangelion.client.modules.movement.ModuleStep;
import dev.evangelion.client.modules.movement.ModuleBoost;
import dev.evangelion.client.modules.movement.ModuleFakeLag;
import dev.evangelion.client.modules.movement.ModuleSpeed;
import dev.evangelion.client.modules.movement.ModuleVelocity;
import dev.evangelion.client.modules.movement.ModuleNoSlow;
import dev.evangelion.client.modules.miscellaneous.ModuleBuildHeight;
import dev.evangelion.client.modules.miscellaneous.ModuleWelcomer;
import dev.evangelion.client.modules.miscellaneous.ambience.ModuleAmbience;
import dev.evangelion.client.modules.miscellaneous.spammer.ModuleSpammer;
import dev.evangelion.client.modules.miscellaneous.ModuleChat;
import dev.evangelion.client.modules.miscellaneous.ModuleXCarry;
import dev.evangelion.client.modules.player.ModuleSwing;
import dev.evangelion.client.modules.player.ModuleSpeedMine;
import dev.evangelion.client.modules.player.ModuleReplenish;
import dev.evangelion.client.modules.player.ModuleFastProjectile;
import dev.evangelion.client.modules.player.ModuleFakePlayer;
import dev.evangelion.client.modules.player.ModulePlayerDebug;
import dev.evangelion.client.modules.player.ModuleMultiTask;
import dev.evangelion.client.modules.combat.ModuleOffhand;
import dev.evangelion.client.modules.combat.ModuleReach;
import dev.evangelion.client.modules.combat.ModuleAutoLog;
import dev.evangelion.client.modules.combat.ModuleKillAura;
import dev.evangelion.client.modules.combat.ModuleSurround;
import dev.evangelion.client.modules.combat.ModuleSelfFill;
import dev.evangelion.client.modules.combat.ModuleCriticals;
import dev.evangelion.client.modules.combat.ModuleAutoTrap;
import dev.evangelion.client.modules.combat.autocrystal.ModuleAutoCrystal;
import dev.evangelion.client.modules.combat.ModuleKillsay;
import dev.evangelion.client.modules.combat.ModulePopCounter;
import dev.evangelion.client.modules.combat.ModuleAutoArmor;
import dev.evangelion.client.modules.combat.ModuleHoleFill;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import dev.evangelion.api.utilities.IMinecraft;

public class ModuleManager implements IMinecraft
{
    private final ArrayList<Module> modules;
    
    public ModuleManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.modules = new ArrayList<Module>();
        this.register(new ModuleHoleFill());
        this.register(new ModuleAutoArmor());
        this.register(new ModulePopCounter());
        this.register(new ModuleKillsay());
        this.register(new ModuleAutoCrystal());
        this.register(new ModuleAutoTrap());
        this.register(new ModuleCriticals());
        this.register(new ModuleSelfFill());
        this.register(new ModuleSurround());
        this.register(new ModuleKillAura());
        this.register(new ModuleAutoLog());
        this.register(new ModuleReach());
        this.register(new ModuleOffhand());
        this.register(new ModuleMultiTask());
        this.register(new ModulePlayerDebug());
        this.register(new ModuleFakePlayer());
        this.register(new ModuleFastProjectile());
        this.register(new ModuleReplenish());
        this.register(new ModuleSpeedMine());
        this.register(new ModuleSwing());
        this.register(new ModuleXCarry());
        this.register(new ModuleChat());
        this.register(new ModuleSpammer());
        this.register(new ModuleAmbience());
        this.register(new ModuleWelcomer());
        this.register(new ModuleBuildHeight());
        this.register(new ModuleNoSlow());
        this.register(new ModuleVelocity());
        this.register(new ModuleSpeed());
        this.register(new ModuleFakeLag());
        this.register(new ModuleBoost());
        this.register(new ModuleStep());
        this.register(new ModuleSprint());
        this.register(new ModuleAnchor());
        this.register(new ModuleTrajectories());
        this.register(new ModuleKillEffects());
        this.register(new ModuleWaypoints());
        this.register(new ModuleHitmarkers());
        this.register(new ModuleArrows());
        this.register(new ModuleChams());
        this.register(new ModuleAnimations());
        this.register(new ModuleCrosshair());
        this.register(new ModuleBlockOverlay());
        this.register(new ModuleFullBright());
        this.register(new ModuleGameSettings());
        this.register(new ModuleGlintModify());
        this.register(new ModuleModelChanger());
        this.register(new ModuleNameTags());
        this.register(new ModuleNoRender());
        this.register(new ModuleViewClip());
        this.register(new ModuleESP());
        this.register(new ModuleRotations());
        this.register(new ModuleStreamerMode());
        this.register(new ModuleMiddleClick());
        this.register(new ModuleHUD());
        this.register(new ModuleColor());
        this.register(new ModuleCommands());
        this.register(new ModuleGUI());
        this.register(new ModuleHUDEditor());
        this.register(new ModuleFont());
        this.modules.sort(Comparator.comparing((Function<? super Module, ? extends Comparable>)Module::getName));
    }
    
    public void register(final Module module) {
        try {
            for (final Field field : module.getClass().getDeclaredFields()) {
                if (Value.class.isAssignableFrom(field.getType())) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    module.getValues().add((Value)field.get(module));
                }
            }
            module.getValues().add(module.tag);
            module.getValues().add(module.chatNotify);
            module.getValues().add(module.drawn);
            module.getValues().add(module.bind);
            this.modules.add(module);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Module> getModules() {
        return this.modules;
    }

    public ArrayList<Module> getModules(Module.Category category) {
        return (ArrayList)this.modules.stream().filter(mm -> mm.getCategory().equals((Object)category)).collect(Collectors.toList());
    }
    
    public boolean isModuleEnabled(final String name) {
        final Module module = this.modules.stream().filter(mm -> mm.getName().equals(name)).findFirst().orElse(null);
        return module != null && module.isToggled();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (ModuleManager.mc.player != null && ModuleManager.mc.world != null) {
            this.modules.stream().filter(Module::isToggled).forEach(Module::onTick);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final EventMotion event) {
        if (ModuleManager.mc.player != null && ModuleManager.mc.world != null) {
            this.modules.stream().filter(Module::isToggled).forEach(Module::onUpdate);
        }
    }
    
    @SubscribeEvent
    public void onRender2D(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            final EventRender2D renderEvent = new EventRender2D(event.getPartialTicks());
            this.modules.stream().filter(Module::isToggled).forEach(m -> m.onRender2D(renderEvent));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent
    public void onRender3D(final RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        ModuleManager.mc.profiler.startSection("evangelion");
        GL11.glHint(3154, 4354);
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.shadeModel(7425);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GL11.glEnable(2848);
        GL11.glEnable(34383);
        final EventRender3D renderEvent = new EventRender3D(event.getPartialTicks());
        this.modules.stream().filter(Module::isToggled).forEach(mm -> mm.onRender3D(renderEvent));
        GL11.glDisable(34383);
        GL11.glDisable(2848);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GL11.glHint(3154, 4352);
        ModuleManager.mc.profiler.endSection();
    }
    
    @SubscribeEvent
    public void onConnect(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.modules.stream().filter(Module::isToggled).forEach(Module::onLogin);
    }
    
    @SubscribeEvent
    public void onDisconnect(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.modules.stream().filter(Module::isToggled).forEach(Module::onLogout);
    }
    
    @SubscribeEvent
    public void onDeath(final EventDeath event) {
        if (event.getEntity() == ModuleManager.mc.player) {
            this.modules.stream().filter(Module::isToggled).forEach(Module::onDeath);
        }
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0) {
                return;
            }
            for (final Module module : this.modules) {
                if (module.getBind() == Keyboard.getEventKey()) {
                    module.toggle(true);
                }
            }
        }
    }
}
