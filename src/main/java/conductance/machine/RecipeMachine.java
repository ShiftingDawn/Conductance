package conductance.machine;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.MachineRecipeProviderConfigAdapter;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeHolder;
import conductance.client.MachineUIFactory;

public class RecipeMachine extends BaseWorkableMachine<RecipeMachine> implements MachineGuiHolder, MachineRecipeProviderConfigAdapter {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(RecipeMachine.class, BaseWorkableMachine.MANAGED_FIELD_HOLDER);
	@Getter
	@Setter
	@Persisted
	private int activeRecipeType;

	public RecipeMachine(final MachineType<RecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return RecipeMachine.MANAGED_FIELD_HOLDER;
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MachineUIFactory.createGui(this, entityPlayer);
	}

	public static final Function<NCRecipeType, MachineGuiSupplier> GUI_SUPPLIER = recipeType -> new MachineGuiSupplier(() ->
			recipeType.createGuiTemplate().createDefault(),
			(template, machine, autoCalc) -> {
				if (machine instanceof RecipeMachine recipeMachine) {
					recipeMachine.getRecipeType().createGuiTemplate().setupGui(template, new RecipeHolder(
									recipeMachine.getRecipeProcessor()::getProgressPercentage,
									recipeMachine.getInputInventory().inventory,
									recipeMachine.getOutputInventory().inventory,
									null,
									null
//									recipeMachine.getInputTank(),
//									recipeMachine.getOutputTank()
							),
							autoCalc
					);
				}
			}
	);
}
