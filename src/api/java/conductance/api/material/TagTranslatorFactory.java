package conductance.api.material;

import org.jetbrains.annotations.Nullable;

public interface TagTranslatorFactory {

	@Nullable
	String translate(Material material);
}
