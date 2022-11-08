package dev.evangelion.client.modules.combat.autocrystal;


import dev.evangelion.Evangelion;
import dev.evangelion.api.manager.module.Module;
import dev.evangelion.api.manager.module.RegisterModule;
import dev.evangelion.api.utilities.DamageUtils;
import dev.evangelion.api.utilities.InventoryUtils;
import dev.evangelion.api.utilities.MathUtils;
import dev.evangelion.api.utilities.MovementUtils;
import dev.evangelion.api.utilities.PlayerUtils;
import dev.evangelion.api.utilities.RenderUtils;
import dev.evangelion.api.utilities.RotationUtils;
import dev.evangelion.client.events.EventMotion;
import dev.evangelion.client.events.EventPacketReceive;
import dev.evangelion.client.events.EventPacketSend;
import dev.evangelion.client.events.EventRender3D;
import dev.evangelion.client.modules.client.ModuleRotations;
import dev.evangelion.client.modules.combat.autocrystal.ObiPlace;
import dev.evangelion.client.values.impl.ValueBoolean;
import dev.evangelion.client.values.impl.ValueCategory;
import dev.evangelion.client.values.impl.ValueColor;
import dev.evangelion.client.values.impl.ValueEnum;
import dev.evangelion.client.values.impl.ValueNumber;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterModule(name="AutoCrystal", tag="Auto Crystal", description="Automatically places and breaks crystals in order to kill your enemy.", category=Module.Category.COMBAT)
public class ModuleAutoCrystal
        extends Module {
    public static ModuleAutoCrystal INSTANCE;
    private final ValueEnum switchMode = new ValueEnum("SwitchMode", "Switch", "Mode fro switching to crystal.", SwitchModes.Normal);
    private final ValueCategory breakCategory = new ValueCategory("Break", "The category for crystal breaking.");
    private final ValueBoolean breakMode = new ValueBoolean("Break", "Break", "Breaks the crystals.", this.breakCategory, true);
    private final ValueNumber breakDelay = new ValueNumber("BreakDelay", "Delay", "The delay for the breaking.", this.breakCategory, 0, 0, 20);
    private final ValueNumber breakRange = new ValueNumber("BreakRange", "Range", "The range for breaking.", this.breakCategory, Float.valueOf(5.0f), Float.valueOf(0.0f), Float.valueOf(6.0f));
    private final ValueNumber breakWallsRange = new ValueNumber("BreakWallsRange", "Walls Range", "The range for breaking through walls.", this.breakCategory, Float.valueOf(3.5f), Float.valueOf(0.0f), Float.valueOf(6.0f));
    private final ValueBoolean predict = new ValueBoolean("Predict", "Predict", "Breaks crystals when they spawn. Good for non-strict servers.", this.breakCategory, true);
    private final ValueBoolean inhibit = new ValueBoolean("Inhibit", "Inhibit", "Doesn't attack crystals that are going to explode.", this.breakCategory, true);
    private final ValueEnum syncMode = new ValueEnum("Sync", "Sync", "The mode for syncing the crystals to prevent desync.", this.breakCategory, SyncModes.Sound);
    private final ValueCategory placeCategory = new ValueCategory("Place", "The category for crystal placing.");
    private final ValueBoolean placeMode = new ValueBoolean("Place", "Place", "Places crystals at the right opportunity.", this.placeCategory, true);
    private final ValueNumber placeDelay = new ValueNumber("PlaceDelay", "Delay", "The delay for the placing.", this.placeCategory, 0, 0, 20);
    private final ValueNumber placeRange = new ValueNumber("PlaceRange", "Range", "The range for placing.", this.placeCategory, Float.valueOf(5.0f), Float.valueOf(0.0f), Float.valueOf(6.0f));
    private final ValueEnum placements = new ValueEnum("Placements", "Placements", "The crystal placements.", this.placeCategory, PlacementModes.Native);
    private final ValueEnum multiPlace = new ValueEnum("MultiPlace", "Multi Place", "Places crystals in more than one position.", this.placeCategory, MultiPlaceModes.None);
    private final ValueBoolean holePlace = new ValueBoolean("HolePlace", "Holes", "Places in the hole where a player has jumped.", this.placeCategory, true);
    private final ValueCategory targetCategory = new ValueCategory("Target", "The category for Targeting.");
    public final ValueNumber targetRange = new ValueNumber("TargetRange", "Range", "The range for the Targeting.", this.targetCategory, Float.valueOf(10.0f), Float.valueOf(1.0f), Float.valueOf(30.0f));
    private final ValueCategory miscellaneousCategory = new ValueCategory("Miscellaneous", "The category for miscellaneous settings.");
    private final ValueEnum timing = new ValueEnum("Timing", "Timing", "The timing for the auto crystal.", this.miscellaneousCategory, TimingModes.Break);
    private final ValueCategory damageCateory = new ValueCategory("Damage", "The category for Damage.");
    private final ValueNumber minimumDamage = new ValueNumber("MinimumDamage", "Minimum", "The minimum damage for the breaking and placing.", this.damageCateory, Float.valueOf(6.0f), Float.valueOf(0.0f), Float.valueOf(36.0f));
    private final ValueNumber maximumSelfDamage = new ValueNumber("MaximumSelfDamage", "Maximum Self", "The maximum self damage for the breaking and placing.", this.damageCateory, Float.valueOf(12.0f), Float.valueOf(0.0f), Float.valueOf(36.0f));
    private final ValueBoolean healthIgnore = new ValueBoolean("HealthIgnore", "Health Ignore", "At a certain point of player health ignore current minimum damage.", this.damageCateory, false);
    private final ValueNumber ignoreHP = new ValueNumber("IgnoreHP", "Ignore HP", "The hp of the target to ignore the current minimum damage.", this.damageCateory, Float.valueOf(3.0f), Float.valueOf(0.0f), Float.valueOf(36.0f));
    private final ValueNumber ignoreDamage = new ValueNumber("IgnoreMinimumDamage", "Ignore Minimum", "The minimum damage for the breaking and placing when ignoring old minimum damage.", this.damageCateory, Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(36.0f));
    private final ValueCategory armorBreakerCategory = new ValueCategory("ArmorBreaker", "Armor breaker category.");
    private final ValueBoolean armorBreaker = new ValueBoolean("ArmorBreaker", "Armor Breaker", "Break armor when it has certain health.", this.armorBreakerCategory, false);
    private final ValueNumber armorHealth = new ValueNumber("ArmorHealth", "Health", "Health to begin armor breaker.", this.armorBreakerCategory, Float.valueOf(30.0f), Float.valueOf(0.0f), Float.valueOf(100.0f));
    private final ValueCategory obiPlaceCategory = new ValueCategory("ObiPlace", "Obi Place");
    public ValueBoolean obiSupport = new ValueBoolean("ObiPlaceToggle", "Obi Place", "Place obsidian support blocks if no where to place.", this.obiPlaceCategory, false);
    public ValueNumber palceReach = new ValueNumber("PlaceReach", "Place Reach", "", this.obiPlaceCategory, 5.0, 2.0, 6.0);
    public ValueNumber blockDistance = new ValueNumber("BlockDistance", "Block Distance", "BlockDistance", this.obiPlaceCategory, 4, 2, 6);
    private final ValueCategory renderCategory = new ValueCategory("Render", "The category for rendering.");
    private final ValueEnum renderMode = new ValueEnum("Render", "Render", "The mode for rendering the block positions.", this.renderCategory, RenderModes.Normal);
    private final ValueEnum fill = new ValueEnum("Fill", "Fill", "The mode for filling in the position.", this.renderCategory, Renders.Normal);
    private final ValueEnum outline = new ValueEnum("Outline", "Outline", "The mode for outlining the position.", this.renderCategory, Renders.Normal);
    private final ValueBoolean renderDamage = new ValueBoolean("RenderDamage", "Damage", "Renders the damage that the position is gonna cause to the target.", this.renderCategory, false);
    private final ValueNumber lineWidth = new ValueNumber("LineWidth", "Width", "The width for the outline.", this.renderCategory, Float.valueOf(1.0f), Float.valueOf(0.0f), Float.valueOf(5.0f));
    private final ValueColor fillColor = new ValueColor("FillColor", "Fill Color", "The color for the filling.", this.renderCategory, new Color(0, 0, 255, 100));
    private final ValueColor outlineColor = new ValueColor("OutlineColor", "Outline Color", "The color for the outlining.", this.renderCategory, new Color(0, 0, 255));
    private final ValueNumber shrinkSpeed = new ValueNumber("ShrinkSpeed", "Shrink Speed", "The speed for the shrinking.", this.renderCategory, 150, 10, 500);
    private final ValueNumber fadeDuration = new ValueNumber("FadeDuration", "Fade Duration", "The duration of the fade.", this.renderCategory, 15, 1, 50);
    ValueCategory devCategory = new ValueCategory("Dev", "Developer category.");
    ValueBoolean movementPredict = new ValueBoolean("MovementPredict", "Movement Predict", "Tries to predict the movement of the target.", this.devCategory, false);
    ValueNumber predictSeconds = new ValueNumber("PredictSeconds", "Predict Seconds", "Calculate future position in given time frame.", this.devCategory, Float.valueOf(2.0f), Float.valueOf(1.0f), Float.valueOf(4.0f));
    ValueBoolean predictRender = new ValueBoolean("PredictRender", "Predict Render", "Renders the movement prediction.", this.devCategory, false);
    private int breakTicks = 0;
    private int placeTicks = 0;
    private final ArrayList<Integer> blacklist = new ArrayList();
    public EntityPlayer target = null;
    private final List<FadePosition> fadePositions = new ArrayList<FadePosition>();
    public static BlockPos renderPosition;
    private float damageNumber = 0.0f;
    public static EntityPlayer obiTarget;
    public static boolean placed;
    public static int oldSlot;
    public static BlockPos currentPos;
    ObiPlace obiPlace;

    public ModuleAutoCrystal() {
        INSTANCE = this;
    }

    @SubscribeEvent
    public void onMotion(EventMotion event) {
        super.onUpdate();
        if (this.nullCheck()) {
            return;
        }
        this.obiPlace = new ObiPlace();
        if (this.obiPlace != null && this.obiSupport.getValue()) {
            this.obiPlace.doObiPlace(event);
        }
        switch ((TimingModes)this.timing.getValue()) {
            case Break: {
                if (this.breakMode.getValue() && this.breakTicks++ > this.breakDelay.getValue().intValue()) {
                    this.breakCrystal(event);
                }
                if (this.placeMode.getValue() && this.placeTicks++ > this.placeDelay.getValue().intValue()) {
                    this.placeCrystal(event);
                }
            }
            case Place: {
                if (this.placeMode.getValue() && this.placeTicks++ > this.placeDelay.getValue().intValue()) {
                    this.placeCrystal(event);
                }
                if (!this.breakMode.getValue() || this.breakTicks++ <= this.breakDelay.getValue().intValue()) break;
                this.breakCrystal(event);
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        placed = false;
    }

    private void breakCrystal(final EventMotion event) {
        this.breakTicks = 0;
        EntityEnderCrystal optimalCrystal = null;
        float maxDamage = 0.0f;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(ModuleAutoCrystal.mc.world.playerEntities)) {
            if (this.isInvalidTarget(player)) {
                continue;
            }
            for (final Entity entity : new ArrayList<Entity>(ModuleAutoCrystal.mc.world.loadedEntityList)) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                final EntityEnderCrystal crystal = (EntityEnderCrystal)entity;
                float targetDamage = 0.0f;
                if (this.movementPredict.getValue()) {
                    final AxisAlignedBB oldBB = player.boundingBox;
                    player.boundingBox = MovementUtils.predictMovement(player, this.predictSeconds.getValue().floatValue());
                    player.resetPositionToBB();
                    targetDamage = this.filterCrystal(crystal, player);
                    player.boundingBox = oldBB;
                    player.resetPositionToBB();
                }
                else {
                    targetDamage = this.filterCrystal(crystal, player);
                }
                if (targetDamage == -1.0f) {
                    continue;
                }
                if (this.blacklist.contains(crystal.entityId) && this.inhibit.getValue()) {
                    continue;
                }
                if (targetDamage <= maxDamage) {
                    continue;
                }
                maxDamage = targetDamage;
                optimalCrystal = crystal;
                this.target = player;
            }
        }
        if (optimalCrystal == null) {
            return;
        }
        if (Evangelion.MODULE_MANAGER.isModuleEnabled("Rotations")) {
            final float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotationsEntity((Entity)optimalCrystal), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
            RotationUtils.rotate(event, rot);
        }
        ModuleAutoCrystal.mc.playerController.attackEntity((EntityPlayer)ModuleAutoCrystal.mc.player, (Entity)optimalCrystal);
        this.blacklist.add(optimalCrystal.entityId);
        ModuleAutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public void placeCrystal(final EventMotion event) {
        this.placeTicks = 0;
        BlockPos optimalPosition = null;
        float maxDamage = 0.0f;
        for (final EntityPlayer player : new ArrayList<EntityPlayer>(ModuleAutoCrystal.mc.world.playerEntities)) {
            if (this.isInvalidTarget(player)) {
                continue;
            }
            for (final BlockPos position : this.getPositions(player)) {
                float targetDamage = 0.0f;
                if (this.movementPredict.getValue()) {
                    final AxisAlignedBB oldBB = player.boundingBox;
                    player.boundingBox = MovementUtils.predictMovement(player, this.predictSeconds.getValue().floatValue());
                    player.resetPositionToBB();
                    targetDamage = this.filterPosition(position, player);
                    player.boundingBox = oldBB;
                    player.resetPositionToBB();
                }
                else {
                    targetDamage = this.filterPosition(position, player);
                }
                if (targetDamage == -1.0f) {
                    continue;
                }
                if (targetDamage <= maxDamage) {
                    continue;
                }
                maxDamage = targetDamage;
                optimalPosition = position;
                this.damageNumber = targetDamage;
                this.target = player;
            }
        }
        if (optimalPosition == null) {
            ModuleAutoCrystal.renderPosition = null;
            return;
        }
        final int slot = InventoryUtils.findItem(Items.END_CRYSTAL, 0, 9);
        final int lastSlot = ModuleAutoCrystal.mc.player.inventory.currentItem;
        if (!this.switchMode.getValue().equals(SwitchModes.None) && slot != -1) {
            if (this.switchMode.getValue().equals(SwitchModes.Normal)) {
                if (ModuleAutoCrystal.mc.player.inventory.getCurrentItem().getItem() != Items.END_CRYSTAL) {
                    InventoryUtils.switchSlot(slot, this.switchMode.getValue().equals(SwitchModes.Silent));
                }
            }
            else {
                InventoryUtils.switchSlot(slot, this.switchMode.getValue().equals(SwitchModes.Silent));
            }
        }
        if (ModuleAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || ModuleAutoCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || this.switchMode.getValue().equals(SwitchModes.Silent)) {
            if (Evangelion.MODULE_MANAGER.isModuleEnabled("Rotations")) {
                final float[] rot = RotationUtils.getSmoothRotations(RotationUtils.getRotations(optimalPosition.getX(), optimalPosition.getY(), optimalPosition.getZ()), ModuleRotations.INSTANCE.smoothness.getValue().intValue());
                RotationUtils.rotate(event, rot);
            }
            ModuleAutoCrystal.renderPosition = optimalPosition;
            ModuleAutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(optimalPosition, EnumFacing.UP, this.switchMode.getValue().equals(SwitchModes.Silent) ? EnumHand.MAIN_HAND : ((ModuleAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND), 0.5f, 0.5f, 0.5f));
            ModuleAutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
        else {
            ModuleAutoCrystal.renderPosition = null;
        }
        if (!this.switchMode.getValue().equals(SwitchModes.None) && lastSlot != -1 && !this.switchMode.getValue().equals(SwitchModes.Normal)) {
            InventoryUtils.switchSlot(lastSlot, this.switchMode.getValue().equals(SwitchModes.Silent));
        }
    }

    @Override
    public void onRender3D(EventRender3D event) {
        super.onRender3D(event);
        if (this.target != null && this.predictRender.getValue()) {
            AxisAlignedBB predictedBB = MovementUtils.predictMovement(this.target, this.predictSeconds.getValue().floatValue());
            predictedBB = RenderUtils.getRenderBB((Object)predictedBB);
            RenderUtils.drawBlockOutline(predictedBB, this.outlineColor.getValue(), 1.0f);
        }
        if (!this.renderMode.getValue().equals((Object)RenderModes.None)) {
            if (this.renderMode.getValue().equals((Object)RenderModes.Normal) || this.renderMode.getValue().equals((Object)RenderModes.Fade) || this.renderMode.getValue() == RenderModes.Shrink) {
                if (renderPosition != null) {
                    if (this.renderMode.getValue() == RenderModes.Fade || this.renderMode.getValue() == RenderModes.Shrink) {
                        this.fadePositions.removeIf(pos -> ((FadePosition)pos).position.equals((Object)renderPosition));
                        this.fadePositions.add(new FadePosition(renderPosition));
                    }
                    if (this.fill.getValue().equals((Object)Renders.Normal)) {
                        RenderUtils.drawBlock(renderPosition, this.fillColor.getValue());
                    }
                    if (this.outline.getValue().equals((Object)Renders.Normal)) {
                        RenderUtils.drawBlockOutline(new AxisAlignedBB((double)renderPosition.getX() - ModuleAutoCrystal.mc.getRenderManager().viewerPosX, (double)renderPosition.getY() - ModuleAutoCrystal.mc.getRenderManager().viewerPosY, (double)renderPosition.getZ() - ModuleAutoCrystal.mc.getRenderManager().viewerPosZ, (double)(renderPosition.getX() + 1) - ModuleAutoCrystal.mc.getRenderManager().viewerPosX, (double)(renderPosition.getY() + 1) - ModuleAutoCrystal.mc.getRenderManager().viewerPosY, (double)(renderPosition.getZ() + 1) - ModuleAutoCrystal.mc.getRenderManager().viewerPosZ), this.outlineColor.getValue(), this.lineWidth.getValue().floatValue());
                    }
                }
                if (this.renderMode.getValue().equals((Object)RenderModes.Fade)) {
                    this.fadePositions.forEach(pos -> {
                        long time;
                        long duration;
                        if (!pos.getPosition().equals((Object)renderPosition) && (duration = (time = System.currentTimeMillis()) - pos.getStartTime()) < (long)this.fadeDuration.getValue().intValue() * 100L) {
                            float opacity = (float)this.fillColor.getValue().getAlpha() / 255.0f - (float)duration / (float)((long)this.fadeDuration.getValue().intValue() * 100L);
                            int alpha = MathHelper.clamp((int)((int)(opacity * 255.0f)), (int)0, (int)255);
                            if (this.fill.getValue().equals((Object)Renders.Normal)) {
                                RenderUtils.drawBlock(pos.getPosition(), new Color(this.fillColor.getValue().getRed(), this.fillColor.getValue().getGreen(), this.fillColor.getValue().getBlue(), alpha));
                            }
                            if (this.outline.getValue().equals((Object)Renders.Normal)) {
                                RenderUtils.drawBlockOutline(pos.getPosition(), new Color(this.outlineColor.getValue().getRed(), this.outlineColor.getValue().getGreen(), this.outlineColor.getValue().getBlue(), alpha), this.lineWidth.getValue().floatValue());
                            }
                        }
                    });
                    this.fadePositions.removeIf(fadePosition -> System.currentTimeMillis() - fadePosition.getStartTime() >= (long)this.fadeDuration.getValue().intValue() * 100L);
                } else if (this.renderMode.getValue().equals((Object)RenderModes.Shrink)) {
                    this.fadePositions.stream().distinct().forEach(p -> {
                        if (!((FadePosition)p).position.equals((Object)renderPosition)) {
                            AxisAlignedBB bb = RenderUtils.getRenderBB((Object)((FadePosition)p).position);
                            long time = System.currentTimeMillis();
                            long duration = time - ((FadePosition)p).startTime;
                            float startAlpha = (float)this.fillColor.getValue().getAlpha() / 255.0f;
                            if (duration < (long)this.shrinkSpeed.getValue().intValue() * 10L) {
                                float opacity = startAlpha - (float)duration / (float)(this.shrinkSpeed.getValue().intValue() * 10);
                                opacity = MathHelper.clamp((float)opacity, (float)-1.0f, (float)0.0f);
                                bb = bb.shrink((double)(-opacity));
                                RenderUtils.drawBlock(bb, this.fillColor.getValue());
                                RenderUtils.drawBlockOutline(bb, this.outlineColor.getValue(), this.lineWidth.getValue().floatValue());
                            }
                        }
                    });
                    this.fadePositions.removeIf(p -> System.currentTimeMillis() - ((FadePosition)p).startTime >= (long)this.shrinkSpeed.getValue().intValue() * 10L || ModuleAutoCrystal.mc.world.getBlockState(((FadePosition)p).position).getBlock() == Blocks.AIR);
                }
            }
            if (this.renderDamage.getValue() && renderPosition != null) {
                RenderUtils.drawText(renderPosition, (Math.floor(this.damageNumber) == (double)this.damageNumber ? Integer.valueOf((int)this.damageNumber) : String.format("%.1f", Float.valueOf(this.damageNumber))) + "");
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(EventPacketSend event) {
        CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && this.syncMode.getValue().equals((Object)SyncModes.Instant) && (packet = (CPacketUseEntity)event.getPacket()).getEntityFromWorld((World)ModuleAutoCrystal.mc.world) instanceof EntityEnderCrystal) {
            Objects.requireNonNull(packet.getEntityFromWorld((World)ModuleAutoCrystal.mc.world)).setDead();
            ModuleAutoCrystal.mc.world.removeEntityFromWorld(packet.entityId);
        }
    }

    @SubscribeEvent
    public void onPacketReceive(final EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketSoundEffect && this.syncMode.getValue().equals(SyncModes.Sound)) {
            final SPacketSoundEffect packet = (SPacketSoundEffect)event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity entity : new ArrayList<Entity>(ModuleAutoCrystal.mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= 36.0) {
                        entity.setDead();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPredict(EventPacketReceive event) {
        if (event.getPacket() instanceof SPacketSpawnObject && this.predict.getValue() && this.breakMode.getValue()) {
            SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if (packet.getType() != 51) {
                return;
            }
            if (this.target == null) {
                return;
            }
            EntityEnderCrystal crystal = new EntityEnderCrystal((World)ModuleAutoCrystal.mc.world, packet.getX(), packet.getY(), packet.getZ());
            if (this.blacklist.contains(packet.getEntityID()) && this.inhibit.getValue()) {
                return;
            }
            if (this.filterCrystal(crystal, this.target) == -1.0f) {
                return;
            }
            CPacketUseEntity crystalPacket = new CPacketUseEntity();
            crystalPacket.entityId = packet.getEntityID();
            crystalPacket.action = CPacketUseEntity.Action.ATTACK;
            ModuleAutoCrystal.mc.player.connection.sendPacket((Packet)crystalPacket);
            if (ModuleAutoCrystal.mc.playerController.getCurrentGameType() != GameType.SPECTATOR) {
                ModuleAutoCrystal.mc.player.resetCooldown();
            }
            ModuleAutoCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
            this.blacklist.add(packet.getEntityID());
        }
    }

    private float filterCrystal(EntityEnderCrystal crystal, EntityPlayer player) {
        double d = ModuleAutoCrystal.mc.player.getDistanceSq((Entity)crystal);
        float f = ModuleAutoCrystal.mc.player.canEntityBeSeen((Entity)crystal) ? this.breakRange.getValue().floatValue() : this.breakWallsRange.getValue().floatValue();
        if (d > (double)MathUtils.square(f)) {
            return -1.0f;
        }
        if (crystal.isDead) {
            return -1.0f;
        }
        float targetDamage = DamageUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)player);
        float selfDamage = DamageUtils.calculateDamage(crystal.posX, crystal.posY, crystal.posZ, (EntityLivingBase)ModuleAutoCrystal.mc.player);
        float f2 = this.healthIgnore.getValue() && this.shouldHealthIgnore(player) ? this.ignoreDamage.getValue().floatValue() : (this.armorBreaker.getValue() && DamageUtils.shouldBreakArmor((EntityLivingBase)player, this.armorHealth.getValue().floatValue()) ? 0.0f : this.minimumDamage.getValue().floatValue());
        if (targetDamage < f2) {
            return -1.0f;
        }
        if (selfDamage > this.maximumSelfDamage.getValue().floatValue()) {
            return -1.0f;
        }
        return targetDamage;
    }

    private float filterPosition(BlockPos position, EntityPlayer player) {
        if (ModuleAutoCrystal.mc.player.getDistanceSq(position) > (double)MathUtils.square(this.placeRange.getValue().floatValue())) {
            return -1.0f;
        }
        float targetDamage = DamageUtils.calculateDamage((double)position.getX() + 0.5, (double)position.getY() + 1.0, (double)position.getZ() + 0.5, (EntityLivingBase)player);
        float selfDamage = DamageUtils.calculateDamage((double)position.getX() + 0.5, (double)position.getY() + 1.0, (double)position.getZ() + 0.5, (EntityLivingBase)ModuleAutoCrystal.mc.player);
        float f = this.healthIgnore.getValue() && this.shouldHealthIgnore(player) ? this.ignoreDamage.getValue().floatValue() : (this.armorBreaker.getValue() && DamageUtils.shouldBreakArmor((EntityLivingBase)player, this.armorHealth.getValue().floatValue()) ? 0.0f : this.minimumDamage.getValue().floatValue());
        if (targetDamage < f) {
            return -1.0f;
        }
        if (selfDamage > this.maximumSelfDamage.getValue().floatValue()) {
            return -1.0f;
        }
        return targetDamage;
    }

    private boolean isInvalidTarget(EntityPlayer player) {
        if (ModuleAutoCrystal.mc.player.getDistanceSq((Entity)player) > (double)MathUtils.square(this.targetRange.getValue().floatValue())) {
            return true;
        }
        if (player == ModuleAutoCrystal.mc.player) {
            return true;
        }
        if (player.getName().equals(ModuleAutoCrystal.mc.player.getName())) {
            return true;
        }
        if (player.getHealth() <= 0.0f) {
            return true;
        }
        if (player.isDead) {
            return true;
        }
        return Evangelion.FRIEND_MANAGER.isFriend(player.getName());
    }

    public ArrayList<BlockPos> getPositions(EntityPlayer player) {
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (BlockPos position : PlayerUtils.getSphere(this.placeRange.getValue().floatValue(), true, false)) {
            if (ModuleAutoCrystal.mc.world.getBlockState(position).getBlock() == Blocks.AIR || !ModuleAutoCrystal.canPlaceCrystal(position, this.placements.getValue().equals((Object)PlacementModes.Protocol), this.multiPlace.getValue().equals((Object)MultiPlaceModes.Static) || this.multiPlace.getValue().equals((Object)MultiPlaceModes.Dynamic) && ModuleAutoCrystal.isEntityMoving((EntityLivingBase)ModuleAutoCrystal.mc.player) && ModuleAutoCrystal.isEntityMoving((EntityLivingBase)player), this.holePlace.getValue())) continue;
            positions.add(position);
        }
        return positions;
    }

    public static boolean isEntityMoving(EntityLivingBase entity) {
        return entity.motionX > 2.0 || entity.motionY > 2.0 || entity.motionZ > 2.0;
    }

    public static boolean canPlaceCrystal(BlockPos position, boolean placeUnderBlock, boolean multiPlace, boolean holePlace) {
        if (ModuleAutoCrystal.mc.world.getBlockState(position).getBlock() != Blocks.BEDROCK && ModuleAutoCrystal.mc.world.getBlockState(position).getBlock() != Blocks.OBSIDIAN) {
            return false;
        }
        if (ModuleAutoCrystal.mc.world.getBlockState(position.add(0, 1, 0)).getBlock() != Blocks.AIR || !placeUnderBlock && ModuleAutoCrystal.mc.world.getBlockState(position.add(0, 2, 0)).getBlock() != Blocks.AIR) {
            return false;
        }
        if (multiPlace) {
            return ModuleAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 1, 0))).isEmpty() && !placeUnderBlock && ModuleAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 2, 0))).isEmpty();
        }
        for (Entity entity : ModuleAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 1, 0)))) {
            if (entity instanceof EntityEnderCrystal) continue;
            return false;
        }
        if (!placeUnderBlock) {
            for (Entity entity : ModuleAutoCrystal.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(position.add(0, 2, 0)))) {
                if (entity instanceof EntityEnderCrystal || holePlace && entity instanceof EntityPlayer) continue;
                return false;
            }
        }
        return true;
    }

    public boolean shouldHealthIgnore(EntityPlayer entity) {
        return entity.getHealth() + entity.getAbsorptionAmount() <= this.ignoreHP.getValue().floatValue();
    }

    static {
        renderPosition = null;
        placed = false;
        oldSlot = -1;
        currentPos = null;
    }

    public static enum Renders {
        None,
        Normal;

    }

    public static enum RenderModes {
        None,
        Normal,
        Fade,
        Shrink;

    }

    public static enum TimingModes {
        Break,
        Place;

    }

    public static enum MultiPlaceModes {
        None,
        Dynamic,
        Static;

    }

    public static enum PlacementModes {
        Native,
        Protocol;

    }

    public static enum SyncModes {
        None,
        Sound,
        Instant;

    }

    public static class FadePosition {
        private final BlockPos position;
        private final long startTime;

        public FadePosition(BlockPos position) {
            this.position = position;
            this.startTime = System.currentTimeMillis();
        }

        public BlockPos getPosition() {
            return this.position;
        }

        public long getStartTime() {
            return this.startTime;
        }
    }

    public static enum SwitchModes {
        Normal,
        Silent,
        None;

    }
}

