package conductance.compat.jei;

import java.util.Optional;
import net.neoforged.neoforge.fluids.FluidStack;
import mezz.jei.api.gui.builder.IClickableIngredientFactory;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.runtime.IClickableIngredient;
import conductance.api.machine.gui.GuiWidget;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.machine.gui.TankWidget;

final class MachineScreenGuiHandler implements IGuiContainerHandler<MachineScreen> {

	@Override
	public Optional<? extends IClickableIngredient<?>> getClickableIngredientUnderMouse(final IClickableIngredientFactory builder, final MachineScreen containerScreen, final double mouseX, final double mouseY) {
		final GuiWidget widget = containerScreen.getWidgetUnderMouse((int) mouseX, (int) mouseY);
		if (widget instanceof final TankWidget tankWidget) {
			final FluidStack fluid = tankWidget.getHandler().getFluidInTank(tankWidget.getTank()).copy();
			if (!fluid.isEmpty()) {
				return builder.createBuilder(NeoForgeTypes.FLUID_STACK, fluid).buildWithArea(widget.getX(), widget.getY(), widget.getWidth(), widget.getHeight());
			}
		}
		return Optional.empty();
	}
}
