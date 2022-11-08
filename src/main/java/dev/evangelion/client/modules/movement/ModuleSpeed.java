package dev.evangelion.client.modules.movement;

import net.minecraft.entity.Entity;
import dev.evangelion.client.events.EventPlayerMove;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import dev.evangelion.client.events.EventPacketReceive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import dev.evangelion.api.utilities.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import dev.evangelion.client.modules.client.ModuleCommands;
import org.lwjgl.input.Keyboard;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import dev.evangelion.api.utilities.MovementUtils;
import dev.evangelion.api.utilities.TimerUtils;
import dev.evangelion.client.values.impl.ValueBind;
import dev.evangelion.client.values.impl.ValueNumber;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.manager.module.Module;

@RegisterModule(name = "Speed", description = "Makes the player fast.", category = Module.Category.MOVEMENT)
public class ModuleSpeed extends Module
{
    private int stage;
    private double moveSpeed;
    private double speed;
    private double lastDist;
    private float timerSpeed;
    private boolean boost;
    ValueEnum mode;
    ValueEnum timer;
    ValueBoolean inWater;
    ValueBoolean step;
    ValueBoolean abuseMotion;
    ValueCategory strafeCategory;
    ValueNumber strafeSpeed;
    ValueBoolean strafeBoost;
    ValueCategory yPortCategory;
    ValueNumber yPortSpeed;
    ValueNumber dropSpeed;
    ValueNumber jumpDelay;
    ValueNumber airTime;
    ValueCategory changeCategory;
    ValueBind changeBind;
    ValueEnum changeMode;
    TimerUtils jumpTimer;
    TimerUtils airTimer;
    TimerUtils ncpTimer;
    boolean resetJump;
    boolean wasAir;
    float yPortMoveSpeed;
    Modes oldMode;
    boolean changedMode;
    
    public ModuleSpeed() {
        this.stage = 1;
        this.mode = new ValueEnum("Mode", "Mode", "The mode for the speed.", Modes.Strafe);
        this.timer = new ValueEnum("Timer", "Timer", "Use timer to make the speed faster.", TimerModes.Normal);
        this.inWater = new ValueBoolean("SpeedInWater", "Speed In Water", "Speed works when in a liquid.", true);
        this.step = new ValueBoolean("CancelStep", "Cancel Step", "Doesnt use step when strafing.", true);
        this.abuseMotion = new ValueBoolean("AbuseMotion", "Abuse Motion", "Take advantage of the extra motion the player would normally receive.", false);
        this.strafeCategory = new ValueCategory("Strafe", "Strafe category.");
        this.strafeSpeed = new ValueNumber("StrafeSpeed", "Speed", "The speed of the strafe.", this.strafeCategory, 0.272f, 0.25f, 0.35f);
        this.strafeBoost = new ValueBoolean("StrafeBoost", "Boost", "Get a speed boost from time to time.", this.strafeCategory, true);
        this.yPortCategory = new ValueCategory("YPort", "YPort category.");
        this.yPortSpeed = new ValueNumber("YPortSpeed", "Speed", "", this.yPortCategory, 0.03, 0.0, 0.2);
        this.dropSpeed = new ValueNumber("DropSpeed", "Drop Speed", "", this.yPortCategory, 0.5, 0.1, 1.0);
        this.jumpDelay = new ValueNumber("YPortJumpDelay", "Jump Delay", "", this.yPortCategory, 500, 0, 1000);
        this.airTime = new ValueNumber("YPortAirTime", "Air Time", "", this.yPortCategory, 250, 0, 350);
        this.changeCategory = new ValueCategory("ChangeMode", "Change mode category.");
        this.changeBind = new ValueBind("ChangeBind", "Bind", "Bind to change mode from one to another.", this.changeCategory, 0);
        this.changeMode = new ValueEnum("ChangeModeTo", "Change To", "Mode to change to when change bind is pressed.", this.changeCategory, Modes.YPort);
        this.jumpTimer = new TimerUtils();
        this.airTimer = new TimerUtils();
        this.ncpTimer = new TimerUtils();
        this.resetJump = false;
        this.wasAir = false;
        this.oldMode = null;
        this.changedMode = false;
        this.boost = false;
    }
    
    @Override
    public void onEnable() {
        this.timerSpeed = 1.0f;
        this.moveSpeed = MovementUtils.getBaseMoveSpeed(0.272);
        this.yPortMoveSpeed = 0.0f;
        this.speed = this.strafeSpeed.getMinimum().floatValue();
    }
    
    @Override
    public void onDisable() {
        ModuleSpeed.mc.timer.tickLength = 50.0f;
        this.moveSpeed = 0.0;
        this.stage = 2;
        this.yPortMoveSpeed = 0.0f;
        this.speed = this.strafeSpeed.getMinimum().floatValue();
    }
    
    @SubscribeEvent
    public void onKeyInput(final InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0) {
                return;
            }
            if (this.changeBind.getValue() == Keyboard.getEventKey()) {
                if (!this.changedMode) {
                    this.oldMode = (Modes)this.mode.getValue();
                    this.mode.setValue(this.changeMode.getValue());
                    ChatUtils.sendMessage("Changed Mode to " + ModuleCommands.getSecondColor() + "" + ChatFormatting.BOLD + this.mode.getValue().name() + ModuleCommands.getFirstColor() + "!", "Speed", 9182734);
                }
                else {
                    this.mode.setValue(this.oldMode);
                    ChatUtils.sendMessage("Changed Mode to " + ModuleCommands.getSecondColor() + "" + ChatFormatting.BOLD + this.mode.getValue().name() + ModuleCommands.getFirstColor() + "!", "Speed", 9182734);
                }
                this.changedMode = !this.changedMode;
            }
        }
    }
    
    @SubscribeEvent
    public void onReceive(final EventPacketReceive event) {
        if (!this.timer.getValue().equals(TimerModes.None) && event.getPacket() instanceof SPacketPlayerPosLook) {
            this.timerSpeed = 1.0f;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (ModuleSpeed.mc.player != null) {
                this.lastDist = 0.0;
            }
            this.stage = 1;
            this.yPortMoveSpeed = 0.0f;
            this.speed = this.strafeSpeed.getMinimum().floatValue();
        }
    }
    
    @Override
    public void onUpdate() {
        if (ModuleSpeed.mc.player.onGround) {
            if (this.wasAir) {
                this.yPortMoveSpeed = Math.min(this.yPortMoveSpeed + 0.004f, this.yPortSpeed.getValue().floatValue());
                this.speed = Math.min(this.speed + 0.026, this.strafeSpeed.getValue().floatValue());
                this.wasAir = false;
            }
        }
        else if (!this.wasAir) {
            this.wasAir = true;
        }
        this.lastDist = Math.sqrt((ModuleSpeed.mc.player.posX - ModuleSpeed.mc.player.prevPosX) * (ModuleSpeed.mc.player.posX - ModuleSpeed.mc.player.prevPosX) + (ModuleSpeed.mc.player.posZ - ModuleSpeed.mc.player.prevPosZ) * (ModuleSpeed.mc.player.posZ - ModuleSpeed.mc.player.prevPosZ));
        if (!this.timer.getValue().equals(TimerModes.None)) {
            if (MovementUtils.isMoving((EntityPlayer)ModuleSpeed.mc.player)) {
                this.timerSpeed = (this.timer.getValue().equals(TimerModes.Normal) ? 1.1f : 1.0888f);
            }
            else {
                this.timerSpeed = 1.0f;
            }
            ModuleSpeed.mc.timer.tickLength = 50.0f / this.timerSpeed;
        }
        if (this.mode.getValue().equals(Modes.YPort)) {
            final float moveForward = ModuleSpeed.mc.player.movementInput.moveForward;
            final float moveStrafe = ModuleSpeed.mc.player.movementInput.moveStrafe;
            if (ModuleSpeed.mc.player.isSneaking() || ModuleSpeed.mc.player.isOnLadder() || ModuleSpeed.mc.player.isInWeb || ModuleSpeed.mc.player.capabilities.isFlying) {
                return;
            }
            if ((ModuleSpeed.mc.player.isInWater() || ModuleSpeed.mc.player.isInLava()) && !this.inWater.getValue()) {
                return;
            }
            if (moveForward != 0.0f || moveStrafe != 0.0f) {
                if (ModuleSpeed.mc.player.onGround) {
                    if (!this.resetJump) {
                        this.jumpTimer.reset();
                        this.resetJump = true;
                    }
                    if (this.jumpTimer.hasTimeElapsed(this.jumpDelay.getValue().intValue()) && this.resetJump) {
                        this.airTimer.reset();
                        ModuleSpeed.mc.player.jump();
                    }
                }
                else {
                    this.resetJump = false;
                    if (this.airTimer.hasTimeElapsed(this.airTime.getValue().intValue())) {
                        final EntityPlayerSP player = ModuleSpeed.mc.player;
                        player.motionY -= this.dropSpeed.getValue().floatValue();
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onMove(final EventPlayerMove event) {
        float moveForward = ModuleSpeed.mc.player.movementInput.moveForward;
        float moveStrafe = ModuleSpeed.mc.player.movementInput.moveStrafe;
        float rotationYaw = ModuleSpeed.mc.player.rotationYaw;
        if (ModuleSpeed.mc.player.isSneaking() || ModuleSpeed.mc.player.isOnLadder() || ModuleSpeed.mc.player.isInWeb || ModuleSpeed.mc.player.capabilities.isFlying) {
            return;
        }
        if ((ModuleSpeed.mc.player.isInWater() || ModuleSpeed.mc.player.isInLava()) && !this.inWater.getValue()) {
            return;
        }
        if (this.mode.getValue().equals(Modes.Strafe) || this.mode.getValue().equals(Modes.StrafeStrict)) {
            if (this.stage == 1 && MovementUtils.isMoving((EntityPlayer)ModuleSpeed.mc.player)) {
                this.moveSpeed = 1.35 * MovementUtils.getBaseMoveSpeed(this.speed) - 0.01;
            }
            else if (this.stage == 2 && MovementUtils.isMoving((EntityPlayer)ModuleSpeed.mc.player)) {
                event.setY(ModuleSpeed.mc.player.motionY = (this.mode.getValue().equals(Modes.Strafe) ? 0.42 : 0.3994) + MovementUtils.getJumpBoost());
                this.moveSpeed *= ((this.boost && this.strafeBoost.getValue()) ? (this.mode.getValue().equals(Modes.Strafe) ? 1.6835 : 1.42) : 1.395);
            }
            else if (this.stage == 3) {
                final double difference = 0.66 * (this.lastDist - MovementUtils.getBaseMoveSpeed(this.speed));
                this.moveSpeed = this.lastDist - difference;
                this.boost = !this.boost;
            }
            else {
                if (ModuleSpeed.mc.world.getCollisionBoxes((Entity)ModuleSpeed.mc.player, ModuleSpeed.mc.player.getEntityBoundingBox().offset(0.0, ModuleSpeed.mc.player.motionY, 0.0)).size() > 0 || (ModuleSpeed.mc.player.collidedVertically && this.stage > 0)) {
                    this.stage = (MovementUtils.isMoving((EntityPlayer)ModuleSpeed.mc.player) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed(this.speed));
            if (this.mode.getValue().equals(Modes.StrafeStrict)) {
                if (this.ncpTimer.hasTimeElapsed(2500L)) {
                    this.ncpTimer.reset();
                }
                final double minSpeed = this.ncpTimer.hasTimeElapsed(1250L) ? 0.465 : 0.44;
                this.moveSpeed = Math.min(this.moveSpeed, minSpeed);
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
                this.moveSpeed = 0.0;
            }
            else if (moveForward != 0.0f) {
                if (moveStrafe >= 1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? -45.0f : 45.0f);
                    moveStrafe = 0.0f;
                }
                else if (moveStrafe <= -1.0f) {
                    rotationYaw += ((moveForward > 0.0f) ? 45.0f : -45.0f);
                    moveStrafe = 0.0f;
                }
                if (moveForward > 0.0f) {
                    moveForward = 1.0f;
                }
                else if (moveForward < 0.0f) {
                    moveForward = -1.0f;
                }
            }
            final double motionX = Math.cos(Math.toRadians(rotationYaw + 90.0f));
            final double motionZ = Math.sin(Math.toRadians(rotationYaw + 90.0f));
            event.setX(moveForward * this.moveSpeed * motionX + moveStrafe * this.moveSpeed * motionZ);
            event.setZ(moveForward * this.moveSpeed * motionZ - moveStrafe * this.moveSpeed * motionX);
            if (this.step.getValue()) {
                ModuleSpeed.mc.player.stepHeight = 0.6f;
            }
            if (moveForward == 0.0f && moveStrafe == 0.0f) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            if (MovementUtils.isMoving((EntityPlayer)ModuleSpeed.mc.player)) {
                ++this.stage;
            }
        }
        else if (this.mode.getValue().equals(Modes.YPort)) {
            MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed(0.272) + this.yPortMoveSpeed);
        }
        else if (this.mode.getValue().equals(Modes.Instant)) {
            MovementUtils.strafe(event, MovementUtils.getBaseMoveSpeed(0.272));
        }
        event.setCancelled(true);
    }
    
    @Override
    public String getHudInfo() {
        return this.mode.getValue().name();
    }
    
    public enum Modes
    {
        Strafe, 
        StrafeStrict, 
        YPort, 
        Instant;
    }
    
    public enum TimerModes
    {
        Normal, 
        Strict, 
        None;
    }
}
