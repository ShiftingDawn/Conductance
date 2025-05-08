package conductance.api.plugin;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.util.tier.Tier;

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

	MaterialBuilder defaultFluid(MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid);

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

	MaterialBuilder ore(int dropMultiplier, int byproductMultiplier, boolean emissive, @Nullable Supplier<Material> pulverizeResult, @Nullable Supplier<Material> smeltResult);

	default MaterialBuilder ore(final boolean emissive, @Nullable final Supplier<Material> pulverizeResult, @Nullable final Supplier<Material> smeltResult) {
		return this.ore(1, 1, emissive, pulverizeResult, smeltResult);
	}

	default MaterialBuilder ore(@Nullable final Supplier<Material> pulverizeResult, @Nullable final Supplier<Material> smeltResult) {
		return this.ore(false, pulverizeResult, smeltResult);
	}

	default MaterialBuilder ore(final int dropMultiplier, final int byproductMultiplier, final boolean emissive) {
		return this.ore(dropMultiplier, byproductMultiplier, emissive, null, null);
	}

	default MaterialBuilder ore(final int dropMultiplier, final int byproductMultiplier) {
		return this.ore(dropMultiplier, byproductMultiplier, false);
	}

	default MaterialBuilder ore(final boolean emissive) {
		return this.ore(1, 1, emissive);
	}

	default MaterialBuilder ore() {
		return this.ore(1, 1, false);
	}

	MaterialBuilder wood();

	MaterialBuilder cable(Tier tier, int amps);

	Material build();
}
