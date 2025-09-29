package conductance.api.material.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialOverrideEvent extends IConductancePluginEvent {

	void add(MaterialGenerationHandler handler, Material material, @Nullable Block block);

	void add(MaterialGenerationHandler handler, Material material, @Nullable Item item);

	void add(MaterialGenerationHandler handler, Material material, @Nullable Fluid fluid);
}
