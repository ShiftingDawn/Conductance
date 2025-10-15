package conductance.api.coil;

import java.util.List;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.UnknownNullability;
import conductance.api.NCBlocks;

public interface CoilBlockRegistry {

	CoilBlockType first();

	CoilBlockType last();

	CoilBlockType getByTemperature(int temperature);

	List<CoilBlockType> getByTemperatureOrHigher(int temperature);

	@UnknownNullability
	default CoilBlockType getByBlock(final Block block) {
		for (final Map.Entry<CoilBlockType, Holder<Block>> entry : NCBlocks.COILS.entrySet()) {
			if (entry.getValue().value() == block) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * @return all registered tiers, sorted by temperature
	 */
	List<CoilBlockType> getCoils();
}
