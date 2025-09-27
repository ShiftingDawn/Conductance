package conductance.core.machine;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.CAPI;
import conductance.api.machine.CapabilityMode;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineType;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBlockFactory;
import conductance.api.machine.event.MachineBlockItemFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.machine.event.RegisterMachineEvent;
import conductance.api.machine.gui.MachineMenu;
import conductance.api.machine.gui.MachineScreen;
import conductance.api.resource.event.AddRuntimeModelEvent;
import conductance.Conductance;
import conductance.core.CreativeTabHelper;

public final class MachineCore {

	private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Conductance.MODID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Conductance.MODID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Conductance.MODID);
	private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(BuiltInRegistries.MENU, Conductance.MODID);

	public static void initialize(final IEventBus modEventBus) {
		MachineCore.BLOCKS.register(modEventBus);
		MachineCore.ITEMS.register(modEventBus);
		MachineCore.BLOCK_ENTITIES.register(modEventBus);
		MachineCore.MENU_TYPES.register(modEventBus);
		modEventBus.addListener(RegisterCapabilitiesEvent.class, MachineCore::attachCapabilities);

		final Supplier<MenuType<MachineMenu>> menuType = MachineCore.MENU_TYPES.register("machine", () -> IMenuTypeExtension.create(
			(containerId, inventory, buffer) -> {
				final BlockPos pos = buffer.readBlockPos();
				final MachineBlockEntity<?> mbe = (MachineBlockEntity<?>) inventory.player.level().getBlockEntity(pos);
				return new MachineMenu(mbe, containerId, ContainerLevelAccess.NULL, inventory);
			}
		));
		modEventBus.addListener(RegisterMenuScreensEvent.class, event -> {
			event.register(menuType.get(), MachineScreen::new);
		});

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

	public static void generateModels(final AddRuntimeModelEvent event) {
		MachineModelHandler.generate(event);
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
			return Util.make(itemFactory.apply(block.get(), props), item -> CreativeTabHelper.addToTab(item, CreativeTabHelper.Tabs.MACHINE));
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

	private static void attachCapabilities(final RegisterCapabilitiesEvent event) {
		BuiltInRegistries.BLOCK.forEach(block -> {
			if (block instanceof final MachineBlock<?> machineBlock) {
				event.registerBlock(Capabilities.ItemHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
					if (blockEntity instanceof final MachineBlockEntity<?> machine) {
						return machine.getItemTransferCapability(direction, CapabilityMode.DEFAULT).orElse(null);
					}
					return null;
				}, machineBlock);
				event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
					if (blockEntity instanceof final MachineBlockEntity<?> machine) {
						return machine.getFluidTransferCapability(direction, CapabilityMode.DEFAULT).orElse(null);
					}
					return null;
				}, machineBlock);
			}
		});
	}

	private MachineCore() {
	}
}
