package conductance.api.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.capability.cover.ICoverable;
import conductance.api.capability.energy.IEnergyHandler;

public final class CapabilityHelper {

	public static final BlockCapability<IEnergyHandler, @Nullable Direction> ENERGY_HANDLER_BLOCK;
	public static final BlockCapability<ICoverable, Void> COVERABLE_BLOCK;

	@Nullable
	public static IEnergyHandler getEnergyHandler(final Level level, final BlockPos pos, @Nullable final Direction side) {
		return level.getCapability(CapabilityHelper.ENERGY_HANDLER_BLOCK, pos, side);
	}

	@Nullable
	public static ICoverable getCoverable(final Level level, final BlockPos pos) {
		return level.getCapability(CapabilityHelper.COVERABLE_BLOCK, pos);
	}

	static {
		ENERGY_HANDLER_BLOCK = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "energy"), IEnergyHandler.class);
		COVERABLE_BLOCK = BlockCapability.createVoid(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "covers"), ICoverable.class);
	}

	private CapabilityHelper() {
	}
}
