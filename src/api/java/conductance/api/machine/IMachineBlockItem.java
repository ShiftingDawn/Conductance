package conductance.api.machine;

import net.minecraft.world.item.ItemStack;
import com.lowdragmc.lowdraglib.client.renderer.IItemRendererProvider;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

public interface IMachineBlockItem<T extends MachineBlockEntity<T>> extends IItemRendererProvider {

	MachineType<T> getMachineType();

	@Override
	default IRenderer getRenderer(final ItemStack stack) {
		return this.getMachineType().getModelRenderer();
	}
}
