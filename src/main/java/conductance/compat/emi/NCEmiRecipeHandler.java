package conductance.compat.emi;

import java.util.List;
import net.minecraft.world.inventory.Slot;
import com.lowdragmc.lowdraglib.gui.modular.ModularUIContainer;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.jei.IngredientIO;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.StandardRecipeHandler;

final class NCEmiRecipeHandler implements StandardRecipeHandler<ModularUIContainer> {

	@Override
	public boolean supportsRecipe(final EmiRecipe recipe) {
		return recipe instanceof NCEmiRecipe;
	}

	@Override
	public List<Slot> getInputSources(final ModularUIContainer handler) {
		return handler.getModularUI().getSlotMap().values().stream()
				.filter(e -> e.getIngredientIO() == IngredientIO.INPUT || e.isPlayerContainer || e.isPlayerHotBar)
				.map(SlotWidget::getHandler)
				.toList();
	}

	@Override
	public List<Slot> getCraftingSlots(final ModularUIContainer handler) {
		return handler.getModularUI().getSlotMap().values().stream()
				.filter(e -> e.getIngredientIO() == IngredientIO.INPUT)
				.map(SlotWidget::getHandler)
				.toList();
	}
}
