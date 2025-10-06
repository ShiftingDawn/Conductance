package conductance.api.machine.multi;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public interface MultiBlockStructureBuilder {

	MultiBlockStructureBuilder slice(String... slice);

	MultiBlockStructureBuilder key(char key, StructurePredicate predicate, @Nullable StructureCheckCallback callback);

	default MultiBlockStructureBuilder key(final char key, final StructurePredicate predicate) {
		return this.key(key, predicate, null);
	}

	default MultiBlockStructureBuilder key(final char key, final BlockState expectedState, @Nullable final StructureCheckCallback callback) {
		return this.key(key, StructurePredicate.isState(expectedState), callback);
	}

	default MultiBlockStructureBuilder key(final char key, final BlockState expectedState) {
		return this.key(key, StructurePredicate.isState(expectedState));
	}

	default MultiBlockStructureBuilder key(final char key, final Block expectedBlock, @Nullable final StructureCheckCallback callback) {
		return this.key(key, StructurePredicate.isBlock(expectedBlock), callback);
	}

	default MultiBlockStructureBuilder key(final char key, final Block expectedBlock) {
		return this.key(key, StructurePredicate.isBlock(expectedBlock));
	}

	default MultiBlockStructureBuilder key(final char key, final TagKey<Block> expectedTag, @Nullable final StructureCheckCallback callback) {
		return this.key(key, StructurePredicate.isTag(expectedTag), callback);
	}

	default MultiBlockStructureBuilder key(final char key, final TagKey<Block> expectedTag) {
		return this.key(key, StructurePredicate.isTag(expectedTag));
	}

	MultiBlockStructureBuilder callback(StructureCheckCallback callback);
}
