package conductance.api.plugin;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.api.material.*;
import conductance.api.material.traits.MaterialTraitFluid;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder dust(final TagKey<Block> requiredToolTag);

	MaterialBuilder dust(final TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder ingot();

	MaterialBuilder ingot(final TagKey<Block> requiredToolTag);

	MaterialBuilder ingot(final TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder gem();

	MaterialBuilder gem(final TagKey<Block> requiredToolTag);

	MaterialBuilder gem(final TagKey<Block> requiredToolTag, int burnTime);

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

	MaterialBuilder flags(MaterialFlag... flags);

	MaterialBuilder addFlagAndPreset(Collection<MaterialFlag> preset, MaterialFlag... flags);

	MaterialBuilder periodicElement(PeriodicElement periodicElement);

	MaterialBuilder ore();

	MaterialBuilder ore(boolean emissive);

	MaterialBuilder ore(int oreMultiplier, int byproductMultiplier);

	MaterialBuilder ore(int oreMultiplier, int byproductMultiplier, boolean emissive);

	MaterialBuilder wood();

	Material build();
}
