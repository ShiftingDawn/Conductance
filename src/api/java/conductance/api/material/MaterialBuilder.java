package conductance.api.material;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.material.traits.MaterialTraitIngot;
import conductance.api.periodicelement.PeriodicElement;
import conductance.api.util.tier.Tier;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder dust(TagKey<Block> requiredToolTag);

	MaterialBuilder dust(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder ingot();

	MaterialBuilder ingot(TagKey<Block> requiredToolTag);

	MaterialBuilder ingot(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder ingot(Supplier<MaterialTraitIngot> factory);

	MaterialBuilder ingot(TagKey<Block> requiredToolTag, Supplier<MaterialTraitIngot> factory);

	MaterialBuilder ingot(TagKey<Block> requiredToolTag, int burnTime, Supplier<MaterialTraitIngot> factory);

	MaterialBuilder gem();

	MaterialBuilder gem(TagKey<Block> requiredToolTag);

	MaterialBuilder gem(TagKey<Block> requiredToolTag, int burnTime);

	MaterialBuilder liquid();

	MaterialBuilder liquid(Supplier<MaterialTraitFluid.Liquid> factory);

	MaterialBuilder liquid(int temperature);

	MaterialBuilder gas();

	MaterialBuilder gas(Supplier<MaterialTraitFluid.Gas> factory);

	MaterialBuilder gas(int temperature);

	MaterialBuilder plasma();

	MaterialBuilder plasma(Supplier<MaterialTraitFluid.Plasma> factory);

	MaterialBuilder plasma(int temperature);

	MaterialBuilder defaultFluid(MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid);

	MaterialBuilder requiredTool(TagKey<Block> requiredToolTag);

	MaterialBuilder burnTime(int burnTime);

	MaterialBuilder lightLevel(int lightLevel);

	MaterialBuilder color(int color);

	MaterialBuilder color(int r, int g, int b);

	MaterialBuilder calcColor();

	MaterialBuilder textureSet(ResourceLocation textureSet);

	MaterialBuilder formula(String formula);

	MaterialBuilder components(Object... components);

	MaterialBuilder components(MaterialStack... components);

	MaterialBuilder components(List<MaterialStack> components);

	MaterialBuilder flags(Collection<MaterialFlag> preset, MaterialFlag... flagsToAdd);

	MaterialBuilder flags(MaterialFlag... flagsToAdd);

	MaterialBuilder periodicElement(PeriodicElement periodicElement);

	MaterialBuilder ore(int dropMultiplier, int byproductMultiplier, boolean emissive, @Nullable Supplier<Material> smeltResult, @Nullable Supplier<Material> pulverizeResult);

	default MaterialBuilder ore(final boolean emissive, @Nullable final Supplier<Material> smeltResult, @Nullable final Supplier<Material> pulverizeResult) {
		return this.ore(1, 1, emissive, smeltResult, pulverizeResult);
	}

	default MaterialBuilder ore(@Nullable final Supplier<Material> smeltResult, @Nullable final Supplier<Material> pulverizeResult) {
		return this.ore(false, smeltResult, pulverizeResult);
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

	MaterialBuilder wire(Tier tier, int amps);
}
