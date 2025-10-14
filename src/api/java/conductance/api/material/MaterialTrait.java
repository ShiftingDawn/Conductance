package conductance.api.material;

import java.util.List;

public interface MaterialTrait<T extends MaterialTrait<T>> {

	List<String> validate(Material material);
}
