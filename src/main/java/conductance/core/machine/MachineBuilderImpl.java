package conductance.core.machine;

import net.minecraft.Util;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.IMbeBlock;
import conductance.api.machine.MachineBlockEntityFactory;
import conductance.api.machine.MachineBlockFactory;
import conductance.api.machine.MachineBuilder;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.MetaBlockEntityBlock;
import conductance.api.machine.MetaBlockEntityBlockEntity;
import conductance.api.machine.MetaBlockEntityType;
import conductance.api.resource.RuntimeModelProvider;
import static conductance.core.apiimpl.ApiBridge.getRegistrate;

public class MachineBuilderImpl<T extends MetaBlockEntity<T>> implements MachineBuilder<T> {

	private final String registryKey;
	@Setter
	private MachineBlockFactory blockFactory = MetaBlockEntityBlock::new;
	@Setter
	private MachineBlockEntityFactory<T, ?> blockEntityFactory = (MachineBlockEntityFactory<T, MetaBlockEntityBlockEntity<T>>) MetaBlockEntityBlockEntity::new;
	@Setter
	@Getter
	private RuntimeModelProvider modelProvider = new DirectionalMachineRuntimeModelProvider();

	public MachineBuilderImpl(final String registryKey) {
		this.registryKey = registryKey;
	}

	private BlockEntry<? extends Block> createBlock(final MetaBlockEntityTypeImpl<T> metaBlockEntityType) {
		final BlockBuilder<Block, Registrate> blockBuilder = getRegistrate().block(this.registryKey, props -> {
			final Block block = this.blockFactory.newInstance(props, metaBlockEntityType);
			if (!(block instanceof IMbeBlock)) {
				throw new RuntimeException("Cannot use Block %s as machine block since it doesn't implement %s".formatted(block.getClass().getName(), IMbeBlock.class.getName()));
			}
			return block;
		});
		blockBuilder
				.initialProperties(() -> Blocks.IRON_BLOCK)
				.blockstate(NonNullBiConsumer.noop())
				.item(BlockItem::new)
				.model(NonNullBiConsumer.noop())
				.build();
		return blockBuilder.register();
	}

	private BlockEntityEntry<?> createBlockEntity(final MetaBlockEntityTypeImpl<T> metaBlockEntityType) {
		final BlockEntityBuilder<BlockEntity, Registrate> builder = getRegistrate().blockEntity(this.registryKey, (type, pos, state) -> this.blockEntityFactory.newInstance(pos, state, metaBlockEntityType));
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
