package conductance.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialTraits;
import conductance.api.capability.CapabilityHelper;
import conductance.api.capability.energy.IEnergyHandler;
import conductance.api.material.traits.MaterialTraitCable;
import conductance.api.util.InteractType;
import conductance.core.pipenet.CableData;
import conductance.core.pipenet.CableType;
import conductance.core.pipenet.EnergyNet;
import conductance.core.pipenet.EnergyNetHandler;
import conductance.core.pipenet.LevelEnergyNet;
import conductance.core.pipenet.PipeNetHelper;

public class CableBlockEntity extends PipeBlockEntity<CableData, LevelEnergyNet> {

	private final Lazy<CableData> cableData = Lazy.of(() -> {
		if (this.getPipeBlock() instanceof CableBlock cableBlock) {
			MaterialTraitCable props = cableBlock.getMaterial().getTrait(NCMaterialTraits.CABLE);
			CableType type = cableBlock.getCableType();
			assert props != null;
			return new CableData(props, type);
		}
		throw new AssertionError();
	});

	public CableBlockEntity(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
		super(type, pos, state);
	}

	@Override
	public LevelEnergyNet getNetwork(final ServerLevel serverLevel) {
		return LevelEnergyNet.getOrCreate(serverLevel);
	}

	@Override
	protected InteractType getInteractType() {
		return InteractType.WIRE_CUTTERS;
	}

	@Override
	public CableData getData() {
		return this.cableData.get();
	}

	@Override
	public ResourceLocation getNodeType() {
		return EnergyNet.TYPE;
	}

	@Override
	public boolean canConnectTo(final Level level, final BlockPos pos, final Direction side) {
		return CapabilityHelper.getEnergyHandler(level, pos.relative(side), side.getOpposite()) != null;
	}

	@Nullable
	public IEnergyHandler getEnergyHandler(@Nullable final Direction side) {
		if (side == null || PipeNetHelper.isBlocked(this.getConnections(), side)) {
			return null;
		}
		if (this.getLevel() instanceof final ServerLevel serverLevel) {
			return new EnergyNetHandler(serverLevel, this);
		}
		return IEnergyHandler.DEFAULT;
	}
}
