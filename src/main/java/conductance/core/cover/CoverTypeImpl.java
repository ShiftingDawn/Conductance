package conductance.core.cover;

import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import conductance.api.CAPI;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverManager;
import conductance.api.capability.cover.CoverRenderer;
import conductance.api.capability.cover.CoverType;
import conductance.api.registry.RegistryObject;

public final class CoverTypeImpl<COVER extends CoverEntity<COVER>> extends RegistryObject<ResourceLocation> implements CoverType<COVER> {

	private final CoverRenderer coverRenderer;
	private final CoverEntityConstructor<COVER> constructor;

	public CoverTypeImpl(final ResourceLocation registryKey, final Function<CoverType<COVER>, CoverRenderer> coverRenderer, final CoverEntityConstructor<COVER> constructor) {
		super(registryKey);
		this.coverRenderer = coverRenderer.apply(this);
		this.constructor = constructor;
		CAPI.regs().covers().register(this.getRegistryKey(), this);
	}

	@Override
	public CoverRenderer getRenderer() {
		return this.coverRenderer;
	}

	@Override
	public COVER instantiate(final CoverManager manager, final Direction side) {
		return this.constructor.instantiate(manager, this, side);
	}
}
