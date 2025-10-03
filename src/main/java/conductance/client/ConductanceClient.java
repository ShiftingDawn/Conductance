package conductance.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterBlockStateModels;
import net.neoforged.neoforge.common.NeoForge;
import conductance.api.block.BlockRotationHelper;
import conductance.Conductance;
import conductance.client.model.ExtendedRotationUnbakedModel;

@Mod(value = Conductance.MODID, dist = Dist.CLIENT)
public final class ConductanceClient extends Conductance {

	public ConductanceClient(final IEventBus modEventBus, final ModContainer modContainer) {
		super(Dist.CLIENT, modEventBus, modContainer);

		modEventBus.addListener(RegisterBlockStateModels.class, this::onRegisterBlockStateModels);
		GridInteractionRenderer.init(NeoForge.EVENT_BUS);
	}

	private void onRegisterBlockStateModels(final RegisterBlockStateModels event) {
		event.registerModel(BlockRotationHelper.LOADER_ID, ExtendedRotationUnbakedModel.MAP_CODEC);
	}
}
