package conductance.core.machine;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBlockFactory;
import conductance.api.machine.event.MachineBlockItemFactory;
import conductance.api.machine.event.MachineBuilder;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
final class MachineBuilderImpl<T extends MachineBlockEntity<T>> implements MachineBuilder<T> {

	private final ResourceLocation registryKey;
	private final MachineBlockEntityFactory<T> blockEntityFactory;
	private @Setter MachineBlockFactory<T> blockFactory = MachineBlock::new;
	private @Setter MachineBlockItemFactory<T> itemFactory = MachineBlockItem::new;

	private Supplier<BlockEntityType<T>> createBlockEntity(final MachineTypeImpl<T> machineType) {
		return () -> Util.make(new BlockEntityType<>((blockPos, blockState) -> this.blockEntityFactory.apply(machineType, blockPos, blockState)), blockEntityType -> {
			Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, this.registryKey, blockEntityType);
		});
	}

	public MachineTypeImpl<T> build() {
		return Util.make(new MachineTypeImpl<>(), type -> {
			type.setBlock(MachineCore.createBlock(this.registryKey.getPath(), type, this.blockFactory));
			type.setItem(MachineCore.createItem(this.registryKey.getPath(), type.getBlock(), this.itemFactory));
			type.setBlockEntityType(MachineCore.createBlockEntityType(this.registryKey.getPath(), type, type.getBlock(), this.blockEntityFactory));
		});
	}
}
