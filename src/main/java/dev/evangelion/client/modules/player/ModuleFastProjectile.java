package dev.evangelion.client.modules.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEgg;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "FastProjectile", tag = "Fast Projectile", description = "Makes your projectiles faster which results in instant speed.", category = Module.Category.PLAYER)
public class ModuleFastProjectile extends Module
{
    private final ValueNumber timeout;
    private final ValueNumber spoofs;
    private final ValueBoolean bows;
    private final ValueBoolean pearls;
    private final ValueBoolean eggs;
    private final ValueBoolean snowballs;
    private final ValueBoolean bypass;
    private long lastShoot;
    
    public ModuleFastProjectile() {
        this.timeout = new ValueNumber("Timeout", "Timeout", "The timeout for the effect.", 50, 10, 200);
        this.spoofs = new ValueNumber("Spoofs", "Spoofs", "The amount of spoofs that should happen.", 10, 1, 300);
        this.bows = new ValueBoolean("Bows", "Bows", "Applies the Fast Projectile effect to Bows.", true);
        this.pearls = new ValueBoolean("Pearls", "Pearls", "Applies the Fast Projectile effect to Pearls.", true);
        this.eggs = new ValueBoolean("Eggs", "Eggs", "Applies the Fast Projectile effect to Eggs.", true);
        this.snowballs = new ValueBoolean("Snowballs", "Snowballs", "Applies the Fast Projectile effect to Snowballs.", true);
        this.bypass = new ValueBoolean("Bypass", "Bypass", "Uses a bypass to bypass some anticheats.", false);
    }
    
    @SubscribeEvent
    public void onPacketSend(final EventPacketSend event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && ModuleFastProjectile.mc.player.getHeldItemMainhand().getItem() instanceof ItemBow && this.bows.getValue()) {
                this.spoof();
            }
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItem) {
            final CPacketPlayerTryUseItem packet2 = (CPacketPlayerTryUseItem)event.getPacket();
            if (packet2.getHand() == EnumHand.MAIN_HAND && ((ModuleFastProjectile.mc.player.getHeldItemMainhand().getItem() instanceof ItemEgg && this.eggs.getValue()) || (ModuleFastProjectile.mc.player.getHeldItemMainhand().getItem() instanceof ItemEnderPearl && this.pearls.getValue()) || (ModuleFastProjectile.mc.player.getHeldItemMainhand().getItem() instanceof ItemSnowball && this.snowballs.getValue()))) {
                this.spoof();
            }
        }
    }
    
    private void spoof() {
        if (System.currentTimeMillis() - this.lastShoot >= this.timeout.getValue().longValue() * 100L) {
            this.lastShoot = System.currentTimeMillis();
            ModuleFastProjectile.mc.player.connection.sendPacket((Packet)new CPacketEntityAction((Entity)ModuleFastProjectile.mc.player, CPacketEntityAction.Action.START_SPRINTING));
            for (int i = 0; i < this.spoofs.getValue().intValue(); ++i) {
                if (this.bypass.getValue()) {
                    ModuleFastProjectile.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleFastProjectile.mc.player.posX, ModuleFastProjectile.mc.player.posY + 1.0E-10, ModuleFastProjectile.mc.player.posZ, false));
                    ModuleFastProjectile.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleFastProjectile.mc.player.posX, ModuleFastProjectile.mc.player.posY - 1.0E-10, ModuleFastProjectile.mc.player.posZ, true));
                }
                else {
                    ModuleFastProjectile.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleFastProjectile.mc.player.posX, ModuleFastProjectile.mc.player.posY - 1.0E-10, ModuleFastProjectile.mc.player.posZ, true));
                    ModuleFastProjectile.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(ModuleFastProjectile.mc.player.posX, ModuleFastProjectile.mc.player.posY + 1.0E-10, ModuleFastProjectile.mc.player.posZ, false));
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.lastShoot = System.currentTimeMillis();
    }
}
