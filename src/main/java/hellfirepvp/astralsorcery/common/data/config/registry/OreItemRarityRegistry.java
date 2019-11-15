/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2019
 *
 * All rights reserved.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.data.config.registry;

import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataAdapter;
import hellfirepvp.astralsorcery.common.data.config.registry.sets.OreItemRarityEntry;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: OreItemRarityRegistry
 * Created by HellFirePvP
 * Date: 31.08.2019 / 23:46
 */
public class OreItemRarityRegistry extends ConfigDataAdapter<OreItemRarityEntry> {

    public static final OreItemRarityRegistry VOID_TRASH_REWARD = new OreItemRarityRegistry("perk_void_trash_ore");

    private final String fileName;

    public OreItemRarityRegistry(String fileName) {
        this.fileName = fileName;
    }

    @Nullable
    public Item getRandomItem(Random rand) {
        List<OreItemRarityEntry> entries = this.getConfiguredValues();
        Set<OreItemRarityEntry> visitedEntires = new HashSet<>();

        while (visitedEntires.size() < entries.size()) {
            OreItemRarityEntry entry = MiscUtils.getWeightedRandomEntry(entries.stream()
                    .filter(visitedEntires::contains)
                    .collect(Collectors.toList()), rand, OreItemRarityEntry::getWeight);

            if (entry != null) {
                visitedEntires.add(entry);
                Item i = entry.getRandomItem(rand);
                if (i != null) {
                    return i;
                }
            } else {
                return null; //Invalid state?
            }
        }
        return null;
    }

    @Override
    public List<OreItemRarityEntry> getDefaultValues() {
        return new ArrayList<OreItemRarityEntry>() {
            {
                add(new OreItemRarityEntry(Tags.Items.ORES_COAL,     5200));
                add(new OreItemRarityEntry(Tags.Items.ORES_IRON,     2500));
                add(new OreItemRarityEntry(Tags.Items.ORES_GOLD,      550));
                add(new OreItemRarityEntry(Tags.Items.ORES_LAPIS,     360));
                add(new OreItemRarityEntry(Tags.Items.ORES_REDSTONE,  700));
                add(new OreItemRarityEntry(Tags.Items.ORES_DIAMOND,   120));
                add(new OreItemRarityEntry(Tags.Items.ORES_EMERALD,   100));
            }
        };
    }

    @Override
    public String getSectionName() {
        return this.fileName;
    }

    @Override
    public String getCommentDescription() {
        return "Format: '<tagName>;<integerWeight>' Defines random-weighted ore-selection data. Define item-tags to select from here with associated weight. Specific mods can be blacklisted in the general AstralSorcery config in 'modidOreBlacklist'.";
    }

    @Override
    public String getTranslationKey() {
        return translationKey("data");
    }

    @Override
    public Predicate<Object> getValidator() {
        return obj -> obj instanceof String && OreItemRarityEntry.deserialize((String) obj) != null;
    }

    @Nullable
    @Override
    public OreItemRarityEntry deserialize(String string) throws IllegalArgumentException {
        return OreItemRarityEntry.deserialize(string);
    }
}