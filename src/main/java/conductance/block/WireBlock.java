package conductance.block;

import java.util.List;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.Lazy;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.NCMaterialTraits;
import conductance.api.capability.CapabilityHelper;
import conductance.api.machine.IPaintable;
import conductance.api.material.Material;
import conductance.api.material.traits.MaterialTraitWire;
import conductance.api.util.TextHelper;
import conductance.Conductance;
import conductance.core.pipenet.WireData;
import conductance.core.pipenet.WireType;
import conductance.core.pipenet.EnergyNet;
import conductance.core.pipenet.IWireNode;
import conductance.core.pipenet.LevelEnergyNet;
import conductance.core.pipenet.PipeBlockRenderer;
import conductance.core.pipenet.PipeModel;
import conductance.init.ConductanceBlockEntities;

public final class WireBlock extends PipeBlock<IWireNode, WireData, LevelEnergyNet> {

	@Getter
	private final Material material;
	@Getter
	private final WireType wireType;
	private final Lazy<PipeModel> pipeModel;
	private final PipeBlockRenderer pipeRenderer;
	private final String unlocalizedName;

	public WireBlock(final Properties properties, final WireType wireType, final Material material) {
		super(properties, EnergyNet.TYPE);
		this.wireType = wireType;
		this.material = material;
		this.unlocalizedName = "block.%s.%s".formatted(material.getRegistryKey().getNamespace(), wireType.getMaterialTaggedSet().getUnlocalizedName(material));
		this.pipeModel = Lazy.of(() -> wireType.createPipeModel(material));
		this.pipeRenderer = new PipeBlockRenderer(this.pipeModel, this);
	}

	@Override
	public String getDescriptionId() {
		return this.unlocalizedName;
	}

	@Override
	public MutableComponent getName() {
		return CAPI.translations().makeLocalizedName(this.getDescriptionId(), this.wireType.getMaterialTaggedSet(), this.material);
	}

	public WireData getBaseProps() {
		final MaterialTraitWire trait = this.material.getTrait(NCMaterialTraits.WIRE);
		assert trait != null;
		return new WireData(trait.getTier().getVoltage(), trait.getAmperage());
	}

	public WireData getRealProps() {
		return this.wireType.getPhysicalProperties(this.getBaseProps());
	}

	@Override
	public BlockEntityType<? extends PipeBlockEntity<IWireNode, WireData, LevelEnergyNet>> getBlockEntityType() {
		return ConductanceBlockEntities.WIRE.get();
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
			if (blockEntity instanceof final WireBlockEntity wireBlockEntity) {
				return wireBlockEntity.getEnergyHandler(direction);
			}
			return null;
		}, this);
	}

	public int getColorTint(final BlockState state, @Nullable final BlockAndTintGetter level, @Nullable final BlockPos pos, final int index) {
		return index == 0 || index == 1 ? this.material.getMaterialColorRGB() : -1;
	}

	@Override
	public void appendHoverText(final ItemStack stack, final Item.TooltipContext context, final List<Component> tooltip, final TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
		final WireData wireData = this.getRealProps();
		tooltip.add(Conductance.tooltip("wire.voltage", wireData.voltage(), TextHelper.ENERGY_FORMAT, wireData.getTier().getLocalizedName()));
		tooltip.add(Conductance.tooltip("wire.amperage", wireData.amperage()));
	}

	@OnlyIn(Dist.CLIENT)
	public static BlockColor handleColorTint() {
		return (blockState, level, blockPos, index) -> {
			if (blockState.getBlock() instanceof final WireBlock block) {
				if (blockPos != null && level.getBlockEntity(blockPos) instanceof final IPaintable paintable && paintable.isPainted()) {
					return paintable.getRealColor();
				}
				return block.getColorTint(blockState, level, blockPos, index);
			}
			return -1;
		};
	}
}
