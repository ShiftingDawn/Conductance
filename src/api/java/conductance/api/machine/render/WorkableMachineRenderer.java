package conductance.api.machine.render;

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
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.IWorkable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineType;

public class WorkableMachineRenderer extends MachineRenderer {

	@Getter
	@Setter
	private WorkableOverlayModelData overlayData;

	public WorkableMachineRenderer(final ResourceLocation modelLocation, final ResourceLocation overlayModelLocation) {
		super(modelLocation);
		this.overlayData = new WorkableOverlayModelData(overlayModelLocation);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void renderMachine(
			final List<BakedQuad> quads, final MachineType<?> machineType, @Nullable final MachineBlockEntity<?> machine, final Direction front, @Nullable final Direction side,
			final RandomSource rand, @Nullable final Direction modelFacing, final ModelState modelState
	) {
		super.renderMachine(quads, machineType, machine, front, side, rand, modelFacing, modelState);
		if (machine instanceof final IWorkable workable) {
			quads.addAll(this.overlayData.getQuads(side, front, workable.isWorking(), workable.canWork()));
		} else {
			quads.addAll(this.overlayData.getQuads(side, front, false, false));
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onPrepareTextureAtlas(final ResourceLocation atlasName, final Consumer<ResourceLocation> register) {
		super.onPrepareTextureAtlas(atlasName, register);
		if (atlasName.equals(InventoryMenu.BLOCK_ATLAS)) {
			this.overlayData.registerTextures(register);
		}
	}
}
