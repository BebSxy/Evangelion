package dev.evangelion.api.manager.element;

import dev.evangelion.client.events.EventDeath;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.renderer.GlStateManager;
import dev.evangelion.client.events.EventRender2D;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import dev.evangelion.client.events.EventMotion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.Iterator;
import java.lang.reflect.Field;
import dev.evangelion.client.values.Value;
import java.util.function.Function;
import java.util.Comparator;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.client.elements.ElementPlayerList;
import dev.evangelion.client.elements.ElementTotems;
import dev.evangelion.client.elements.ElementWelcomer;
import dev.evangelion.client.elements.ElementWatermark;
import dev.evangelion.client.elements.ElementStickyNotes;
import dev.evangelion.client.elements.ElementFriends;
import dev.evangelion.client.elements.ElementArmor;
import net.minecraftforge.common.MinecraftForge;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class ElementManager
{
    protected static final Minecraft mc;
    private final ArrayList<Element> elements;
    
    public ElementManager() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.elements = new ArrayList<Element>();
        this.register(new ElementArmor());
        this.register(new ElementFriends());
        this.register(new ElementStickyNotes());
        this.register(new ElementWatermark());
        this.register(new ElementWelcomer());
        this.register(new ElementTotems());
        this.register(new ElementPlayerList());
        this.elements.sort(Comparator.comparing((Function<? super Element, ? extends Comparable>)Module::getName));
    }
    
    public void register(final Element element) {
        try {
            for (final Field field : element.getClass().getDeclaredFields()) {
                if (Value.class.isAssignableFrom(field.getType())) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    element.getValues().add((Value)field.get(element));
                }
            }
            this.elements.add(element);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Element> getElements() {
        return this.elements;
    }
    
    public Element getElement(final String name) {
        for (final Element module : this.elements) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }
    
    public boolean isElementEnabled(final String name) {
        final Element module = this.elements.stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
        return module != null && module.isToggled();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (ElementManager.mc.player != null && ElementManager.mc.world != null) {
            this.elements.stream().filter(Module::isToggled).forEach(Module::onTick);
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final EventMotion event) {
        if (ElementManager.mc.player != null && ElementManager.mc.world != null) {
            this.elements.stream().filter(Module::isToggled).forEach(Module::onUpdate);
        }
    }
    
    @SubscribeEvent
    public void onRender2D(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            this.elements.stream().filter(Module::isToggled).forEach(m -> m.onRender2D(new EventRender2D(event.getPartialTicks())));
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent
    public void onLogin(final FMLNetworkEvent.ClientConnectedToServerEvent event) {
        this.elements.stream().filter(Module::isToggled).forEach(Module::onLogin);
    }
    
    @SubscribeEvent
    public void onLogout(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        this.elements.stream().filter(Module::isToggled).forEach(Module::onLogout);
    }
    
    @SubscribeEvent
    public void onDeath(final EventDeath event) {
        if (event.getEntity() == ElementManager.mc.player) {
            this.elements.stream().filter(Module::isToggled).forEach(Module::onDeath);
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
