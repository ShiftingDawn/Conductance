package conductance.api.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCRecipeElementTypes;
import conductance.api.machine.gui.GuiTextures;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.ManagedBoolean;
import conductance.api.machine.gui.ToggleButtonWidget;
import conductance.api.recipe.MachineRecipe;
import conductance.api.util.GuiUtils;
import conductance.api.util.IO;

public final class MachineRecipeCapabilityFluids extends MachineRecipeCapability<SizedFluidIngredient> implements IBlockCapabilityHandler, IDelegatedFluidHandler {

	private final @Getter MachineFluidHandler handler;

	public MachineRecipeCapabilityFluids(final MachineBlockEntity<?> machine, final int tankCount, final IO recipeIoMode, final CapIO capabilityIoMode, final IntFunction<MachineFluidHandler> fluidHandlerFactory) {
		super(machine, NCRecipeElementTypes.FLUID, recipeIoMode, capabilityIoMode);
		this.handler = fluidHandlerFactory.apply(tankCount);
		this.handler.setChangeListener(this::setChanged);
		this.addChangedListener(machine::syncToClient);
	}

	@Override
	public IFluidHandlerModifiable getRealFluidHandler() {
		return this.handler;
	}

	@Override
	public void serialize(final ValueOutput valueOutput) {
		this.handler.serialize(valueOutput);
	}

	@Override
	public void deserialize(final ValueInput valueInput) {
		this.handler.deserialize(valueInput);
	}

	@Override
	protected @Nullable List<SizedFluidIngredient> handleInternal(final IO io, final MachineRecipe recipe, final List<SizedFluidIngredient> inputs, final boolean simulate) {
		if (io != this.getRecipeIoMode()) {
			return inputs;
		}
		return switch (io) {
			case IN -> {
				final MachineFluidHandler fluidHandler = simulate ? this.handler.copy() : this.handler;
				final List<SizedFluidIngredient> leftOvers = new ArrayList<>();
				for (final SizedFluidIngredient ingredient : inputs) {
					int ingredientCount = ingredient.amount();
					for (int tank = 0; tank < fluidHandler.getTanks(); ++tank) {
						if (!ingredient.test(fluidHandler.getFluidInTank(tank))) {
							continue;
						}
						final FluidStack extracted = fluidHandler.drain(tank, ingredientCount, FluidAction.EXECUTE);
						if (!extracted.isEmpty()) {
							ingredientCount -= extracted.getAmount();
						}
						if (ingredientCount < 0) {
							break;
						}
					}
					if (ingredientCount > 0) {
						leftOvers.add(new SizedFluidIngredient(ingredient.ingredient(), ingredientCount));
					}
				}
				yield !leftOvers.isEmpty() ? leftOvers : null;
			}
			case OUT -> {
				final MachineFluidHandler fluidHandler = simulate ? this.handler.copy() : this.handler;
				final List<SizedFluidIngredient> leftOvers = new ArrayList<>();
				for (final SizedFluidIngredient ingredient : inputs) {
					final FluidStack stackToInsert = new FluidStack(ingredient.ingredient().fluids().getFirst().value(), ingredient.amount());
					for (int slot = 0; slot < fluidHandler.getTanks(); ++slot) {
						final int filled = fluidHandler.fill(slot, stackToInsert, FluidAction.EXECUTE);
						stackToInsert.shrink(filled);
						if (stackToInsert.isEmpty()) {
							break;
						}
					}
					if (!stackToInsert.isEmpty()) {
						leftOvers.add(new SizedFluidIngredient(ingredient.ingredient(), stackToInsert.getAmount()));
					}
				}
				yield !leftOvers.isEmpty() ? leftOvers : null;
			}
		};
	}

	@Override
	public List<SizedFluidIngredient> getAvailableContent() {
		final ArrayList<SizedFluidIngredient> result = new ArrayList<>();
		for (int i = 0; i < this.handler.getTanks(); ++i) {
			final FluidStack stack = this.handler.getFluidInTank(i);
			if (!stack.isEmpty()) {
				result.add(SizedFluidIngredient.of(stack.getFluid(), stack.getAmount()));
			}
		}
		return Collections.unmodifiableList(result);
	}

	@Override
	public int getMaxSpaceForContent(final SizedFluidIngredient object) {
		if (object.ingredient().fluids().isEmpty()) {
			return 0;
		}
		final Fluid fluid = object.ingredient().fluids().getFirst().value();
		return this.handler.fill(new FluidStack(fluid, object.amount()), FluidAction.SIMULATE);
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		if (!this.canCapabilityInput()) {
			return 0;
		}
		return this.handler.fill(resource, action);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final FluidAction action) {
		if (!this.canCapabilityOutput()) {
			return FluidStack.EMPTY;
		}
		return this.handler.drain(resource, action);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		if (!this.canCapabilityOutput()) {
			return FluidStack.EMPTY;
		}
		return this.handler.drain(maxDrain, action);
	}

	@Override
	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
		if (this.canCapabilityInput()) {
			adder.accept("fluid_allow_overflow", CAPI.make(new ToggleButtonWidget(
				0, 0, 0, 0,
				new ManagedBoolean(this.handler::setAllowOverflow, this.handler::isAllowOverflow),
				toggled -> toggled ? GuiTextures.TEXTURE_FLUID_OVERFLOW_ON.get() : GuiTextures.TEXTURE_FLUID_OVERFLOW_OFF.get(),
				null
			), button -> button.addTooltipCallback((widget, tooltip) -> {
				final ToggleButtonWidget btn = (ToggleButtonWidget) widget;
				GuiUtils.tooltipTranslatable(tooltip, btn.isToggled() ? "guiWidget.conductance.allow_overflow.fluid.disable" : "guiWidget.conductance.allow_overflow.fluid.enable");
			})));
		}
	}
}
