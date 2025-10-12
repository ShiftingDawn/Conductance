package conductance.init.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.ints.IntSortedSet;
import it.unimi.dsi.fastutil.ints.IntSortedSets;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineRecipeCapability;
import conductance.api.machine.RecipeCapabilityHolder;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.event.MachineRecipeModifier;
import conductance.api.machine.multi.IMultiBlockPart;
import conductance.api.machine.multi.MultiControllerMachineBlockEntity;
import conductance.api.machine.multi.MultiMachineType;
import conductance.api.machine.multi.StructureCheckContext;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.recipe.RecipeElementType;
import conductance.api.util.IO;

public class GenericRecipeMultiBlockMachine extends MultiControllerMachineBlockEntity<GenericRecipeMultiBlockMachine> implements RecipeCapabilityHolder {

	private final Table<RecipeElementType<?>, IO, List<MachineRecipeCapability<?>>> recipeCapabilities = HashBasedTable.create();
	private final @Getter RecipeHandler recipeHandler;
	private final Lazy<IntSortedSet> recipePrograms = Lazy.of(this::reloadRecipePrograms);

	public GenericRecipeMultiBlockMachine(final MultiMachineType<GenericRecipeMultiBlockMachine> type, final BlockPos pos, final BlockState blockState) {
		super(type, pos, blockState);
		this.recipeHandler = new RecipeHandler(this, this);
	}

	private void reloadRecipeCapabilities() {
		this.recipeCapabilities.clear();
		for (final IMultiBlockPart part : this.getParts()) {
			part.attachCapabilities((io, capability) -> {
				List<MachineRecipeCapability<?>> list = this.recipeCapabilities.get(capability.getElementType(), io);
				if (list == null) {
					list = new ArrayList<>();
					this.recipeCapabilities.put(capability.getElementType(), io, list);
				}
				list.add(capability);
			});
		}
		this.recipePrograms.invalidate();
	}

	private IntSortedSet reloadRecipePrograms() {
		//TODO implement
		return IntSortedSets.EMPTY_SET;
	}

	@Override
	public void onStructureFormed(final StructureCheckContext ctx) {
		super.onStructureFormed(ctx);
		this.reloadRecipeCapabilities();
	}

	@Override
	public MachineRecipeType getRecipeType() {
		return this.getMachineType().getRecipeTypes()[0];
	}

	@Override
	public @Nullable MachineRecipeModifier getRecipeModifier() {
		return this.getMachineType().getRecipeModifier();
	}

	@Override
	public List<MachineRecipeCapability<?>> getRecipeCapabilities(final RecipeElementType<?> elementType, final IO io) {
		return Objects.requireNonNullElse(this.recipeCapabilities.get(elementType, io), List.of());
	}

	@Override
	public IntSortedSet getRecipePrograms() {
		return this.recipePrograms.get();
	}
}
