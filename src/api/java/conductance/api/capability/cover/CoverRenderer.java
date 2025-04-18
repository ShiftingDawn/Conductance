package conductance.api.capability.cover;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;

public abstract class CoverRenderer implements IRenderer {

	@Getter(AccessLevel.PROTECTED)
	private final CoverType<?> coverType;

	public CoverRenderer(final CoverType<?> coverType) {
		this.coverType = coverType;
		if (CAPI.isClient()) {
			this.registerEvent();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public abstract void renderCover(List<BakedQuad> quads, @Nullable Direction side, RandomSource rand, @NotNull CoverEntity<?> cover, @Nullable Direction modelFacing, ModelState modelState);
}
