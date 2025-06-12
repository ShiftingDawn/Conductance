package conductance.core.cover;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.CoverQuadProvider;
import conductance.api.capability.cover.CoverType;
import conductance.api.registry.RegistryObject;

public final class CoverTypeImpl<COVER extends CoverEntity<COVER>> extends RegistryObject<ResourceLocation> implements CoverType<COVER> {

	private final Supplier<CoverQuadProvider> coverRenderer;
	private final CoverEntityConstructor<COVER> constructor;

	public CoverTypeImpl(final ResourceLocation registryKey, final Function<CoverType<COVER>, Supplier<CoverQuadProvider>> coverRenderer, final CoverEntityConstructor<COVER> constructor) {
		super(registryKey);
		this.coverRenderer = coverRenderer.apply(this);
		this.constructor = constructor;
	}

	@Override
	public Supplier<CoverQuadProvider> getRenderer() {
		return this.coverRenderer;
	}

	@Override
	public COVER instantiate(final CoverManager manager, final Direction side) {
		return this.constructor.instantiate(manager, this, side);
	}
}
