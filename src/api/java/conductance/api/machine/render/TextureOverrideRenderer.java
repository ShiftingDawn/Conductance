package conductance.api.machine.render;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

@SuppressWarnings("removal")
@Getter
public class TextureOverrideRenderer extends RebakedModelRenderer {

	@Setter
	private Map<String, ResourceLocation> overrides;
	@Nullable
	private Supplier<Map<String, ResourceLocation>> overrideSupplier;

	public TextureOverrideRenderer(final ResourceLocation modelLocation, final Map<String, ResourceLocation> overrides) {
		super(modelLocation);
		this.overrides = overrides;
		if (CAPI.isClient()) {
			this.registerEvent();
		}
	}

	public TextureOverrideRenderer(final ResourceLocation modelLocation, final Supplier<Map<String, ResourceLocation>> overridesSupplier) {
		super(modelLocation);
		this.overrides = Collections.emptyMap();
		this.overrideSupplier = overridesSupplier;
		if (CAPI.isClient()) {
			this.registerEvent();
		}
	}

	public TextureOverrideRenderer(final ResourceLocation modelLocation) {
		super(modelLocation);
		this.overrides = Collections.emptyMap();
		if (CAPI.isClient()) {
			this.registerEvent();
		}
	}

	@Override
	@Nullable
	@OnlyIn(Dist.CLIENT)
	protected BakedModel getItemBakedModel() {
		if (this.itemModel == null) {
			UnbakedModel model = this.getModel();
			if (model instanceof final BlockModel blockModel && blockModel.getRootModel() == ModelBakery.GENERATION_MARKER) {
				model = ModelFactory.ITEM_MODEL_GENERATOR.generateBlockModel(new TextureOverrider(this.overrides), blockModel);
			}
			this.itemModel = model.bake(ModelFactory.getModelBaker(), new TextureOverrider(this.overrides), BlockModelRotation.X0_Y0);
		}
		return this.itemModel;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public BakedModel getRotatedModel(final Direction front) {
		return this.blockModels.computeIfAbsent(front, side -> this.getModel().bake(ModelFactory.getModelBaker(), new TextureOverrider(this.overrides), ModelFactory.getRotation(side)));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		super.onPrepareTextureAtlas(atlasName, register);
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			if (this.overrideSupplier != null) {
				this.overrides = this.overrideSupplier.get();
			}
			this.overrides.values().forEach(register);
		}
	}
}
