package conductance.api.coil;

import java.util.Objects;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import conductance.api.CAPI;
import conductance.api.NCBlocks;

public interface CoilBlockType {

	boolean isFirst();

	boolean isLast();

	CoilBlockType getPreviousCoil();

	CoilBlockType getNextCoil();

	int getIndex();

	int getColor();

	int getTemperature();

	default Block getBlock() {
		return NCBlocks.COILS.get(this).value();
	}

	String getDescriptionId();

	Component getName();

	default ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().coilBlockTypes().getKey(this), "Unregistered coil block type");
	}
}
