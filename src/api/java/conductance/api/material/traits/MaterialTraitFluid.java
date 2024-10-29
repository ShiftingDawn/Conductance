package conductance.api.material.traits;

import lombok.Getter;
import conductance.api.NCMaterialTraits;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitMap;

@Getter
public abstract class MaterialTraitFluid<T extends MaterialTraitFluid<T>> implements IMaterialTrait<T> {

	private static final int WATER_VISCOSITY = 1000;
	private static final int ROOM_TEMP = 300;
	private static final int ROOM_DENSITY = 1000;
	private static final int GAS_DENSITY = -100;
	private static final int MOLTEN_TEMP = 1300;
	private static final int MOLTEN_DENSITY = 1500;
	private static final int PLASMA_TEMP = 10000;
	private static final int PLASMA_DENSITY = -10000;

	private int viscosity = MaterialTraitFluid.WATER_VISCOSITY;
	private int temperature = -1;
	private int density = -1;

	private MaterialTraitFluid() {
	}

	protected void setViscosityInternal(final int newViscosity) {
		this.viscosity = newViscosity;
	}

	protected void setTemperatureInternal(final int newTemperature) {
		this.temperature = newTemperature;
	}

	protected void setDensityInternal(final int newDensity) {
		this.density = newDensity;
	}

	public void setViscosity(final int viscosity) {
		if (viscosity < 0) {
			throw new IllegalArgumentException("viscosity cannot be negative!");
		}
		this.viscosity = viscosity;
	}

	public void setTemperature(final int temperature) {
		if (temperature < 0) {
			throw new IllegalArgumentException("temperature cannot be negative!");
		}
		this.temperature = temperature;
	}

	public void setDensity(final int density) {
		if (density < 0) {
			throw new IllegalArgumentException("density cannot be negative!");
		}
		this.density = density;
	}

	@Override
	public void verify(final Material material, final MaterialTraitMap traitMap) {
	}

	public static class Liquid extends MaterialTraitFluid<Liquid> {

		@Override
		public void verify(final Material material, final MaterialTraitMap traitMap) {
			super.verify(material, traitMap);
			final boolean hasSolidForm = traitMap.has(NCMaterialTraits.DUST);
			if (this.getTemperature() == -1) {
				this.setTemperatureInternal(hasSolidForm ? MaterialTraitFluid.MOLTEN_TEMP : MaterialTraitFluid.ROOM_TEMP);
			}
			if (this.getDensity() == -1) {
				this.setDensityInternal(hasSolidForm ? MaterialTraitFluid.MOLTEN_DENSITY : MaterialTraitFluid.ROOM_DENSITY);
			}
		}
	}

	public static class Gas extends MaterialTraitFluid<Gas> {

		public Gas() {
			this.setDensityInternal(MaterialTraitFluid.GAS_DENSITY);
		}
	}

	public static class Plasma extends MaterialTraitFluid<Plasma> {

		public Plasma() {
			this.setTemperatureInternal(MaterialTraitFluid.PLASMA_TEMP);
			this.setDensityInternal(MaterialTraitFluid.PLASMA_DENSITY);
		}
	}
}
