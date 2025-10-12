package conductance.lib.pipenet;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import conductance.api.CAPI;

public final class LevelEnergyNet extends LevelPipeNetwork<IWireNode, WireData> {

	public static final SavedDataType<LevelEnergyNet> ID = new SavedDataType<>(
		"%s_enet".formatted(CAPI.MOD_ID),
		LevelEnergyNet::new,
		ctx -> RecordCodecBuilder.create(instance -> instance.group(
			RecordCodecBuilder.point(ctx)
		).apply(instance, LevelEnergyNet::new))
	);

	public LevelEnergyNet(final SavedData.Context ctx) {
		super(ctx);
	}

	@Override
	protected PipeNetwork<IWireNode, WireData> createNetwork() {
		return new EnergyNet(this);
	}

	public static LevelEnergyNet getOrCreate(final ServerLevel serverLevel) {
		return serverLevel.getDataStorage().computeIfAbsent(LevelEnergyNet.ID);
	}
}
