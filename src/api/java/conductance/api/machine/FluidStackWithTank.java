package conductance.api.machine;

import net.minecraft.util.ExtraCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

public record FluidStackWithTank(int tank, FluidStack stack) {

	public static final Codec<FluidStackWithTank> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		ExtraCodecs.UNSIGNED_BYTE.fieldOf("tank").orElse(0).forGetter(FluidStackWithTank::tank),
		FluidStack.CODEC.fieldOf("fluid").forGetter(FluidStackWithTank::stack)
	).apply(instance, FluidStackWithTank::new));

	public boolean isValidInContainer(final int tankCount) {
		return this.tank >= 0 && this.tank < tankCount;
	}
}
