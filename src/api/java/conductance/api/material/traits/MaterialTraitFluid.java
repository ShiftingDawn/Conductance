package conductance.api.material.traits;

import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialTraitKey;

@RequiredArgsConstructor
@Getter
public abstract class MaterialTraitFluid<T extends MaterialTraitFluid<T>> implements IMaterialTrait<T> {

	public static final int WATER_VISCOSITY = 1000;
	public static final int ROOM_TEMP = 300;
	public static final int ROOM_DENSITY = 1000;
	public static final int GAS_DENSITY = -100;
	public static final int MOLTEN_TEMP = 1300;
	public static final int MOLTEN_DENSITY = 1500;
	public static final int PLASMA_TEMP = 10000;
	public static final int PLASMA_DENSITY = -10000;

	private final int viscosity;
	private final int temperature;
	private final int density;

	@Override
	public void validate(final Material material, final Consumer<MaterialTraitKey<?>> assertTrait) {
	}

	public static class Liquid extends MaterialTraitFluid<Liquid> {

		public Liquid(final int viscosity, final int temperature, final int density) {
			super(viscosity, temperature, density);
		}
	}

	public static class Gas extends MaterialTraitFluid<Gas> {

		public Gas(final int viscosity, final int temperature, final int density) {
			super(viscosity, temperature, density);
		}
	}

	public static class Plasma extends MaterialTraitFluid<Plasma> {

		public Plasma(final int viscosity, final int temperature, final int density) {
			super(viscosity, temperature, density);
		}
	}
}
