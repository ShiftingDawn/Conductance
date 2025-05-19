package conductance.api.plugin;

import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import conductance.api.capability.cover.CoverEntity;
import conductance.api.capability.cover.CoverEntityConstructor;
import conductance.api.capability.cover.CoverRenderer;
import conductance.api.capability.cover.CoverType;
import conductance.api.capability.cover.SimpleTextureCoverRenderer;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegisterCoverEvent implements IConductancePluginEvent {

	public interface CoverRegister {

		<COVER extends CoverEntity<COVER>> CoverType<COVER> register(ResourceLocation registryName, Function<CoverType<COVER>, CoverRenderer> coverRenderer, CoverEntityConstructor<COVER> constructor);
	}

	private final String modid;
	private final CoverRegister delegate;

	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final Function<CoverType<COVER>, CoverRenderer> coverRenderer, final CoverEntityConstructor<COVER> constructor) {
		return this.delegate.register(ResourceLocation.fromNamespaceAndPath(this.modid, registryName), coverRenderer, constructor);
	}

	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final ResourceLocation coverTexture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverTexture), constructor);
	}

	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final String texture, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverType.getRegistryKey().withPath(("block/cover/" + texture))), constructor);
	}

	public <COVER extends CoverEntity<COVER>> CoverType<COVER> register(final String registryName, final CoverEntityConstructor<COVER> constructor) {
		return this.register(registryName, coverType -> new SimpleTextureCoverRenderer(coverType, coverType.getRegistryKey().withPrefix("block/cover/")), constructor);
	}

}
