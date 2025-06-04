package conductance.init.machine;

import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiHolder;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.machine.recipe.RecipeHolder;
import conductance.api.tier.Tier;
import conductance.client.MachineUIFactory;
import conductance.core.RecipeModifiers;

public class GenericGeneratorMachine extends TieredWorkableMachine<GenericGeneratorMachine> implements MachineGuiHolder {

	public GenericGeneratorMachine(final MachineType<GenericGeneratorMachine> machineType, final BlockPos pos, final BlockState blockState, final Tier tier) {
		super(machineType, pos, blockState, tier);
		this.getEnergy().setCapabilityValidator(side -> side == this.getFrontFacing());
		this.getEnergy().setSideOutputCondition(side -> side == this.getFrontFacing());
		this.getInputInventory().setCapabilityValidator(side -> side != this.getFrontFacing());
		this.getInputTank().setCapabilityValidator(side -> side != this.getFrontFacing());
		this.getOutputInventory().setCapabilityValidator(side -> side != this.getFrontFacing());
		this.getOutputTank().setCapabilityValidator(side -> side != this.getFrontFacing());
	}

	@Override
	protected boolean isEnergyGenerator() {
		return true;
	}

	@Override
	protected long getMaxEnergyAmperage() {
		return 1L;
	}

	@Override
	public ModularUI createUI(final Player entityPlayer) {
		return MachineUIFactory.createGui(this, this, entityPlayer);
	}

	public static IRecipe recipeModifier(final MachineBlockEntity<?> machine, final IRecipe recipe) {
		if (machine instanceof final GenericGeneratorMachine generator) {
			final long energy = recipe.getEnergyPerTick();
			if (energy > 0) {
				final int parallels = (int) (Math.min(generator.getOverclockVoltage(), generator.getOverclockTier().getVoltage()) / energy);
				return RecipeModifiers.parallels(generator, recipe, parallels, false).getA();
			}
		}
		return recipe;
	}

	public static final Function<NCRecipeType, MachineGuiSupplier> GUI_SUPPLIER = recipeType -> new MachineGuiSupplier(() ->
			recipeType.createGuiTemplate().createDefault(),
			(template, machine, theme, autoCalc) -> {
				if (machine instanceof GenericGeneratorMachine generatorMachine) {
					RecipeHolder recipeHolder = new RecipeHolder(
							generatorMachine.getRecipeProcessor()::getProgressPercentage,
							generatorMachine.getInputInventory().inventory,
							generatorMachine.getOutputInventory().inventory,
							generatorMachine.getInputTank(),
							generatorMachine.getOutputTank()
					);
					generatorMachine.getRecipeType().createGuiTemplate().setupGui(template, recipeHolder, theme, autoCalc);
				}
			}
	);
}
