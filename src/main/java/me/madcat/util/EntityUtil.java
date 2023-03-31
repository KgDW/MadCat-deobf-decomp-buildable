package me.madcat.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.madcat.MadCat;
import me.madcat.util.BlockUtil;
import me.madcat.util.MathUtil;
import me.madcat.util.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityUtil
implements Wrapper {
    public static final Vec3d[] doubleLegOffsetList = new Vec3d[]{new Vec3d(-1.0, 0.0, 0.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(-2.0, 0.0, 0.0), new Vec3d(2.0, 0.0, 0.0), new Vec3d(0.0, 0.0, -2.0), new Vec3d(0.0, 0.0, 2.0)};

    public static boolean isSafe(Entity entity) {
        return EntityUtil.isSafe(entity, 0, false);
    }

    public static boolean isInLiquid() {
        if (EntityUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        boolean bl = false;
        AxisAlignedBB axisAlignedBB = EntityUtil.mc.player.getRidingEntity() != null ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox() : EntityUtil.mc.player.getEntityBoundingBox();
        int n = (int)axisAlignedBB.minY;
        for (int i = MathHelper.floor(axisAlignedBB.minX); i < MathHelper.floor(axisAlignedBB.maxX) + 1; ++i) {
            for (int j = MathHelper.floor(axisAlignedBB.minZ); j < MathHelper.floor(axisAlignedBB.maxZ) + 1; ++j) {
                Block block = EntityUtil.mc.world.getBlockState(new BlockPos(i, n, j)).getBlock();
                if (block instanceof BlockAir) {
                    continue;
                }
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static boolean isEntityMoving(Entity entity) {
        if (entity == null) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return EntityUtil.mc.gameSettings.keyBindForward.isKeyDown() || EntityUtil.mc.gameSettings.keyBindBack.isKeyDown() || EntityUtil.mc.gameSettings.keyBindLeft.isKeyDown() || EntityUtil.mc.gameSettings.keyBindRight.isKeyDown();
        }
        return entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0;
    }

    public static boolean isLiving(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static boolean isOnLiquid() {
        double d = EntityUtil.mc.player.posY - 0.03;
        for (int i = MathHelper.floor(EntityUtil.mc.player.posX); i < MathHelper.ceil(EntityUtil.mc.player.posX); ++i) {
            for (int j = MathHelper.floor(EntityUtil.mc.player.posZ); j < MathHelper.ceil(EntityUtil.mc.player.posZ); ++j) {
                BlockPos blockPos = new BlockPos(i, MathHelper.floor(d), j);
                if (!(EntityUtil.mc.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    public static boolean isMoving() {
        return (double)EntityUtil.mc.player.moveForward != 0.0 || (double)EntityUtil.mc.player.moveStrafing != 0.0;
    }

    public static boolean isInHole(Entity entity) {
        return BlockUtil.isHole(new BlockPos(entity.posX, entity.posY, entity.posZ));
    }

    public static List<Vec3d> getOffsetList(int n, boolean bl) {
        ArrayList<Vec3d> arrayList = new ArrayList<>();
        arrayList.add(new Vec3d(-1.0, n, 0.0));
        arrayList.add(new Vec3d(1.0, n, 0.0));
        arrayList.add(new Vec3d(0.0, n, -1.0));
        arrayList.add(new Vec3d(0.0, n, 1.0));
        if (bl) {
            arrayList.add(new Vec3d(0.0, n - 1, 0.0));
        }
        return arrayList;
    }

    public static boolean isProjectile(Entity entity) {
        return entity instanceof EntityShulkerBullet || entity instanceof EntityFireball;
    }


    public static Color getColor(Entity entity, int n, int n2, int n3, int n4, boolean bl) {
        Color color = new Color((float)n / 255.0f, (float)n2 / 255.0f, (float)n3 / 255.0f, (float)n4 / 255.0f);
        if (entity instanceof EntityPlayer && bl && MadCat.friendManager.isFriend((EntityPlayer)entity)) {
            color = new Color(0.33333334f, 1.0f, 1.0f, (float)n4 / 255.0f);
        }
        return color;
    }

    public static boolean canEntityFeetBeSeen(Entity entity) {
        return EntityUtil.mc.world.rayTraceBlocks(new Vec3d(EntityUtil.mc.player.posX, EntityUtil.mc.player.posX + (double)EntityUtil.mc.player.getEyeHeight(), EntityUtil.mc.player.posZ), new Vec3d(entity.posX, entity.posY, entity.posZ), false, true, false) == null;
    }

    public static boolean isntValid(Entity entity, double d) {
        return entity == null || EntityUtil.isDead(entity) || entity.equals(EntityUtil.mc.player) || entity instanceof EntityPlayer && MadCat.friendManager.isFriend(entity.getName()) || EntityUtil.mc.player.getDistanceSq(entity) > MathUtil.square(d);
    }

    public static float getHealth(Entity entity) {
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return entityLivingBase.getHealth() + entityLivingBase.getAbsorptionAmount();
        }
        return 0.0f;
    }

    public static boolean isMobAggressive(Entity entity) {
        if (entity instanceof EntityPigZombie) {
            if (((EntityPigZombie)entity).isArmsRaised() || ((EntityPigZombie)entity).isAngry()) {
                return false;
            }
        } else {
            if (entity instanceof EntityWolf) {
                return !((EntityWolf)entity).isAngry() || EntityUtil.mc.player.equals(((EntityWolf)entity).getOwner());
            }
            if (entity instanceof EntityEnderman) {
                return !((EntityEnderman)entity).isScreaming();
            }
        }
        return !EntityUtil.isHostileMob(entity);
    }

    public static Vec3d[] getUnsafeBlockArrayFromVec3d(Vec3d vec3d, int n, boolean bl) {
        List<Vec3d> list = EntityUtil.getUnsafeBlocksFromVec3d(vec3d, n, bl);
        Vec3d[] vec3dArray = new Vec3d[list.size()];
        return list.toArray(vec3dArray);
    }

    public static boolean isNeutralMob(Entity entity) {
        return entity instanceof EntityPigZombie || entity instanceof EntityWolf || entity instanceof EntityEnderman;
    }

    public static List<Vec3d> getUnsafeBlocksFromVec3d(Vec3d vec3d, int n, boolean bl) {
        ArrayList<Vec3d> arrayList = new ArrayList<>();
        for (Vec3d vec3d2 : EntityUtil.getOffsets(n, bl)) {
            BlockPos blockPos = new BlockPos(vec3d).add(vec3d2.x, vec3d2.y, vec3d2.z);
            Block block = EntityUtil.mc.world.getBlockState(blockPos).getBlock();
            if (!(block instanceof BlockAir || block instanceof BlockLiquid || block instanceof BlockTallGrass || block instanceof BlockFire || block instanceof BlockDeadBush || block instanceof BlockSnow)) {
                continue;
            }
            arrayList.add(vec3d2);
        }
        return arrayList;
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec3d) {
        return EntityUtil.getInterpolatedAmount(entity, vec3d.x, vec3d.y, vec3d.z);
    }

    public static boolean stopSneaking(boolean bl) {
        if (bl && EntityUtil.mc.player != null) {
            EntityUtil.mc.player.connection.sendPacket(new CPacketEntityAction(EntityUtil.mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
        }
        return false;
    }

    public static void moveEntityStrafe(double d, Entity entity) {
        if (entity != null) {
            MovementInput movementInput = EntityUtil.mc.player.movementInput;
            double d2 = movementInput.moveForward;
            double d3 = movementInput.moveStrafe;
            float f = EntityUtil.mc.player.rotationYaw;
            if (d2 == 0.0 && d3 == 0.0) {
                entity.motionX = 0.0;
                entity.motionZ = 0.0;
            } else {
                if (d2 != 0.0) {
                    if (d3 > 0.0) {
                        f += (float)(d2 > 0.0 ? -45 : 45);
                    } else if (d3 < 0.0) {
                        f += (float)(d2 > 0.0 ? 45 : -45);
                    }
                    d3 = 0.0;
                    if (d2 > 0.0) {
                        d2 = 1.0;
                    } else if (d2 < 0.0) {
                        d2 = -1.0;
                    }
                }
                entity.motionX = d2 * d * Math.cos(Math.toRadians(f + 90.0f)) + d3 * d * Math.sin(Math.toRadians(f + 90.0f));
                entity.motionZ = d2 * d * Math.sin(Math.toRadians(f + 90.0f)) - d3 * d * Math.cos(Math.toRadians(f + 90.0f));
            }
        }
    }

    public static Vec3d getInterpolatedPos(Entity entity, float f) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(EntityUtil.getInterpolatedAmount(entity, f));
    }

    public static Vec3d[] getVarOffsets(int n, int n2, int n3) {
        List<Vec3d> list = EntityUtil.getVarOffsetList(n, n2, n3);
        Vec3d[] vec3dArray = new Vec3d[list.size()];
        return list.toArray(vec3dArray);
    }

    public static double getMaxSpeed() {
        double d = 0.2873;
        if (EntityUtil.mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            d *= 1.0 + 0.2 * (double)(Objects.requireNonNull(EntityUtil.mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return d;
    }

    public static void attackEntity(Entity entity, boolean bl, boolean bl2) {
        if (bl) {
            EntityUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            EntityUtil.mc.playerController.attackEntity(EntityUtil.mc.player, entity);
        }
        if (bl2) {
            EntityUtil.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double d, double d2, double d3) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * d, (entity.posY - entity.lastTickPosY) * d2, (entity.posZ - entity.lastTickPosZ) * d3);
    }

    public static boolean isAlive(Entity entity) {
        return EntityUtil.isLiving(entity) && !entity.isDead && ((EntityLivingBase)entity).getHealth() > 0.0f;
    }

    public static Vec3d[] getUnsafeBlockArray(Entity entity, int n, boolean bl) {
        List<Vec3d> list = EntityUtil.getUnsafeBlocks(entity, n, bl);
        Vec3d[] vec3dArray = new Vec3d[list.size()];
        return list.toArray(vec3dArray);
    }

    public static boolean holdingWeapon(EntityPlayer entityPlayer) {
        return entityPlayer.getHeldItemMainhand().getItem() instanceof ItemSword || entityPlayer.getHeldItemMainhand().getItem() instanceof ItemAxe;
    }

    public static BlockPos getRoundedBlockPos(Entity entity) {
        return new BlockPos(MathUtil.roundVec(entity.getPositionVector(), 0));
    }

    public static void attackEntity(Entity entity, boolean bl) {
        if (bl) {
            EntityUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            EntityUtil.mc.playerController.attackEntity(EntityUtil.mc.player, entity);
        }
    }

    public static void OffhandAttack(Entity entity, boolean bl, boolean bl2) {
        if (bl) {
            EntityUtil.mc.player.connection.sendPacket(new CPacketUseEntity(entity));
        } else {
            EntityUtil.mc.playerController.attackEntity(EntityUtil.mc.player, entity);
        }
        if (bl2) {
            EntityUtil.mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }

    public static boolean isPassive(Entity entity) {
        if (entity instanceof EntityWolf && ((EntityWolf)entity).isAngry()) {
            return false;
        }
        if (entity instanceof EntityAgeable || entity instanceof EntityAmbientCreature || entity instanceof EntitySquid) {
            return true;
        }
        return entity instanceof EntityIronGolem && ((EntityIronGolem)entity).getRevengeTarget() == null;
    }

    public static List<Vec3d> getUnsafeBlocks(Entity entity, int n, boolean bl) {
        return EntityUtil.getUnsafeBlocksFromVec3d(entity.getPositionVector(), n, bl);
    }

    public static boolean isVehicle(Entity entity) {
        return entity instanceof EntityBoat || entity instanceof EntityMinecart;
    }

    public static boolean isSafe(Entity entity, int n, boolean bl) {
        return EntityUtil.getUnsafeBlocks(entity, n, bl).size() != 0;
    }

    public static EntityPlayer getClosestEnemy(double d) {
        EntityPlayer entityPlayer = null;
        for (EntityPlayer entityPlayer2 : EntityUtil.mc.world.playerEntities) {
            if (EntityUtil.isntValid(entityPlayer2, d)) {
                continue;
            }
            if (entityPlayer != null && EntityUtil.mc.player.getDistanceSq(entityPlayer2) >= EntityUtil.mc.player.getDistanceSq(entityPlayer)) {
                continue;
            }
            entityPlayer = entityPlayer2;
        }
        return entityPlayer;
    }

    public static boolean isMob(Entity entity) {
        return entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityDragon;
    }

    public static boolean isAnimal(Entity entity) {
        return entity instanceof EntityAnimal || entity instanceof EntitySquid || entity instanceof EntityGolem || entity instanceof EntityVillager || entity instanceof EntityBat;
    }

    public static Vec3d getInterpolatedRenderPos(Entity entity, float f) {
        return EntityUtil.getInterpolatedPos(entity, f).subtract(EntityUtil.mc.getRenderManager().viewerPosX, EntityUtil.mc.getRenderManager().viewerPosY, EntityUtil.mc.getRenderManager().viewerPosZ);
    }


    public static boolean isCrystalAtFeet(EntityEnderCrystal entityEnderCrystal, double d) {
        for (EntityPlayer entityPlayer : EntityUtil.mc.world.playerEntities) {
            if (EntityUtil.mc.player.getDistanceSq(entityPlayer) > d * d) {
                continue;
            }
            if (MadCat.friendManager.isFriend(entityPlayer)) {
                continue;
            }
            for (Vec3d vec3d : doubleLegOffsetList) {
                if (new BlockPos(entityPlayer.getPositionVector()).add(vec3d.x, vec3d.y, vec3d.z) != entityEnderCrystal.getPosition()) continue;
                return true;
            }
        }
        return false;
    }

    public static BlockPos getPlayerPos(EntityPlayer entityPlayer) {
        return new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
    }

    public static boolean isValid(Entity entity, double d) {
        return !EntityUtil.isntValid(entity, d);
    }

    public static List<Vec3d> getVarOffsetList(int n, int n2, int n3) {
        ArrayList<Vec3d> arrayList = new ArrayList<>();
        arrayList.add(new Vec3d(n, n2, n3));
        return arrayList;
    }

    public static BlockPos getPlayerPosWithEntity() {
        return new BlockPos(EntityUtil.mc.player.getRidingEntity() != null ? EntityUtil.mc.player.getRidingEntity().posX : EntityUtil.mc.player.posX, EntityUtil.mc.player.getRidingEntity() != null ? EntityUtil.mc.player.getRidingEntity().posY : EntityUtil.mc.player.posY, EntityUtil.mc.player.getRidingEntity() != null ? EntityUtil.mc.player.getRidingEntity().posZ : EntityUtil.mc.player.posZ);
    }

    public static boolean isDead(Entity entity) {
        return !EntityUtil.isAlive(entity);
    }

    public static boolean isHostileMob(Entity entity) {
        return entity.isCreatureType(EnumCreatureType.MONSTER, false) && !EntityUtil.isNeutralMob(entity);
    }

    public static float getHealth(Entity entity, boolean bl) {
        if (EntityUtil.isLiving(entity)) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            return entityLivingBase.getHealth() + (bl ? entityLivingBase.getAbsorptionAmount() : 0.0f);
        }
        return 0.0f;
    }

    public static boolean isOnLiquid(double d) {
        if (EntityUtil.mc.player.fallDistance >= 3.0f) {
            return false;
        }
        AxisAlignedBB axisAlignedBB = EntityUtil.mc.player.getRidingEntity() != null ? EntityUtil.mc.player.getRidingEntity().getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -d, 0.0) : EntityUtil.mc.player.getEntityBoundingBox().contract(0.0, 0.0, 0.0).offset(0.0, -d, 0.0);
        boolean bl = false;
        int n = (int)axisAlignedBB.minY;
        for (int i = MathHelper.floor(axisAlignedBB.minX); i < MathHelper.floor(axisAlignedBB.maxX + 1.0); ++i) {
            for (int j = MathHelper.floor(axisAlignedBB.minZ); j < MathHelper.floor(axisAlignedBB.maxZ + 1.0); ++j) {
                Block block = EntityUtil.mc.world.getBlockState(new BlockPos(i, n, j)).getBlock();
                if (block == Blocks.AIR) {
                    continue;
                }
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                bl = true;
            }
        }
        return bl;
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float f) {
        return EntityUtil.getInterpolatedAmount(entity, f, f, f);
    }

    public static boolean rayTraceHitCheck(Entity entity, boolean bl) {
        return !bl || EntityUtil.mc.player.canEntityBeSeen(entity);
    }

    public static Vec3d[] getOffsets(int n, boolean bl) {
        List<Vec3d> list = EntityUtil.getOffsetList(n, bl);
        Vec3d[] vec3dArray = new Vec3d[list.size()];
        return list.toArray(vec3dArray);
    }

    public static Vec3d interpolateEntity(Entity entity, float f) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f);
    }

    public static double getEntitySpeed(Entity entity) {
        if (entity != null) {
            double d = entity.posX - entity.prevPosX;
            double d2 = entity.posZ - entity.prevPosZ;
            double d3 = MathHelper.sqrt(d * d + d2 * d2);
            return d3 * 20.0;
        }
        return 0.0;
    }
}

