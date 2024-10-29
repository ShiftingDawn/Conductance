package conductance.api.plugin;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.PeriodicElement;
import conductance.api.material.traits.MaterialTraitFluid;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder dust(TagKey<Block> requiredToolTag);

	MaterialBuilder dust(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder ingot();

	MaterialBuilder ingot(TagKey<Block> requiredToolTag);

	MaterialBuilder ingot(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder gem();

	MaterialBuilder gem(TagKey<Block> requiredToolTag);

	MaterialBuilder gem(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder liquid();

	MaterialBuilder liquid(int temperature);

	MaterialBuilder liquid(Consumer<MaterialTraitFluid.Liquid> builder);

	MaterialBuilder gas();

	MaterialBuilder gas(int temperature);

	MaterialBuilder gas(Consumer<MaterialTraitFluid.Gas> builder);

	MaterialBuilder plasma();

	MaterialBuilder plasma(int temperature);

	MaterialBuilder plasma(Consumer<MaterialTraitFluid.Plasma> builder);

	MaterialBuilder requiredTool(TagKey<Block> requiredToolTag);

	MaterialBuilder burnTime(int burnTime);

	MaterialBuilder lightLevel(int lightLevel);

	MaterialBuilder color(int color);

	MaterialBuilder color(int r, int g, int b);

	MaterialBuilder calcColor();

	MaterialBuilder textureSet(MaterialTextureSet set);

	MaterialBuilder formula(String formula);

	MaterialBuilder components(Object... components);

	MaterialBuilder components(MaterialStack... components);

	MaterialBuilder components(List<MaterialStack> components);

	MaterialBuilder flags(MaterialFlag... flagsToAdd);

	MaterialBuilder addFlagAndPreset(Collection<MaterialFlag> preset, MaterialFlag... flagsToAdd);

	MaterialBuilder periodicElement(PeriodicElement periodicElement);

	MaterialBuilder ore();

	MaterialBuilder ore(boolean emissive);

	MaterialBuilder ore(int dropMultiplier, int byproductMultiplier);

	MaterialBuilder ore(int dropMultiplier, int byproductMultiplier, boolean emissive);

	MaterialBuilder wood();

	Material build();
}
