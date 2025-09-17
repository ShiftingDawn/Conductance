package conductance.api;

import conductance.api.material.MaterialProp;

public final class NCMaterialProps {

	public static final MaterialProp.Int BURN_TIME = new MaterialProp.Int();

	public static final MaterialProp.Int LIQUID_TEMPERATURE = new MaterialProp.Int();
	public static final MaterialProp.Int LIQUID_VISCOSITY = new MaterialProp.Int();
	public static final MaterialProp.Int LIQUID_DENSITY = new MaterialProp.Int();
	public static final MaterialProp.Int GAS_TEMPERATURE = new MaterialProp.Int();
	public static final MaterialProp.Int GAS_VISCOSITY = new MaterialProp.Int();
	public static final MaterialProp.Int GAS_DENSITY = new MaterialProp.Int();
	public static final MaterialProp.Int PLASMA_TEMPERATURE = new MaterialProp.Int();
	public static final MaterialProp.Int PLASMA_VISCOSITY = new MaterialProp.Int();
	public static final MaterialProp.Int PLASMA_DENSITY = new MaterialProp.Int();
	public static final MaterialProp.Enum<FluidType> DEFAULT_FLUID = new MaterialProp.Enum<>();

	public enum FluidType {
		LIQUID, GAS, PLASMA;
	}

	private NCMaterialProps() {
	}
}
