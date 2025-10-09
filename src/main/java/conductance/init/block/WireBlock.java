package conductance.init.block;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import lombok.Getter;
import conductance.api.NCMaterialProps;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.material.Material;
import conductance.api.material.MaterialGenerationHandler;
import conductance.core.block.IMaterialBlock;
import conductance.lib.pipenet.WireType;

public final class WireBlock extends Block implements IMaterialBlock, IGeneratedMiningTags {

	public static final Map<Direction, BooleanProperty> CONNECTION_PROPS;
	private final @Getter Material material;
	private final @Getter WireType wireType;
	private final MutableComponent name;

	public WireBlock(final Properties props, final Material material, final WireType wireType) {
		super(props.noOcclusion().dynamicShape());
		this.material = material;
		this.wireType = wireType;
		this.name = Component.translatable(this.getHandler().makeDescriptionId(material), Component.translatable(material.getDescriptionId()));
		this.registerDefaultState(Util.make(() -> {
			BlockState s = this.defaultBlockState();
			for (final Direction face : Direction.values()) {
				s = s.setValue(WireBlock.CONNECTION_PROPS.get(face), false);
			}
			return s;
		}));
	}

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(WireBlock.CONNECTION_PROPS.values().toArray(Property[]::new));
	}

	@Override
	public MaterialGenerationHandler getHandler() {
		return this.wireType.getHandler();
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		final List<TagKey<Block>> tags = this.getHandler().getMiningToolTypeTags();
		return !tags.isEmpty() ? tags : List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(this.material.getProp(NCMaterialProps.REQUIRED_TOOL_LEVEL, BlockTags.NEEDS_STONE_TOOL));
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}

	static {
		CONNECTION_PROPS = Util.make(new EnumMap<>(Direction.class), map -> {
			for (final Direction d : Direction.values()) {
				map.put(d, BooleanProperty.create(d.getName()));
			}
		});
	}
}
