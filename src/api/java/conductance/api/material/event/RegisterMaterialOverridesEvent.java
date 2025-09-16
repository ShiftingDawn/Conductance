package conductance.api.material.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialOverridesEvent extends IConductancePluginEvent {

	void add(Material material, MaterialGenerationHandler handler, @Nullable Block block);

	void add(Material material, MaterialGenerationHandler handler, @Nullable Item item);
}
