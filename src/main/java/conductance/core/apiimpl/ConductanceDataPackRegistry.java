package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import conductance.api.registry.IRegistryObject;
import conductance.Conductance;

public class ConductanceDataPackRegistry<TYPE extends IRegistryObject<ResourceLocation>> extends ConductanceRegistryImpl<ResourceLocation, TYPE> {

	public ConductanceDataPackRegistry(final ResourceLocation registryName) {
		super(registryName);
	}

	public final void unfreeze() {
		Conductance.LOGGER.info("Registry {} has been unfrozen!", this.getRegistryKey());
		this.setFrozen(false);
	}

	public final void reset() {
		if (this.isFrozen()) {
			throw new IllegalStateException("[register]Registry %s has been frozen".formatted(this.getRegistryKey()));
		}
		this.registry().clear();
	}
}
