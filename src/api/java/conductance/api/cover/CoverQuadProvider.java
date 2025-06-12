package conductance.api.cover;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CoverQuadProvider {

	List<BakedQuad> getCoverQuads(Direction face, RandomSource rand, @NotNull CoverEntity<?> cover, @Nullable Direction modelFacing, ModelState modelState);
}
