package conductance.api.material;

import net.neoforged.neoforge.common.util.Lazy;
import conductance.api.CAPI;
import conductance.api.registry.RegistryObject;

public final class MaterialTextureSet extends RegistryObject<String> {

	private final Lazy<MaterialTextureSet> parentSet;

	public MaterialTextureSet(final String registryName, final String parentSet) {
		super(registryName);
		this.parentSet = Lazy.of(() -> {
			if (this.isRootSet()) {
				return this;
			}
			MaterialTextureSet result = CAPI.REGS.materialTextureSets().get(parentSet);
			if (result == null) {
				throw new IllegalStateException("Tried to fetch unregistered parent MaterialTextureSet[%s] for MaterialTextureSet[%s]".formatted(parentSet, getRegistryKey()));
			}
			return result;
		});
	}

	public boolean isRootSet() {
		return this.getRegistryKey().equals("dull");
	}

	public MaterialTextureSet getParentSet() {
		return this.parentSet.get();
	}
}
