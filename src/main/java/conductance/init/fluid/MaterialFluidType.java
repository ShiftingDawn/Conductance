package conductance.init.fluid;

import net.neoforged.neoforge.fluids.FluidType;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialFluidType extends FluidType {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;

	public MaterialFluidType(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
	}
}
