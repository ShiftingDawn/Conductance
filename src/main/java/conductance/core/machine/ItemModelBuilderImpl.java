package conductance.core.machine;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;
import conductance.api.resource.ItemModelBuilder;

public final class ItemModelBuilderImpl extends ModelBuilderImpl<ItemModelBuilderImpl> implements ItemModelBuilder<ItemModelBuilderImpl> {

	@Nullable
	private BlockModel.GuiLight guiLight;

	public ItemModelBuilderImpl() {
		super(ResourceLocation.withDefaultNamespace("item/generated"));
	}

	@Override
	public ItemModelBuilderImpl guiLight(final BlockModel.GuiLight newGuiLight) {
		this.guiLight = newGuiLight;
		return this;
	}

	@Override
	protected void addJsonProperties(final JsonObject json) {
		if (this.guiLight != null) {
			json.addProperty("gui_light", this.guiLight.getSerializedName());
		}
	}
}
