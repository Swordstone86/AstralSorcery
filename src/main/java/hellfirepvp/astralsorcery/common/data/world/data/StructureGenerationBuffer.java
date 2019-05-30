/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.data.world.data;

import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.data.world.base.SectionWorldData;
import hellfirepvp.astralsorcery.common.data.world.base.WorldSection;
import hellfirepvp.astralsorcery.common.structure.StructureType;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: StructureGenerationBuffer
 * Created by HellFirePvP
 * Date: 30.05.2019 / 15:05
 */
public class StructureGenerationBuffer extends SectionWorldData<StructureGenerationBuffer.StructureRegion> {

    public StructureGenerationBuffer() {
        super(WorldCacheManager.SaveKey.STRUCTURE_GEN, PRECISION_REGION);
    }

    @Override
    protected StructureRegion createNewSection(int sectionX, int sectionZ) {
        return new StructureRegion(sectionX, sectionZ);
    }

    public void setStructureGeneration(StructureType type, BlockPos pos) {
        getOrCreateSection(pos).setStructure(type, pos);
        markDirty(pos);
    }

    public double getDstToClosest(StructureType type, double idealDistance, BlockPos dstTo) {
        double closest = Double.MAX_VALUE;
        double halfDst = idealDistance / 2.0D;
        int maxDistance = MathHelper.floor(idealDistance * 2);
        Vec3i searchVector = new Vec3i(maxDistance, 0, maxDistance);

        if (type.isAverageDistanceRequired()) {
            for (StructureType tt : StructureType.getAllTypes()) {
                if (!tt.isAverageDistanceRequired()) {
                    continue;
                }
                for (StructureRegion region : getSections(dstTo.subtract(searchVector), dstTo.add(searchVector))) {
                    for (BlockPos position : region.getStructures(type)) {
                        double dst = position.getDistance(dstTo);
                        if (dst <= halfDst) {
                            return dst; //Fast fail on close structures
                        }
                    }
                }
            }
        }

        for (StructureRegion region : getSections(dstTo.subtract(searchVector), dstTo.add(searchVector))) {
            for (BlockPos position : region.getStructures(type)) {
                double dst = position.getDistance(dstTo);
                if(dst < closest) {
                    closest = dst;
                }
            }
        }
        return closest;
    }

    @Nullable
    public BlockPos getClosest(StructureType type, BlockPos dstTo, int searchRadius) {
        double closest = Double.MAX_VALUE;
        BlockPos closestPos = null;
        Vec3i searchVector = new Vec3i(searchRadius, 0, searchRadius);
        for (StructureRegion region : getSections(dstTo.subtract(searchVector), dstTo.add(searchVector))) {
            for (BlockPos position : region.getStructures(type)) {
                double dst = position.getDistance(dstTo);
                if(dst < closest) {
                    closest = dst;
                    closestPos = position;
                }
            }
        }
        return closestPos;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {}

    @Override
    public void readFromNBT(NBTTagCompound nbt) {}

    @Override
    public void updateTick(World world) {}

    public static class StructureRegion extends WorldSection {

        private Map<StructureType, Collection<BlockPos>> generatedStructures = new HashMap<>();

        protected StructureRegion(int sX, int sZ) {
            super(sX, sZ);
        }

        public void setStructure(StructureType type, BlockPos pos) {
            generatedStructures.computeIfAbsent(type, t -> new HashSet<>()).add(pos);
        }

        @Nonnull
        public Collection<BlockPos> getStructures(StructureType type) {
            return this.generatedStructures.getOrDefault(type, new HashSet<>());
        }

        @Override
        public void writeToNBT(NBTTagCompound compound) {
            for (StructureType type : generatedStructures.keySet()) {
                NBTTagList list = new NBTTagList();
                for (BlockPos pos : generatedStructures.get(type)) {
                    NBTTagCompound tag = new NBTTagCompound();
                    NBTHelper.writeBlockPosToNBT(pos, tag);
                    list.add(tag);
                }
                compound.setTag(type.getName().toString(), list);
            }
        }

        @Override
        public void readFromNBT(NBTTagCompound compound) {
            generatedStructures.clear();

            for (StructureType type : StructureType.getAllTypes()) {
                if (compound.contains(type.getName().toString(), Constants.NBT.TAG_LIST)) {
                    NBTTagList list = compound.getList(type.getName().toString(), Constants.NBT.TAG_COMPOUND);
                    for (int i = 0; i < list.size(); i++) {
                        NBTTagCompound cmp = list.getCompound(i);
                        BlockPos pos = NBTHelper.readBlockPosFromNBT(cmp);
                        generatedStructures.computeIfAbsent(type, t -> new HashSet<>()).add(pos);
                    }
                }
            }
        }
    }

}
