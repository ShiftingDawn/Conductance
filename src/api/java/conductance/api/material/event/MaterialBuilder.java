package conductance.api.material.event;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialFlags;
import conductance.api.NCMaterialTraits;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTrait;
import conductance.api.material.MaterialTraitFluid;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.MaterialTraitOre;

public interface MaterialBuilder {

	MaterialBuilder flag(MaterialFlag flag);

	MaterialBuilder removeFlag(MaterialFlag... flagsToRemove);

	default MaterialBuilder dust() {
		return this.flag(NCMaterialFlags.DUST);
	}

	default MaterialBuilder ingot(final boolean generateBlock, final boolean generateNugget) {
		this.flag(NCMaterialFlags.INGOT);
		if (generateBlock) {
			this.flag(NCMaterialFlags.BLOCK);
		}
		if (generateNugget) {
			this.flag(NCMaterialFlags.NUGGET);
		}
		return this;
	}

	default MaterialBuilder ingot() {
		return this.ingot(true, true);
	}

	default MaterialBuilder gem(final boolean generateBlock, final boolean generateHighQuality, final boolean generateLowQuality) {
		this.flag(NCMaterialFlags.GEM);
		if (generateBlock) {
			this.flag(NCMaterialFlags.BLOCK);
		}
		if (generateHighQuality) {
			this.flag(NCMaterialFlags.GEM_EXQUISITE);
			this.flag(NCMaterialFlags.GEM_FLAWLESS);
		}
		if (generateLowQuality) {
			this.flag(NCMaterialFlags.GEM_FLAWED);
		}
		return this;
	}

	default MaterialBuilder gem() {
		return this.gem(true, true, true);
	}

	default MaterialBuilder plate() {
		return this.flag(NCMaterialFlags.PLATE);
	}

	default MaterialBuilder rod() {
		return this.flag(NCMaterialFlags.ROD);
	}

	default MaterialBuilder gear() {
		return this.flag(NCMaterialFlags.GEAR);
	}

	default MaterialBuilder gearSmall() {
		return this.flag(NCMaterialFlags.GEAR_SMALL);
	}

	default MaterialBuilder foil() {
		return this.flag(NCMaterialFlags.FOIL);
	}

	default MaterialBuilder boltAndScrew() {
		return this.flag(NCMaterialFlags.BOLT_AND_SCREW);
	}

	default MaterialBuilder ring() {
		return this.flag(NCMaterialFlags.RING);
	}

	default MaterialBuilder rotor() {
		return this.flag(NCMaterialFlags.ROTOR);
	}

	default MaterialBuilder fineWire() {
		return this.flag(NCMaterialFlags.FINE_WIRE);
	}

	default MaterialBuilder frameBox() {
		return this.flag(NCMaterialFlags.FRAME_BOX);
	}

	default MaterialBuilder metalDefault() {
		this.dust();
		this.ingot();
		this.plate();
		return this;
	}

	default MaterialBuilder metalExtra() {
		this.metalDefault();
		this.gear();
		this.rod();
		this.foil();
		this.ring();
		this.frameBox();
		return this;
	}

	default MaterialBuilder metalAll() {
		this.metalExtra();
		this.gearSmall();
		this.boltAndScrew();
		this.rotor();
		return this;
	}

	default MaterialBuilder gemDefault() {
		this.dust();
		this.gem();
		this.plate();
		return this;
	}

	default MaterialBuilder gemExtra() {
		this.gemDefault();
		this.rod();
		return this;
	}

	<T extends MaterialTrait<T>> MaterialBuilder trait(MaterialTraitKey<T> key, T instance);

	<T extends MaterialTrait<T>> MaterialBuilder removeTrait(MaterialTraitKey<T> traitToRemove);

	MaterialBuilder liquid(Supplier<MaterialTraitFluid.Liquid> factory);

	default MaterialBuilder liquid(final int temperature) {
		return this.liquid(() -> new MaterialTraitFluid.Liquid(-1, temperature, -1));
	}

	default MaterialBuilder liquid() {
		return this.liquid(-1);
	}

	MaterialBuilder gas(Supplier<MaterialTraitFluid.Gas> factory);

	default MaterialBuilder gas(final int temperature) {
		return this.gas(() -> new MaterialTraitFluid.Gas(-1, temperature, -1));
	}

	default MaterialBuilder gas() {
		return this.gas(-1);
	}

	MaterialBuilder plasma(Supplier<MaterialTraitFluid.Plasma> factory);

	default MaterialBuilder plasma(final int temperature) {
		return this.plasma(() -> new MaterialTraitFluid.Plasma(-1, temperature, -1));
	}

	default MaterialBuilder plasma() {
		return this.plasma(-1);
	}

	default MaterialBuilder ore(final int dropMultiplier, final int byproductMultiplier, final boolean emissive, @Nullable final Supplier<Material> smeltResult, @Nullable final Supplier<Material> pulverizeResult) {
		return this.trait(NCMaterialTraits.ORE, new MaterialTraitOre(dropMultiplier, byproductMultiplier, emissive, smeltResult, pulverizeResult));
	}

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

	<T> MaterialBuilder prop(MaterialProp<T> property, T value);

	<T> MaterialBuilder removeProp(MaterialProp<T> propToRemove);

	MaterialBuilder color(int[] colors, int frametime);

	default MaterialBuilder color(final int rgb) {
		return this.color(new int[] {rgb}, 0);
	}

	MaterialBuilder textureSet(ResourceLocation textureSet);

	default MaterialBuilder style(final int rgb, final ResourceLocation textureSet) {
		return this.color(rgb).textureSet(textureSet);
	}

	MaterialBuilder chemicalFormula(String formula);

	MaterialBuilder components(Object... components);
}
