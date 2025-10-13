package conductance.init.block;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import lombok.Getter;
import conductance.api.block.IGeneratedMiningTags;
import conductance.api.tier.Tier;
import conductance.api.tier.TieredItemType;

public final class TieredBlock extends Block implements IGeneratedMiningTags {

	private final @Getter TieredItemType type;
	private final @Getter Tier tier;
	private final MutableComponent name;

	public TieredBlock(final Properties props, final TieredItemType type, final Tier tier) {
		super(props);
		this.type = type;
		this.tier = tier;
		this.name = Component.translatable(type.getDescriptionId(), tier.getName());
	}

	@Override
	public List<TagKey<Block>> getRequiredToolTypeTag() {
		return List.of(BlockTags.MINEABLE_WITH_PICKAXE);
	}

	@Override
	public List<TagKey<Block>> getRequiredToolLevelTag() {
		return List.of(BlockTags.NEEDS_STONE_TOOL);
	}

	@Override
	public MutableComponent getName() {
		return this.name;
	}
}
