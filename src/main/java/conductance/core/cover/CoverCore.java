package conductance.core.cover;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import conductance.api.cover.CoverEntity;
import conductance.api.cover.CoverEntityConstructor;
import conductance.api.cover.CoverQuadProvider;
import conductance.api.cover.CoverType;
import conductance.api.cover.event.RegisterCoverEvent;
import conductance.Conductance;
import conductance.core.register.RegisterCore;

public final class CoverCore {

	public static void initialize() {
		Conductance.dispatch(RegisterCoverEvent.class, modid -> new RegisterCoverEventImpl(new RegisterCoverEventImpl.CoverRegister() {

			@Override
			public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final Function<CoverType<COVER>, Supplier<CoverQuadProvider>> coverRenderer,
					final CoverEntityConstructor<COVER> constructor) {
				return Util.make(new CoverTypeImpl<>(ResourceLocation.fromNamespaceAndPath(modid, registryName), coverRenderer, constructor),
						RegisterCore.REGS.covers()::register
				);
			}
		}));
	}

	private CoverCore() {
	}
}
