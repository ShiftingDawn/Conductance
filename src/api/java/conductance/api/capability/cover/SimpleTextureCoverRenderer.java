package conductance.api.capability.cover;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.bakedpipeline.FaceQuad;
import com.lowdragmc.lowdraglib.client.model.ModelFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleTextureCoverRenderer extends CoverRenderer {

	private final ResourceLocation texture;

	public SimpleTextureCoverRenderer(final CoverType<?> coverType, final ResourceLocation texture) {
		super(coverType);
		this.texture = texture;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderCover(final List<BakedQuad> quads, @Nullable final Direction side, final RandomSource rand, @NotNull final CoverEntity<?> cover, @Nullable final Direction modelFacing, final ModelState modelState) {
		quads.add(FaceQuad.bakeFace(modelFacing, ModelFactory.getBlockSprite(this.texture), modelState));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			register.accept(this.texture);
		}
	}
}
