package conductance.core.machine;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineModelType;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.IRecipe;
import conductance.api.machine.recipe.IRecipeElementType;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.registry.RegistryObject;

//FIXME refactor
class MachineTypeImpl<T extends MachineBlockEntity<T>> extends RegistryObject<String> implements MachineType<T> {

	@Setter(AccessLevel.PACKAGE)
	private BlockEntry<? extends MachineBlock<T>> block;
	@Setter(AccessLevel.PACKAGE)
	private BlockEntityEntry<T> blockEntityType;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private NCRecipeType[] recipeTypes;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Object2IntMap<IRecipeElementType<?>> recipeOutputLimits;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	@Nullable
	private BiFunction<MachineBlockEntity<?>, IRecipe, IRecipe> recipeModifier;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	@Nullable
	private MachineGuiSupplier guiSupplier;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	@Nullable
	private String localizedName;
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private BiConsumer<ItemStack, List<Component>> tooltipBuilder;
	@Setter(AccessLevel.PACKAGE)
	@Getter
	private MachineModelType<?> modelType;
	@Setter(AccessLevel.PACKAGE)
	@Getter
	@UnknownNullability
	private Object modelData;

	MachineTypeImpl(final String registryKey) {
		super(registryKey);
	}

	/**
	 * Override to add validation after creation. DO NOT FORGET TO CALL SUPER
	 */
	protected void validate() {
		Objects.requireNonNull(this.block, "No block");
		Objects.requireNonNull(this.blockEntityType, "No block entity type");
		Objects.requireNonNull(this.recipeTypes, "No recipe types");
		Objects.requireNonNull(this.tooltipBuilder, "No tooltip builder");
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
