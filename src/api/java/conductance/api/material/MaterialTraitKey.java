package conductance.api.material;

import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import conductance.api.CAPI;

@RequiredArgsConstructor
public final class MaterialTraitKey<T extends MaterialTrait<T>> {

	@Getter
	private final Class<T> typeClass;

	public ResourceLocation getId() {
		return Objects.requireNonNull(CAPI.regs().materialTraits().getKey(this), "unregistered material trait");
	}
}
