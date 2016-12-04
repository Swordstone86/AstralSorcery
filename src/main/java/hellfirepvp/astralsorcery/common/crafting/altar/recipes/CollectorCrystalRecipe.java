package hellfirepvp.astralsorcery.common.crafting.altar.recipes;

import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapeMap;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.item.block.ItemCollectorCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemRockCrystalBase;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemTunedCrystalBase;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: CollectorCrystalRecipe
 * Created by HellFirePvP
 * Date: 14.11.2016 / 02:03
 */
public class CollectorCrystalRecipe extends ConstellationRecipe {

    private final boolean celestial;
    private final ItemStack renderOutput;

    public CollectorCrystalRecipe(boolean celestial) {
        super(new ShapedRecipe(celestial ? BlocksAS.celestialCollectorCrystal : BlocksAS.collectorCrystal)
                .addPart((celestial ? ItemsAS.tunedCelestialCrystal : ItemsAS.tunedRockCrystal),
                        ShapedRecipeSlot.CENTER)
                .addPart(ItemCraftingComponent.MetaType.STARDUST.asStack(),
                        ShapedRecipeSlot.UPPER_CENTER,
                        ShapedRecipeSlot.LOWER_CENTER));
        setAttItem(ItemCraftingComponent.MetaType.AQUAMARINE.asStack(), AltarSlot.values());
        this.celestial = celestial;
        ItemStack stack = new ItemStack(celestial ? BlocksAS.celestialCollectorCrystal : BlocksAS.collectorCrystal);
        ItemCollectorCrystal.setType(stack, celestial ?
                BlockCollectorCrystalBase.CollectorCrystalType.CELESTIAL_CRYSTAL :
                BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL);
        this.renderOutput = stack;
    }

    @Nullable
    @Override
    public ItemStack getOutput(ShapeMap centralGridMap, TileAltar altar) {
        ItemStack center = centralGridMap.get(ShapedRecipeSlot.CENTER);
        if(center == null || center.getItem() == null || !(center.getItem() instanceof ItemTunedCrystalBase)) return null;
        ItemStack out = super.getOutput(centralGridMap, altar);
        IMajorConstellation attuned = ItemTunedCrystalBase.getMainConstellation(center);
        CrystalProperties prop = CrystalProperties.getCrystalProperties(center);
        if(attuned == null || prop == null) return null;
        ItemCollectorCrystal.setConstellation(out, attuned);
        CrystalProperties.applyCrystalProperties(out, prop);
        ItemCollectorCrystal.setType(out, celestial ?
                BlockCollectorCrystalBase.CollectorCrystalType.CELESTIAL_CRYSTAL :
                BlockCollectorCrystalBase.CollectorCrystalType.ROCK_CRYSTAL);
        return out;
    }

    @Nonnull
    @Override
    public ItemStack getOutputForRender() {
        return renderOutput;
    }
}