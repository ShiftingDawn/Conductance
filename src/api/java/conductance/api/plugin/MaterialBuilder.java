package conductance.api.plugin;

import java.util.Collection;
import java.util.List;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialStack;
import conductance.api.material.MaterialTextureSet;
import conductance.api.material.PeriodicElement;

public interface MaterialBuilder {

	MaterialBuilder dust();

	MaterialBuilder dust(int harvestLevel);

	MaterialBuilder dust(int harvestLevel, int burnTime);

	MaterialBuilder ingot();

	MaterialBuilder ingot(int harvestLevel);

	MaterialBuilder ingot(int harvestLevel, int burnTime);

	MaterialBuilder gem();

	MaterialBuilder gem(int harvestLevel);

	MaterialBuilder gem(int harvestLevel, int burnTime);

	MaterialBuilder burnTime(int burnTime);

	MaterialBuilder color(int color);

	MaterialBuilder color(int r, int g, int b);

	MaterialBuilder calcColor();

	MaterialBuilder textureSet(MaterialTextureSet set);

	MaterialBuilder formula(String formula);

	MaterialBuilder components(Object... components);

	MaterialBuilder components(MaterialStack... components);

	MaterialBuilder components(List<MaterialStack> components);

	MaterialBuilder flags(MaterialFlag... flags);

	MaterialBuilder addFlagAndPreset(Collection<MaterialFlag> preset, MaterialFlag... flags);

	MaterialBuilder periodicElement(PeriodicElement periodicElement);

	MaterialBuilder ore();

	MaterialBuilder ore(boolean emissive);

	MaterialBuilder ore(int oreMultiplier, int byproductMultiplier);

	MaterialBuilder ore(int oreMultiplier, int byproductMultiplier, boolean emissive);

	MaterialBuilder wood();

	Material build();
}
