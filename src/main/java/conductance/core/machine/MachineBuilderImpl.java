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
import conductance.api.machine.MachineBlockFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityBlock;
import conductance.api.machine.MetaBlockEntityFactory;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.resource.RuntimeModelProvider;
import static conductance.core.apiimpl.ApiBridge.getRegistrate;

public class MachineBuilderImpl<T extends MetaBlockEntity<T>> implements MachineBuilder<T> {

	private final String registryKey;
	@Setter
	private MachineBlockFactory<T> blockFactory = MetaBlockEntityBlock::new;
	@Setter
	private MetaBlockEntityFactory<T> blockEntityFactory;
	@Setter
	@Getter
	private RuntimeModelProvider modelProvider = new DirectionalMachineRuntimeModelProvider();

	public MachineBuilderImpl(final String registryKey, final MetaBlockEntityFactory<T> metaBlockEntityFactory) {
		this.registryKey = registryKey;
		this.blockEntityFactory = metaBlockEntityFactory;
	}

	private BlockEntry<? extends MetaBlockEntityBlock<T>> createBlock(final MetaBlockEntityTypeImpl<T> metaBlockEntityType) {
		final var blockBuilder = getRegistrate().block(this.registryKey, props -> this.blockFactory.newInstance(props, metaBlockEntityType));
		blockBuilder
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.item(BlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build();
		return blockBuilder.register();
	}

	private BlockEntityEntry<T> createBlockEntity(final MetaBlockEntityTypeImpl<T> metaBlockEntityType) {
		final BlockEntityBuilder<T, Registrate> builder = getRegistrate().blockEntity(this.registryKey, (type, pos, state) -> this.blockEntityFactory.newInstance(metaBlockEntityType, pos, state));
		builder.validBlock(metaBlockEntityType.getBlock());
		return builder.register();
	}

	@Override
	public MetaBlockEntityType<T> build() {
		final MetaBlockEntityTypeImpl<T> metaBlockEntityType = Util.make(new MetaBlockEntityTypeImpl<>(this.registryKey), result -> {
			result.setBlock(this.createBlock(result));
			result.setBlockEntityType(this.createBlockEntity(result));
			result.setModelProvider(this.modelProvider);
		});
		metaBlockEntityType.validate();
		CAPI.regs().metaBlockEntities().register(metaBlockEntityType.getRegistryKey(), metaBlockEntityType);
		return metaBlockEntityType;
	}
}
