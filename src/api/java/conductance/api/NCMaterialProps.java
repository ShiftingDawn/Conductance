package conductance.api;

import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTraitKey;

public final class NCMaterialProps {

	public static final MaterialProp<Integer> BURN_TIME = new MaterialProp<>();

	public static final MaterialProp<MaterialTraitKey<?>> DEFAULT_FLUID = new MaterialProp<>();

	private NCMaterialProps() {
	}
}
