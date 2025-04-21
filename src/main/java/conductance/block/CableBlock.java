package conductance.block;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.capability.CapabilityHelper;
import conductance.api.machine.IPaintable;
import conductance.api.material.Material;
import conductance.core.pipenet.CableData;
import conductance.core.pipenet.CableType;
import conductance.core.pipenet.EnergyNet;
import conductance.core.pipenet.LevelEnergyNet;
import conductance.core.pipenet.PipeBlockRenderer;
import conductance.core.pipenet.PipeModel;
import conductance.init.ConductanceBlockEntities;

public final class CableBlock extends PipeBlock<CableData, LevelEnergyNet> {

	@Getter
	private final Material material;
	@Getter
	private final CableType cableType;
	private final Lazy<PipeModel> pipeModel;
	private final PipeBlockRenderer pipeRenderer;

	public CableBlock(final Properties properties, final CableType cableType, final Material material) {
		super(properties, EnergyNet.TYPE);
		this.cableType = cableType;
		this.material = material;
		this.pipeModel = Lazy.of(() -> cableType.createPipeModel(material));
		this.pipeRenderer = new PipeBlockRenderer(this.pipeModel, this);
	}

	@Override
	public BlockEntityType<? extends PipeBlockEntity<CableData, LevelEnergyNet>> getBlockEntityType() {
		return ConductanceBlockEntities.CABLE.get();
	}

	@Override
	public PipeBlockRenderer getRenderer(final BlockState state) {
		return this.pipeRenderer;
	}

	@Override
	protected PipeModel getPipeModel() {
		return this.pipeModel.get();
	}

	public void attachCapabilities(final RegisterCapabilitiesEvent event) {
		event.registerBlock(CapabilityHelper.ENERGY_HANDLER_BLOCK, (level, blockPos, blockState, blockEntity, direction) -> {
			if (blockEntity instanceof final CableBlockEntity cableBlockEntity) {
				return cableBlockEntity.getEnergyHandler(direction);
			}
			return null;
		}, this);
	}

	public int getColorTint(final BlockState state, @Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, final int index) {
		if (this.cableType.isCable() && index == 0) {
			return 0x404040;
		}
		return index == 0 || index == 1 ? this.material.getMaterialColorRGB() : -1;
	}

	@OnlyIn(Dist.CLIENT)
	public static BlockColor handleColorTint() {
		return (blockState, level, blockPos, index) -> {
			if (blockState.getBlock() instanceof final CableBlock block) {
				if (blockPos != null && level.getBlockEntity(blockPos) instanceof final IPaintable paintable && paintable.isPainted()) {
					return paintable.getRealColor();
				}
				return block.getColorTint(blockState, level, blockPos, index);
			}
			return -1;
		};
	}
}
