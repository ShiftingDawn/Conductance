package conductance.core.pipenet;

import net.minecraft.server.level.ServerLevel;
import conductance.api.CAPI;

public final class LevelEnergyNet extends LevelPipeNetwork<IWireNode, WireData> {

	public LevelEnergyNet(final ServerLevel level) {
		super(level);
	}

	@Override
	protected PipeNetwork<IWireNode, WireData> createNetwork() {
		return new EnergyNet(this);
	}

	public static LevelEnergyNet getOrCreate(final ServerLevel serverLevel) {
		return serverLevel.getDataStorage().computeIfAbsent(new Factory<>(
				() -> new LevelEnergyNet(serverLevel),
				(nbt, providers) -> new LevelEnergyNet(serverLevel)
		), "%s_enet".formatted(CAPI.MOD_ID));
	}
}
