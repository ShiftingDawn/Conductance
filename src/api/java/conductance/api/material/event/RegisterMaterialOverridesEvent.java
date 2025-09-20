package conductance.api.material.event;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialOverridesEvent extends IConductancePluginEvent {

	void add(MaterialGenerationHandler handler, Material material, @Nullable Block block);

	void add(MaterialGenerationHandler handler, Material material, @Nullable Item item);

	void add(MaterialGenerationHandler handler, Material material, @Nullable Fluid fluid);

	default void regularGemOnly(final Material material, final Item regularGemItem) {
		this.add(NCMaterialGenerationHandlers.GEM, material, regularGemItem);
		this.add(NCMaterialGenerationHandlers.GEM_FLAWED, material, (Item) null);
		this.add(NCMaterialGenerationHandlers.GEM_FLAWLESS, material, (Item) null);
		this.add(NCMaterialGenerationHandlers.GEM_EXQUISITE, material, (Item) null);
	}
}
