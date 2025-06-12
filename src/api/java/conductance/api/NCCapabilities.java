package conductance.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.jetbrains.annotations.Nullable;
import conductance.api.cover.ICoverable;
import conductance.api.energy.IEnergyHandler;

public final class NCCapabilities {

	public static final BlockCapability<IEnergyHandler, @Nullable Direction> ENERGY_HANDLER_BLOCK;
	public static final BlockCapability<ICoverable, Void> COVERABLE_BLOCK;

	@Nullable
	public static IEnergyHandler getEnergyHandler(final Level level, final BlockPos pos, @Nullable final Direction side) {
		return level.getCapability(NCCapabilities.ENERGY_HANDLER_BLOCK, pos, side);
	}

	@Nullable
	public static ICoverable getCoverable(final Level level, final BlockPos pos) {
		return level.getCapability(NCCapabilities.COVERABLE_BLOCK, pos);
	}

	static {
		ENERGY_HANDLER_BLOCK = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "energy"), IEnergyHandler.class);
		COVERABLE_BLOCK = BlockCapability.createVoid(ResourceLocation.fromNamespaceAndPath(CAPI.MOD_ID, "covers"), ICoverable.class);
	}

	private NCCapabilities() {
	}
}
