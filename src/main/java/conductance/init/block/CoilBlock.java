package conductance.init.block;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import lombok.Getter;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.coil.CoilBlockType;

public final class CoilBlock extends Block implements IGeneratedMiningTags {

	private final @Getter CoilBlockType type;
	private final MutableComponent name;

	public CoilBlock(final Properties props, final CoilBlockType type) {
		super(props);
		this.type = type;
		this.name = Component.translatable("block.conductance.coil_block", type.getName());
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		return List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(BlockTags.NEEDS_IRON_TOOL);
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}
}
