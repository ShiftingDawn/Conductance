package conductance.core.apiimpl;

import net.minecraft.resources.ResourceLocation;
import conductance.api.registry.IRegistryObject;
import conductance.Conductance;

public class ConductanceDataPackRegistry<TYPE extends IRegistryObject<ResourceLocation>> extends ConductanceRegistryImpl<ResourceLocation, TYPE> {

	public ConductanceDataPackRegistry(final ResourceLocation registryName) {
		super(registryName);
	}

	public void unfreeze() {
		Conductance.LOGGER.info("Registry {} has been unfrozen!", this.getRegistryKey());
		this.frozen = false;
	}

	public void reset() {
		if (this.frozen) {
			throw new IllegalStateException("[register]Registry %s has been frozen".formatted(this.getRegistryKey()));
		}
		this.registry().clear();
	}
}
