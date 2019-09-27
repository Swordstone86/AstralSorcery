/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.crafting.recipe.altar;

import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.AttunementUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.ConstellationUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.builtin.TraitUpgradeRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: DerivativeAltarRecipeHandler
 * Created by HellFirePvP
 * Date: 27.09.2019 / 20:05
 */
public class DerivativeAltarRecipeHandler {

    private static Map<ResourceLocation, Function<SimpleAltarRecipe, ? extends SimpleAltarRecipe>> typeConverterMap = new HashMap<>();

    public static void registerConverter(ResourceLocation name, Function<SimpleAltarRecipe, ? extends SimpleAltarRecipe> converter) {
        typeConverterMap.put(name, converter);
    }

    public static SimpleAltarRecipe convert(SimpleAltarRecipe recipe, ResourceLocation alternativeBase) {
        return typeConverterMap.getOrDefault(alternativeBase, Function.identity()).apply(recipe);
    }

    //TODO complete list
    public static void registerDefaultConverters() {
        registerConverter(AstralSorcery.key("attunement_upgrade"), AttunementUpgradeRecipe::convertToThis);
        registerConverter(AstralSorcery.key("constellation_upgrade"), ConstellationUpgradeRecipe::convertToThis);
        registerConverter(AstralSorcery.key("trait_upgrade"), TraitUpgradeRecipe::convertToThis);
    }

}
