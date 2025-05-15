package conductance.machine;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeHolder;
import conductance.api.util.tier.Tier;
import conductance.client.MachineUIFactory;

public class GenericRecipeMachine extends TieredWorkableMachine<GenericRecipeMachine> implements MachineGuiHolder {

	public GenericRecipeMachine(final MachineType<GenericRecipeMachine> type, final BlockPos pos, final BlockState blockState, final Tier tier) {
		super(type, pos, blockState, tier);
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MachineUIFactory.createGui(this, this, entityPlayer);
	}

	public static final Function<NCRecipeType, MachineGuiSupplier> GUI_SUPPLIER = recipeType -> new MachineGuiSupplier(() ->
			recipeType.createGuiTemplate().createDefault(),
			(template, machine, theme, autoCalc) -> {
				if (machine instanceof GenericRecipeMachine recipeMachine) {
					RecipeHolder recipeHolder = new RecipeHolder(
							recipeMachine.getRecipeProcessor()::getProgressPercentage,
							recipeMachine.getInputInventory().inventory,
							recipeMachine.getOutputInventory().inventory,
							recipeMachine.getInputTank(),
							recipeMachine.getOutputTank()
					);
					recipeMachine.getRecipeType().createGuiTemplate().setupGui(template, recipeHolder, theme, autoCalc);
				}
			}
	);
}
