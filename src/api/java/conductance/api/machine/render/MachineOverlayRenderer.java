package conductance.api.machine.render;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.client.renderer.impl.IModelRenderer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

@SuppressWarnings({ "removal", "deprecation" })
public class MachineOverlayRenderer extends MachineRenderer {

	@Getter
	@Setter
	private IModelRenderer overlayModel;

	public MachineOverlayRenderer(final ResourceLocation modelLocation, final ResourceLocation overlayModelLocation) {
		super(modelLocation);
		this.overlayModel = new IModelRenderer(overlayModelLocation);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderMachine(
			final List<BakedQuad> quads, final MachineType<?> machineType, @Nullable final MachineBlockEntity<?> machine, final Direction front, @Nullable final Direction side,
			final RandomSource rand, @Nullable final Direction modelFacing, final ModelState modelState
	) {
		super.renderMachine(quads, machineType, machine, front, side, rand, modelFacing, modelState);
		quads.addAll(this.overlayModel.getRotatedModel(front).getQuads(machineType.getDefaultBlockState(), side, rand));
	}
}
