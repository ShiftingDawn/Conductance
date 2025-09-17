package conductance.init.fluid;

import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import lombok.Getter;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;

public final class MaterialFluidType extends FluidType {

	private final @Getter Material material;
	private final @Getter MaterialGenerationHandler handler;
	private final Component description;

	public MaterialFluidType(final Properties properties, final Material material, final MaterialGenerationHandler handler) {
		super(properties);
		this.material = material;
		this.handler = handler;
		this.description = Component.translatable(handler.makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
	}

	@Override
	public Component getDescription() {
		return this.description;
	}

	@Override
	public Component getDescription(final FluidStack stack) {
		return this.getDescription();
	}
}
