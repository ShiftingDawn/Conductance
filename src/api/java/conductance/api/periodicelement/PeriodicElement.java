package conductance.api.periodicelement;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record PeriodicElement(long protons, long neutrons, String name, String symbol, @Nullable ResourceLocation parent) {

	public long mass() {
		return this.protons + this.neutrons;
	}
}
