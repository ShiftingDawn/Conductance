package conductance.init;

import java.util.function.Supplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import conductance.api.NCBlocks;
import conductance.Conductance;
import conductance.init.block.CreativeTankBlockEntity;
import conductance.init.block.CreativeTankBlockEntityRenderer;
import conductance.init.block.WireBlockEntity;
import conductance.lib.pipenet.WireRegistry;

public final class ConductanceBlockEntities {

	private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Conductance.MODID);

	public static final Supplier<BlockEntityType<CreativeTankBlockEntity>> CREATIVE_TANK = ConductanceBlockEntities.REGISTER.register("creative_tank",
		() -> new BlockEntityType<>(CreativeTankBlockEntity::new, false, NCBlocks.CREATIVE_TANK.value()));

	public static final Supplier<BlockEntityType<WireBlockEntity>> WIRE = ConductanceBlockEntities.REGISTER.register("wire",
		() -> new BlockEntityType<>(WireBlockEntity::new, false, WireRegistry.getAllBlocks()));

	public static void initialize(final IEventBus modEventBus) {
		ConductanceBlockEntities.REGISTER.register(modEventBus);
		modEventBus.addListener(EntityRenderersEvent.RegisterRenderers.class, ConductanceBlockEntities::registerEntityRenderers);
	}

	private static void registerEntityRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(ConductanceBlockEntities.CREATIVE_TANK.get(), CreativeTankBlockEntityRenderer::new);
	}

	private ConductanceBlockEntities() {
	}
}
