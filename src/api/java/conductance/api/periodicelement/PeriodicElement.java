package conductance.api.periodicelement;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import conductance.api.registry.RegistryObject;

public final class PeriodicElement extends RegistryObject<ResourceLocation> {

	private final long protons;
	private final long neutrons;
	private final String name;
	private final String symbol;
	@Nullable
	private final ResourceLocation parent;

	public PeriodicElement(final ResourceLocation registryName, final long protons, final long neutrons, final String name, final String symbol, @Nullable final ResourceLocation parent) {
		super(registryName);
		this.protons = protons;
		this.neutrons = neutrons;
		this.name = name;
		this.symbol = symbol;
		this.parent = parent;
	}

	public long protons() {
		return this.protons;
	}

	public long neutrons() {
		return this.neutrons;
	}

	public String name() {
		return this.name;
	}

	public String symbol() {
		return this.symbol;
	}

	@Nullable
	public ResourceLocation parent() {
		return this.parent;
	}

	public long mass() {
		return this.protons + this.neutrons;
	}
}
