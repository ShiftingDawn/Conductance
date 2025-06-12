package conductance.api.plugin;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import conductance.api.cover.CoverEntity;
import conductance.api.cover.CoverEntityConstructor;
import conductance.api.cover.CoverQuadProvider;
import conductance.api.cover.CoverType;

public interface RegisterCoverEvent extends IConductancePluginEvent {

	<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, Function<CoverType<COVER>, Supplier<CoverQuadProvider>> coverRenderer, CoverEntityConstructor<COVER> constructor);

	<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, ResourceLocation coverTexture, CoverEntityConstructor<COVER> constructor);

	<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, String texture, CoverEntityConstructor<COVER> constructor);

	<COVER extends CoverEntity<COVER>> CoverType<COVER> register(String registryName, CoverEntityConstructor<COVER> constructor);
}
