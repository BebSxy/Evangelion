package dev.evangelion.client.modules.combat;

import dev.evangelion.api.utilities.sounds.SoundInitializer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.network.play.client.CPacketUseEntity;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.Evangelion;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.api.utilities.ChatUtils;
import dev.evangelion.api.utilities.sounds.SoundUtils;
import dev.evangelion.client.events.EventDeath;
import java.util.Iterator;
import dev.evangelion.client.modules.combat.autocrystal.ModuleAutoCrystal;
import java.util.concurrent.ConcurrentHashMap;
import dev.evangelion.api.utilities.TimerUtils;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Map;
import java.io.File;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueString;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import java.util.ArrayList;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Killsay", description = "Send a message when u kill someone.", category = Module.Category.COMBAT)
public class ModuleKillsay extends Module
{
    public static ArrayList<String> deathMessages;
    public static ArrayList<String> deathMessagesJap;
    ValueCategory killCategory;
    ValueBoolean messageKill;
    ValueBoolean greenText;
    ValueEnum mode;
    ValueBoolean japanese;
    ValueString customGG;
    ValueNumber delay;
    ValueNumber reset;
    ValueBoolean soundKill;
    ValueString soundName;
    ValueNumber volumeKill;
    ValueCategory killStreakCategory;
    ValueBoolean deathReset;
    ValueBoolean messageKS;
    ValueBoolean soundKS;
    ValueNumber volumeKS;
    private File killSound;
    public Map<EntityPlayer, Integer> targets;
    public EntityPlayer cauraTarget;
    private TimerUtils timer;
    private TimerUtils cooldownTimer;
    private boolean cooldown;
    private int killStreak;
    
    public ModuleKillsay() {
        this.killCategory = new ValueCategory("Kill", "Kill category.");
        this.messageKill = new ValueBoolean("MessageKill", "Message", "Send a messaeg when you kill someone.", this.killCategory, true);
        this.greenText = new ValueBoolean("GreenText", "Green Text", "Make the message green by adding a '>' before.", this.killCategory, false);
        this.mode = new ValueEnum("Mode", "Mode", "", this.killCategory, modes.Default);
        this.japanese = new ValueBoolean("Japanese", "Japanese", "Make the messages in japanese.", this.killCategory, true);
        this.customGG = new ValueString("CustomGG", "CustomGG", "", this.killCategory, "GG <player>!");
        this.delay = new ValueNumber("Delay", "Delay", "Delay to wait before sending another message", this.killCategory, 10, 0, 30);
        this.reset = new ValueNumber("Reset", "Reset", "Time to reset the target.", this.killCategory, 30, 0, 90);
        this.soundKill = new ValueBoolean("SoundKill", "Sound", "Play a sound when you kill someone.", this.killCategory, true);
        this.soundName = new ValueString("SoundName", "Sound Name", "File name of killsound.", this.killCategory, "killsay.wav");
        this.volumeKill = new ValueNumber("VolumeKill", "Volume", "", this.killCategory, 50.0f, 0.0f, 50.0f);
        this.killStreakCategory = new ValueCategory("KillStreak", "Killstreak category.");
        this.deathReset = new ValueBoolean("DeathReset", "Death Reset", "Reset killsterak when you die.", this.killStreakCategory, false);
        this.messageKS = new ValueBoolean("MessageKS", "Message", "Send client message of your killstreak.", this.killStreakCategory, false);
        this.soundKS = new ValueBoolean("SoundKS", "Sounds", "Play sounds when you are on a killstreak.", this.killStreakCategory, true);
        this.volumeKS = new ValueNumber("VolumeKS", "Volume", "", this.killStreakCategory, 50.0f, 0.0f, 50.0f);
        this.targets = new ConcurrentHashMap<EntityPlayer, Integer>();
        this.timer = new TimerUtils();
        this.cooldownTimer = new TimerUtils();
        this.killStreak = 0;
        this.killSound = new File(ModuleKillsay.mc.gameDir + File.separator + "Evangelion" + File.separator + "Sounds" + File.separator + this.soundName.getValue());
        ModuleKillsay.deathMessages.add("<player> you have been put to sleep thanks to Evangelion, goodnight!");
        ModuleKillsay.deathMessages.add("Sit <player>, Evangelion owns me and all!");
        ModuleKillsay.deathMessages.add("Well played <player>!");
        ModuleKillsay.deathMessages.add("See you later <player> :P");
        ModuleKillsay.deathMessages.add("<player> zZz >o<");
        ModuleKillsay.deathMessagesJap.add("<player> \u3042\u306a\u305f\u306f\u30a8\u30f4\u30a1\u30f3\u30b2\u30ea\u30aa\u30f3\u306e\u304a\u304b\u3052\u3067\u7720\u308a\u306b\u3064\u3044\u305f\u3001\u304a\u3084\u3059\u307f\u306a\u3055\u3044!");
        ModuleKillsay.deathMessagesJap.add("\u5ea7\u308b <player>, \u30a8\u30f4\u30a1\u30f3\u30b2\u30ea\u30aa\u30f3\u306f\u79c1\u3068\u3059\u3079\u3066\u3092\u6240\u6709\u3057\u3066\u3044\u307e\u3059!");
        ModuleKillsay.deathMessagesJap.add("\u3088\u304f\u904a\u3093\u3060 <player>!");
        ModuleKillsay.deathMessagesJap.add("\u3058\u3083\u3042\u3001\u307e\u305f <player> :P");
        ModuleKillsay.deathMessagesJap.add("<player> zZz >o<");
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
        this.cooldownTimer.reset();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        this.killSound = new File(ModuleKillsay.mc.gameDir + File.separator + "Evangelion" + File.separator + "Sounds" + File.separator + this.soundName.getValue());
        if (ModuleAutoCrystal.INSTANCE.target != null && this.cauraTarget != ModuleAutoCrystal.INSTANCE.target) {
            this.cauraTarget = ModuleAutoCrystal.INSTANCE.target;
        }
        if (!this.cooldown) {
            this.cooldownTimer.reset();
        }
        if (this.cooldownTimer.hasTimeElapsed(this.delay.getValue().intValue() * 1000) && this.cooldown) {
            this.cooldown = false;
            this.cooldownTimer.reset();
        }
        if (ModuleAutoCrystal.INSTANCE.target != null) {
            this.targets.put(ModuleAutoCrystal.INSTANCE.target, (int)(this.timer.timeElapsed() / 1000L));
        }
        this.targets.replaceAll((p, v) -> Integer.valueOf((int)(this.timer.timeElapsed() / 1000L)));
        for (final EntityPlayer player : this.targets.keySet()) {
            if (this.targets.get(player) <= this.reset.getValue().intValue()) {
                continue;
            }
            this.targets.remove(player);
            this.timer.reset();
        }
    }
    
    @SubscribeEvent
    public void onEntityDeath(final EventDeath event) {
        if (this.targets.containsKey(event.getEntity()) && !this.cooldown) {
            if (this.messageKill.getValue()) {
                this.announceDeath(event.getEntity());
            }
            this.cooldown = true;
            this.targets.remove(event.getEntity());
            if (this.soundKill.getValue()) {
                SoundUtils.playSound(this.killSound, -50.0f + this.volumeKill.getValue().floatValue());
            }
            ++this.killStreak;
            if (this.killStreak > 1) {
                if (this.messageKS.getValue()) {
                    ChatUtils.sendMessage("You are on a " + this.killStreak + " Kill Streak");
                }
                if (this.soundKS.getValue()) {
                    SoundUtils.playSound(this.killStreakSound(), -50.0f + this.volumeKS.getValue().floatValue());
                }
            }
        }
        if (event.getEntity() == this.cauraTarget && !this.cooldown) {
            if (this.messageKill.getValue()) {
                this.announceDeath(event.getEntity());
            }
            this.cooldown = true;
            if (this.soundKill.getValue()) {
                SoundUtils.playSound(this.killSound, -50.0f + this.volumeKill.getValue().floatValue());
            }
            ++this.killStreak;
            if (this.killStreak > 1) {
                if (this.messageKS.getValue()) {
                    ChatUtils.sendMessage("You are on a " + this.killStreak + " Kill Streak");
                }
                if (this.soundKS.getValue()) {
                    SoundUtils.playSound(this.killStreakSound(), -50.0f + this.volumeKS.getValue().floatValue());
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getTarget() instanceof EntityPlayer && !Evangelion.FRIEND_MANAGER.isFriend(event.getEntityPlayer().getName())) {
            this.targets.put((EntityPlayer)event.getTarget(), 0);
        }
    }
    
    @SubscribeEvent
    public void onSendAttackPacket(final EventPacketSend event) {
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK && packet.getEntityFromWorld((World)ModuleKillsay.mc.world) instanceof EntityPlayer && !Evangelion.FRIEND_MANAGER.isFriend(((EntityPlayer)packet.getEntityFromWorld((World)ModuleKillsay.mc.world)).getName())) {
            this.targets.put((EntityPlayer)packet.getEntityFromWorld((World)ModuleKillsay.mc.world), 0);
        }
    }
    
    public String returnRandomMessage() {
        final Random random = new Random();
        if (this.japanese.getValue()) {
            return ModuleKillsay.deathMessagesJap.get(random.nextInt(ModuleKillsay.deathMessagesJap.size()));
        }
        return ModuleKillsay.deathMessages.get(random.nextInt(ModuleKillsay.deathMessages.size()));
    }
    
    public void announceDeath(final EntityPlayer target) {
        switch ((modes)this.mode.getValue()) {
            case Default: {
                ModuleKillsay.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greenText.getValue() ? ">" : "") + "GG " + target.getDisplayNameString() + (this.japanese.getValue() ? ", \u30a8\u30f4\u30a1\u30f3\u30b2\u30ea\u30aa\u30f3\u306f\u79c1\u3068\u3059\u3079\u3066\u3092\u6240\u6709\u3057\u3066\u3044\u307e\u3059!" : ", Evangelion owns me and all!")));
                break;
            }
            case Random: {
                ModuleKillsay.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greenText.getValue() ? ">" : "") + this.returnRandomMessage().replaceAll("<player>", target.getDisplayNameString())));
                break;
            }
            case Custom: {
                ModuleKillsay.mc.player.connection.sendPacket((Packet)new CPacketChatMessage((this.greenText.getValue() ? ">" : "") + this.customGG.getValue().replaceAll("<player>", target.getDisplayNameString())));
                break;
            }
        }
    }
    
    @Override
    public void onLogin() {
        super.onLogin();
        this.killStreak = 0;
    }
    
    @Override
    public void onLogout() {
        super.onLogout();
        this.killStreak = 0;
    }
    
    @Override
    public void onDeath() {
        super.onDeath();
        if (this.deathReset.getValue()) {
            this.killStreak = 0;
        }
    }
    
    @Override
    public String getHudInfo() {
        if (this.messageKS.getValue() || this.soundKS.getValue()) {
            return this.killStreak + "";
        }
        return "";
    }
    
    public File killStreakSound() {
        if (this.killStreak == 3) {
            return SoundInitializer.triple_kill;
        }
        if (this.killStreak == 4) {
            return SoundInitializer.over_kill;
        }
        if (this.killStreak == 5) {
            return SoundInitializer.kill_tacular;
        }
        if (this.killStreak > 5) {
            return SoundInitializer.killamonjaro;
        }
        return SoundInitializer.double_kill;
    }
    
    static {
        ModuleKillsay.deathMessages = new ArrayList<String>();
        ModuleKillsay.deathMessagesJap = new ArrayList<String>();
    }
    
    public enum modes
    {
        Default, 
        Random, 
        Custom;
    }
}
