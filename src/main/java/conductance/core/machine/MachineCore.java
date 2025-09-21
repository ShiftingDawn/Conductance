package conductance.core.machine;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBlockFactory;
import conductance.api.machine.event.MachineBlockItemFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.Conductance;

public final class MachineCore {

	private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Conductance.MODID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Conductance.MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		MachineCore.BLOCKS.register(modEventBus);
		MachineCore.ITEMS.register(modEventBus);
		MachineCore.BLOCK_ENTITIES.register(modEventBus);

		Conductance.dispatch(RegisterMachineEvent.class, modid -> new RegisterMachineEventImpl(new RegisterMachineEventImpl.Delegate() {
			@Override
			public <T extends MachineBlockEntity<T>> MachineType<T> apply(final String registryName, final MachineBlockEntityFactory<T> blockEntityFactory, final Consumer<MachineBuilder<T>> builder) {
				final ResourceLocation registryKey = ResourceLocation.fromNamespaceAndPath(modid, registryName);
				final MachineType<T> result = Util.make(new MachineBuilderImpl<>(registryKey, blockEntityFactory), builder).build();
				Conductance.REGISTRIES.register(CAPI.regs().machines(), registryKey, result);
				return result;
			}
		}));
	}

	static <T extends MachineBlockEntity<T>> Supplier<MachineBlock<T>> createBlock(final String registryName, final MachineTypeImpl<T> machineType, final MachineBlockFactory<T> blockFactory) {
		return MachineCore.BLOCKS.registerBlock(registryName, props -> {
			//TODO builder callback here
			return blockFactory.apply(props, machineType);
		}, BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK));
	}

	static <T extends MachineBlockEntity<T>> Supplier<MachineBlockItem<T>> createItem(final String registryName, final Supplier<MachineBlock<T>> block, final MachineBlockItemFactory<T> itemFactory) {
		return MachineCore.ITEMS.registerItem(registryName, props -> {
			//TODO builder callback here
			return itemFactory.apply(block.get(), props);
		}, new Item.Properties());
	}

	static <T extends MachineBlockEntity<T>> Supplier<BlockEntityType<T>> createBlockEntityType(
		final String registryName, final MachineTypeImpl<T> machineType, final Supplier<MachineBlock<T>> block, final MachineBlockEntityFactory<T> blockEntityFactory
	) {
		return MachineCore.BLOCK_ENTITIES.register(registryName, () -> new BlockEntityType<>(
			(blockPos, blockState) -> blockEntityFactory.apply(machineType, blockPos, blockState),
			block.get()
		));
	}

	private MachineCore() {
	}
}
