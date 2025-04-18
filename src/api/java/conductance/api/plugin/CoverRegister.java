package conductance.api.plugin;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverRenderer;
import conductance.api.capability.cover.CoverType;
import conductance.api.capability.cover.SimpleTextureCoverRenderer;

public interface CoverRegister {

	<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, Function<CoverType<COVER>, CoverRenderer> coverRenderer, CoverEntityConstructor<COVER> constructor);

	default <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final ResourceLocation coverTexture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverTexture), constructor);
	}

	default <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final String texture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverType.getRegistryKey().withPath(("block/cover/" + texture))), constructor);
	}

	default <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverType.getRegistryKey().withPrefix("block/cover/")), constructor);
	}
}
