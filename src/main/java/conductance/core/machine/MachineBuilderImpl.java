package conductance.core.machine;

import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBlockFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.MachineGuiSupplier;
import conductance.api.machine.recipe.NCRecipeType;
import conductance.api.resource.RuntimeModelProvider;
import static conductance.core.apiimpl.ApiBridge.getRegistrate;

public class MachineBuilderImpl<T extends MachineBlockEntity<T>> implements MachineBuilder<T> {

	private final String registryKey;
	@Setter
	private MachineBlockFactory<T> blockFactory = MachineBlock::new;
	@Setter
	private MachineBlockEntityFactory<T> blockEntityFactory;
	@Setter
	@Getter
	private RuntimeModelProvider modelProvider = new DirectionalMachineRuntimeModelProvider();
	@Getter
	private NCRecipeType[] recipeTypes = new NCRecipeType[0];
	@Getter
	private MachineGuiSupplier guiSupplier;

	public MachineBuilderImpl(final String registryKey, final MachineBlockEntityFactory<T> machineBlockEntityFactory) {
		this.registryKey = registryKey;
		this.blockEntityFactory = machineBlockEntityFactory;
	}

	private BlockEntry<? extends MachineBlock<T>> createBlock(final MachineTypeImpl<T> machineType) {
		final var blockBuilder = getRegistrate().block(this.registryKey, props -> this.blockFactory.newInstance(props, machineType));
		blockBuilder
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.item(BlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build();
		return blockBuilder.register();
	}

	private BlockEntityEntry<T> createBlockEntity(final MachineTypeImpl<T> machineType) {
		final BlockEntityBuilder<T, Registrate> builder = getRegistrate().blockEntity(this.registryKey, (type, pos, state) -> this.blockEntityFactory.newInstance(machineType, pos, state));
		builder.validBlock(machineType.getBlock());
		return builder.register();
	}

	@Override
	public MachineBuilder<T> recipeType(final NCRecipeType recipeType, final NCRecipeType... moreTypes) {
		this.recipeTypes = new NCRecipeType[moreTypes.length + 1];
		this.recipeTypes[0] = recipeType;
		System.arraycopy(moreTypes, 0, this.recipeTypes, 1, moreTypes.length);
		return this;
	}

	@Override
	public MachineBuilder<T> guiSupplier(final MachineGuiSupplier supplier) {
		this.guiSupplier = supplier;
		return this;
	}

	@Override
	public MachineType<T> build() {
		final MachineTypeImpl<T> machineType = Util.make(new MachineTypeImpl<>(this.registryKey), result -> {
			result.setBlock(this.createBlock(result));
			result.setBlockEntityType(this.createBlockEntity(result));
			result.setModelProvider(this.modelProvider);
			result.setRecipeTypes(this.recipeTypes);
			result.setGuiSupplier(this.guiSupplier);
		});
		machineType.validate();
		CAPI.regs().machines().register(machineType.getRegistryKey(), machineType);
		return machineType;
	}
}
