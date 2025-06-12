package conductance.core.cover;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import lombok.AllArgsConstructor;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverQuadProvider;
import conductance.api.capability.cover.CoverType;
import conductance.api.capability.cover.SimpleTextureCoverQuadProvider;
import conductance.api.plugin.RegisterCoverEvent;

@AllArgsConstructor
final class RegisterCoverEventImpl implements RegisterCoverEvent {

	public interface CoverRegister {

		<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, Function<CoverType<COVER>, Supplier<CoverQuadProvider>> coverRenderer, CoverEntityConstructor<COVER> constructor);
	}

	private final CoverRegister delegate;

	@Override
	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final Function<CoverType<COVER>, Supplier<CoverQuadProvider>> coverRenderer,
			final CoverEntityConstructor<COVER> constructor) {
		return this.delegate.register(registryName, coverRenderer, constructor);
	}

	@Override
	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final ResourceLocation coverTexture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> () -> new SimpleTextureCoverQuadProvider(coverTexture), constructor);
	}

	@Override
	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final String texture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> () -> new SimpleTextureCoverQuadProvider(coverType.getRegistryKey().withPath("cover/" + texture)), constructor);
	}

	@Override
	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> () -> new SimpleTextureCoverQuadProvider(coverType.getRegistryKey().withPrefix("cover/")), constructor);
	}

}
