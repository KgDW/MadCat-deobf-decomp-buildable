package me.madcat.util;

import java.util.function.BiPredicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class DamageUtil
implements Wrapper {

    public static boolean hasDurability(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof ItemArmor || item instanceof ItemSword || item instanceof ItemTool || item instanceof ItemShield;
    }

    public static float calculateDamage(BlockPos blockPos, Entity entity) {
        return DamageUtil.calculateDamage((double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5, entity);
    }

    public static int getItemDamage(ItemStack itemStack) {
        return itemStack.getMaxDamage() - itemStack.getItemDamage();
    }

    public static float getBlastReduction(EntityLivingBase entityLivingBase, float f, Explosion explosion) {
        float f2;
        block3: {
            f2 = f;
            if (!(entityLivingBase instanceof EntityPlayer)) break block3;
            EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
            DamageSource damageSource = DamageSource.causeExplosionDamage(explosion);
            f2 = CombatRules.getDamageAfterAbsorb(f2, (float)entityPlayer.getTotalArmorValue(), (float)entityPlayer.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int n = 0;
            try {
                n = EnchantmentHelper.getEnchantmentModifierDamage(entityPlayer.getArmorInventoryList(), damageSource);
            }
            catch (Exception exception) {
                // empty catch block
            }
            float f3 = MathHelper.clamp((float)n, 0.0f, 20.0f);
            f2 *= 1.0f - f3 / 25.0f;
            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                f2 -= f2 / 4.0f;
            }
            f2 = Math.max(f2, 0.0f);
            return f2;
        }
        f2 = CombatRules.getDamageAfterAbsorb(f2, (float)entityLivingBase.getTotalArmorValue(), (float)entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return f2;
    }

    public static int getCooldownByWeapon(EntityPlayer entityPlayer) {
        Item item = entityPlayer.getHeldItemMainhand().getItem();
        if (item instanceof ItemSword) {
            return 600;
        }
        if (item instanceof ItemPickaxe) {
            return 850;
        }
        if (item == Items.IRON_AXE) {
            return 1100;
        }
        if (item == Items.STONE_HOE) {
            return 500;
        }
        if (item == Items.IRON_HOE) {
            return 350;
        }
        if (item == Items.WOODEN_AXE || item == Items.STONE_AXE) {
            return 1250;
        }
        if (item instanceof ItemSpade || item == Items.GOLDEN_AXE || item == Items.DIAMOND_AXE || item == Items.WOODEN_HOE || item == Items.GOLDEN_HOE) {
            return 1000;
        }
        return 250;
    }

    private static boolean isResistant(BlockPos blockPos, IBlockState iBlockState) {
        return !iBlockState.getMaterial().isLiquid() && (double)iBlockState.getBlock().getExplosionResistance(DamageUtil.mc.world, blockPos, null, null) >= 19.7;
    }

    public static boolean isNaked(EntityPlayer entityPlayer) {
        for (ItemStack itemStack : entityPlayer.inventory.armorInventory) {
            if (itemStack == null) continue;
            if (itemStack.isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static boolean canBreakWeakness(EntityPlayer entityPlayer) {
        int n = 0;
        PotionEffect potionEffect = DamageUtil.mc.player.getActivePotionEffect(MobEffects.STRENGTH);
        if (potionEffect != null) {
            n = potionEffect.getAmplifier();
        }
        return !DamageUtil.mc.player.isPotionActive(MobEffects.WEAKNESS) || n >= 1 || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSword || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe || DamageUtil.mc.player.getHeldItemMainhand().getItem() instanceof ItemSpade;
    }

    public static float getDamageMultiplied(float f) {
        int n = DamageUtil.mc.world.getDifficulty().getId();
        return f * (n == 0 ? 0.0f : (n == 2 ? 1.0f : (n == 1 ? 0.5f : 1.5f)));
    }

    public static float calculateDamage(double d, double d2, double d3, Entity entity) {
        float f = 12.0f;
        double d4 = entity.getDistance(d, d2, d3) / (double)f;
        Vec3d vec3d = new Vec3d(d, d2, d3);
        double d5 = 0.0;
        try {
            d5 = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception exception) {
            // empty catch block
        }
        double d6 = (1.0 - d4) * d5;
        float f2 = (int)((d6 * d6 + d6) / 2.0 * 7.0 * (double)f + 1.0);
        double d7 = 1.0;
        if (entity instanceof EntityLivingBase) {
            d7 = DamageUtil.getBlastReduction((EntityLivingBase)entity, DamageUtil.getDamageMultiplied(f2), new Explosion(DamageUtil.mc.world, null, d, d2, d3, 6.0f, false, true));
        }
        return (float)d7;
    }

    private static float getBlockDensity(Vec3d vec3d, AxisAlignedBB axisAlignedBB, BlockPos.MutableBlockPos mutableBlockPos) {
        double d = 1.0 / ((axisAlignedBB.maxX - axisAlignedBB.minX) * 2.0 + 1.0);
        double d2 = 1.0 / ((axisAlignedBB.maxY - axisAlignedBB.minY) * 2.0 + 1.0);
        double d3 = 1.0 / ((axisAlignedBB.maxZ - axisAlignedBB.minZ) * 2.0 + 1.0);
        double d4 = (1.0 - Math.floor(1.0 / d) * d) / 2.0;
        double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int n = 0;
            int n2 = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        double d6 = axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * (double)f;
                        double d7 = axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * (double)f2;
                        double d8 = axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * (double)f3;
                        RayTraceResult rayTraceResult = DamageUtil.rayTraceBlocks(DamageUtil.mc.world, new Vec3d(d6 + d4, d7, d8 + d5), vec3d, mutableBlockPos, DamageUtil::isResistant);
                        if (rayTraceResult == null) {
                            ++n;
                        }
                        ++n2;
                    }
                }
            }
            return (float)n / (float)n2;
        }
        return 0.0f;
    }

    public static int getRoundedDamage(ItemStack itemStack) {
        return (int)DamageUtil.getDamageInPercent(itemStack);
    }

    public static float calculateDamage(Entity entity, Entity entity2) {
        return DamageUtil.calculateDamage(entity.posX, entity.posY, entity.posZ, entity2);
    }

    private static RayTraceResult rayTraceBlocks(World world, Vec3d vec3d, Vec3d vec3d2, BlockPos.MutableBlockPos mutableBlockPos, BiPredicate<BlockPos, IBlockState> biPredicate) {
        if (!(Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z) || Double.isNaN(vec3d2.x) || Double.isNaN(vec3d2.y) || Double.isNaN(vec3d2.z))) {
            RayTraceResult rayTraceResult;
            int n2 = MathHelper.floor(vec3d.x);
            int n3 = MathHelper.floor(vec3d.y);
            int n4 = MathHelper.floor(vec3d.z);
            int n5 = MathHelper.floor(vec3d2.x);
            int n6 = MathHelper.floor(vec3d2.y);
            int n7 = MathHelper.floor(vec3d2.z);
            IBlockState iBlockState = world.getBlockState(mutableBlockPos.setPos(n2, n3, n4));
            Block block = iBlockState.getBlock();
            if (block.canCollideCheck(iBlockState, false) && biPredicate.test(mutableBlockPos, iBlockState) && (rayTraceResult = iBlockState.collisionRayTrace(world, mutableBlockPos, vec3d, vec3d2)) != null) {
                return rayTraceResult;
            }
            int n8 = 20;
            while (n8-- >= 0) {
                EnumFacing enumFacing;
                if (Double.isNaN(vec3d.x) || Double.isNaN(vec3d.y) || Double.isNaN(vec3d.z)) {
                    return null;
                }
                if (n2 == n5 && n3 == n6 && n4 == n7) {
                    return null;
                }
                double d = vec3d2.x - vec3d.x;
                double d2 = vec3d2.y - vec3d.y;
                double d3 = vec3d2.z - vec3d.z;
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                double d7 = 999.0;
                double d8 = 999.0;
                double d9 = 999.0;
                if (n5 > n2) {
                    d4 = (double)n2 + 1.0;
                    d7 = (d4 - vec3d.x) / d;
                } else if (n5 < n2) {
                    d4 = n2;
                    d7 = (d4 - vec3d.x) / d;
                }
                if (n6 > n3) {
                    d5 = (double)n3 + 1.0;
                    d8 = (d5 - vec3d.y) / d2;
                } else if (n6 < n3) {
                    d5 = n3;
                    d8 = (d5 - vec3d.y) / d2;
                }
                if (n7 > n4) {
                    d6 = (double)n4 + 1.0;
                    d9 = (d6 - vec3d.z) / d3;
                } else if (n7 < n4) {
                    d6 = n4;
                    d9 = (d6 - vec3d.z) / d3;
                }
                if (d7 == -0.0) {
                    d7 = -1.0E-4;
                }
                if (d8 == -0.0) {
                    d8 = -1.0E-4;
                }
                if (d9 == -0.0) {
                    d9 = -1.0E-4;
                }
                if (d7 < d8 && d7 < d9) {
                    enumFacing = n5 > n2 ? EnumFacing.WEST : EnumFacing.EAST;
                    vec3d = new Vec3d(d4, vec3d.y + d2 * d7, vec3d.z + d3 * d7);
                } else if (d8 < d9) {
                    enumFacing = n6 > n3 ? EnumFacing.DOWN : EnumFacing.UP;
                    vec3d = new Vec3d(vec3d.x + d * d8, d5, vec3d.z + d3 * d8);
                } else {
                    enumFacing = n7 > n4 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                    vec3d = new Vec3d(vec3d.x + d * d9, vec3d.y + d2 * d9, d6);
                }
                n2 = MathHelper.floor(vec3d.x) - (enumFacing == EnumFacing.EAST ? 1 : 0);
                n3 = MathHelper.floor(vec3d.y) - (enumFacing == EnumFacing.UP ? 1 : 0);
                n4 = MathHelper.floor(vec3d.z) - (enumFacing == EnumFacing.SOUTH ? 1 : 0);
                mutableBlockPos.setPos(n2, n3, n4);
                IBlockState iBlockState2 = world.getBlockState(mutableBlockPos);
                Block block2 = iBlockState2.getBlock();
                if (!block2.canCollideCheck(iBlockState2, false)) continue;
                if (!biPredicate.test(mutableBlockPos, iBlockState2)) {
                    continue;
                }
                RayTraceResult rayTraceResult2 = iBlockState2.collisionRayTrace(world, mutableBlockPos, vec3d, vec3d2);
                if (rayTraceResult2 == null) continue;
                return rayTraceResult2;
            }
        }
        return null;
    }

    public static boolean isArmorLow(EntityPlayer entityPlayer, int n) {
        for (ItemStack itemStack : entityPlayer.inventory.armorInventory) {
            if (itemStack == null) {
                return true;
            }
            if (DamageUtil.getItemDamage(itemStack) >= n) {
                continue;
            }
            return true;
        }
        return false;
    }

    public static float getDamageInPercent(ItemStack itemStack) {
        return (float)DamageUtil.getItemDamage(itemStack) / (float)itemStack.getMaxDamage() * 100.0f;
    }

    public static boolean canTakeDamage(boolean bl) {
        return !DamageUtil.mc.player.capabilities.isCreativeMode && !bl;
    }
}

