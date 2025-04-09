package conductance.machine;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.gui.MetaBlockEntityGuiHolder;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.MetaRecipeProviderConfigAdapter;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeProcessor;
import conductance.api.machine.trait.MetaCapability;
import conductance.client.MetaBlockEntityUIFactory;

public class RecipeMachine extends MetaBlockEntity<RecipeMachine> implements MetaBlockEntityGuiHolder, MetaRecipeProviderConfigAdapter {

	@Getter
	@Setter
	@Persisted
	private int activeRecipeType;

	public RecipeMachine(final MetaBlockEntityType<RecipeMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
	}

	@Override
	protected void registerCapabilities(final Consumer<MetaCapability> register) {
	}

	@Override
	public NCRecipeType[] getRecipeTypes() {
		return this.getMetaType().getRecipeTypes();
	}

	@Override
	public NCRecipeType getRecipeType() {
		return this.getRecipeTypes()[this.activeRecipeType];
	}

	@Override
	public RecipeProcessor getRecipeProcessor() {
		//TODO implement
		return null;
	}

	@Override
	public Map<IRecipeElementType<?>, Integer> getOutputLimits() {
		//TODO implement
		return Map.of();
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MetaBlockEntityUIFactory.createGui(this, entityPlayer);
	}

	public static final Function<NCRecipeType, MachineGuiSupplier> GUI_SUPPLIER = recipeType -> new MachineGuiSupplier(() ->
			recipeType.createGuiTemplate().createDefault(),
			(template, metaBlockEntity, autoCalc) -> {
				if (metaBlockEntity instanceof RecipeMachine recipeMachine) {
					//					recipeMachine.getRecipeType().createGuiTemplate().setupGui(template, new RecipeHolder(
					//									recipeMachine.getRecipeProcessor()::getProgressPercentage,
					//									recipeMachine.getInputInventory().inventory,
					//									recipeMachine.getOutputInventory().inventory,
					//									recipeMachine.getInputTank(),
					//									recipeMachine.getOutputTank()
					//							),
					//							autoCalc
					//					);
				}
			}
	);
}
