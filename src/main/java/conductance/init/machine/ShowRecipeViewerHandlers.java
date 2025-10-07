package conductance.init.machine;

import java.util.List;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import lombok.RequiredArgsConstructor;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MouseEventListener;
import conductance.api.machine.gui.TooltipCallback;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.GuiUtils;
import conductance.compat.CompatHelper;

@RequiredArgsConstructor
final class ShowRecipeViewerHandlers implements TooltipCallback, MouseEventListener {

	private final MachineBlockEntity<?> machine;

	@Override
	public void onTooltip(final List<ClientTooltipComponent> tooltip) {
		if (this.machine.getMachineType().getRecipeTypes().length > 0) {
			GuiUtils.tooltipTranslatable(tooltip, "info.conductance.jei.open_recipe_viewer");
		}
	}

	@Override
	public boolean onMouseEvent(final IGuiWidget widget, final Event event, final int button, final int mouseX, final int mouseY) {
		if (event == MouseEventListener.Event.PRESS && button == 0) {
			if (this.machine instanceof final RecipeCapabilityHolder recipeCapabilityHolder) {
				CompatHelper.showRecipes(recipeCapabilityHolder.getRecipeType());
				return true;
			} else {
				final MachineRecipeType[] recipeTypes = this.machine.getMachineType().getRecipeTypes();
				if (recipeTypes.length > 0) {
					CompatHelper.showRecipes(recipeTypes[0]);
					return true;
				}
			}
		}
		return false;
	}
}
