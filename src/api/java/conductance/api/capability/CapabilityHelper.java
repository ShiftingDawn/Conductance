package conductance.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.capability.energy.IEnergyHandler;

public final class CapabilityHelper {

	public static final BlockCapability<IEnergyHandler, @Nullable Direction> ENERGY_HANDLER_BLOCK;

	@Nullable
	public static IEnergyHandler getEnergyHandler(final Level level, final BlockPos pos, @Nullable final Direction side) {
		return level.getCapability(CapabilityHelper.ENERGY_HANDLER_BLOCK, pos, side);
	}

	static {
		ENERGY_HANDLER_BLOCK = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "energy"), IEnergyHandler.class);
	}

	private CapabilityHelper() {
	}
}
