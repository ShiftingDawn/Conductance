package conductance.core.machine;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.RegistryObject;
import conductance.api.resource.RuntimeModelProvider;

public class MachineTypeImpl<T extends MachineBlockEntity<T>> extends RegistryObject<String> implements MachineType<T> {

	@Setter(AccessLevel.PACKAGE)
	private BlockEntry<? extends MachineBlock<T>> block;
	@Setter(AccessLevel.PACKAGE)
	private BlockEntityEntry<T> blockEntityType;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Function<MachineType<?>, RuntimeModelProvider> modelProvider;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private NCRecipeType[] recipeTypes;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Object2IntMap<IRecipeElementType<?>> recipeOutputLimits = new Object2IntOpenHashMap<>();
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> recipeModifier;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private MachineGuiSupplier guiSupplier;

	public MachineTypeImpl(final String registryKey) {
		super(registryKey);
	}

	/**
	 * Override to add validation after creation. DO NOT FORGET TO CALL SUPER
	 */
	protected void validate() {
		Objects.requireNonNull(this.block, "No block");
		Objects.requireNonNull(this.blockEntityType, "No block entity type");
		Objects.requireNonNull(this.modelProvider, "No model provider");
		Objects.requireNonNull(this.recipeTypes, "No recipe types");
	}

	@Override
	public NonNullSupplier<? extends MachineBlock<T>> getBlock() {
		return this.block;
	}

	@Override
	public NonNullSupplier<BlockEntityType<T>> getBlockEntityType() {
		return this.blockEntityType;
	}
}
