package conductance.api.util.multiblock;

import java.util.LinkedList;
import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;

public final class MultiBlockStructureBuilder {

	private final List<String[]> pattern = new LinkedList<>(); // y([z][x])
	private final Char2ObjectMap<StructurePredicate> mapping = new Char2ObjectArrayMap<>();
	private final char controller;
	private int sliceLengthZ = -1;
	private int lineLengthX = -1;

	public MultiBlockStructureBuilder(final char controllerKey, final Block controller) {
		this.mapping.put(' ', StructurePredicate.isAir());
		this.mapping.put(controllerKey, StructurePredicate.isBlock(controller));
		this.controller = controllerKey;
	}

	public MultiBlockStructureBuilder slice(final String... slice) {
		if (slice.length == 0) {
			throw new IllegalStateException("Slice requires at least 1 line");
		}
		if (this.sliceLengthZ == -1) {
			this.sliceLengthZ = slice.length;
		} else if (slice.length != this.sliceLengthZ) {
			throw new IllegalStateException("Slice has invalid length of %s. Expected %s (set by first slice)".formatted(slice.length, this.sliceLengthZ));
		}
		for (int z = 0; z < slice.length; ++z) {
			final String line = slice[z];
			if (this.lineLengthX == -1) {
				this.lineLengthX = line.length();
			} else if (this.lineLengthX != line.length()) {
				throw new IllegalStateException("Line %s has invalid length of %s. Expected %s (set by first line of first slice)".formatted(z, line.length(), this.lineLengthX));
			}
		}
		this.pattern.addLast(slice);
		return this;
	}

	public MultiBlockStructureBuilder key(final char key, final StructurePredicate predicate) {
		if (this.mapping.containsKey(key)) {
			throw new IllegalStateException("Duplicate key " + key);
		}
		this.mapping.put(key, predicate);
		return this;
	}

	public MultiBlockStructureBuilder key(final char key, final BlockState expectedState) {
		return this.key(key, StructurePredicate.isState(expectedState));
	}

	public MultiBlockStructureBuilder key(final char key, final Block expectedBlock) {
		return this.key(key, StructurePredicate.isBlock(expectedBlock));
	}

	public MultiBlockStructureBuilder key(final char key, final TagKey<Block> expectedTag) {
		return this.key(key, StructurePredicate.isTag(expectedTag));
	}

	private int[] findControllerOffset() {
		for (int y = 0; y < this.pattern.size(); ++y) {
			final String[] slice = this.pattern.get(y);
			for (int z = 0; z < slice.length; ++z) {
				final String line = slice[z];
				for (int x = 0; x < line.length(); ++x) {
					if (line.charAt(x) == this.controller) {
						return new int[] {x, y, z};
					}
				}
			}
		}
		return new int[0];
	}

	private StructurePredicate[][][] validateAndBuild() {
		final StructurePredicate[][][] result = new StructurePredicate[this.lineLengthX][][];
		for (int y = 0; y < this.pattern.size(); ++y) {
			//Pattern is written top-to-bottom but checked bottom-to-top
			final int realY = this.pattern.size() - 1 - y;
			final String[] slice = this.pattern.get(y);
			for (int z = 0; z < slice.length; ++z) {
				final String line = slice[z];
				for (int x = 0; x < line.length(); ++x) {
					if (result[x] == null) {
						result[x] = new StructurePredicate[this.pattern.size()][];
					}
					if (result[x][y] == null) {
						result[x][realY] = new StructurePredicate[this.sliceLengthZ];
					}
					result[x][realY][z] = this.mapping.get(line.charAt(x));
				}
			}
		}
		return result;
	}

	public MultiBlockStructure build() {
		if (this.pattern.isEmpty()) {
			throw new IllegalStateException("No pattern was registered");
		}
		final int[] controllerOffset = this.findControllerOffset();
		if (controllerOffset.length == 0) {
			throw new IllegalStateException("Controller was not found in structure");
		}
		final StructurePredicate[][][] statePattern = this.validateAndBuild();

		return new MultiBlockStructure(statePattern, controllerOffset[0], controllerOffset[1], controllerOffset[2]);
	}
}
