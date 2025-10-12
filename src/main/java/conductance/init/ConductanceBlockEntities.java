package conductance.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.Conductance;
import conductance.init.block.WireBlockEntity;
import conductance.lib.pipenet.WireRegistry;

public final class ConductanceBlockEntities {

	private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Conductance.MODID);
	public static final Supplier<BlockEntityType<WireBlockEntity>> WIRE = ConductanceBlockEntities.REGISTER.register("wire",
		() -> new BlockEntityType<>(WireBlockEntity::new, false, WireRegistry.getAllBlocks()));

	public static void initialize(final IEventBus modEventBus) {
		ConductanceBlockEntities.REGISTER.register(modEventBus);
	}

	private ConductanceBlockEntities() {
	}
}
