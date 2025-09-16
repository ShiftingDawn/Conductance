package conductance.api.material;

import java.util.List;
import javax.annotation.Nullable;

public interface MaterialFlagValidator {

	@Nullable
	List<String> validate(Material material);
}
