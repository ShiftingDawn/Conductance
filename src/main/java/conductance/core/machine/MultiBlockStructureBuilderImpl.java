package conductance.core.machine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.Block;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.multi.MultiBlockStructure;
import conductance.api.machine.multi.MultiBlockStructureBuilder;
import conductance.api.machine.multi.StructureCheckCallback;
import conductance.api.machine.multi.StructurePredicate;

public final class MultiBlockStructureBuilderImpl implements MultiBlockStructureBuilder {

	private final List<String[]> pattern = new LinkedList<>();
	private final Char2ObjectMap<StructurePredicate> mapping = new Char2ObjectArrayMap<>();
	private final List<StructureCheckCallback> globalCallbacks = new ArrayList<>();
	private final Char2ObjectMap<StructureCheckCallback> mappedCallbacks = new Char2ObjectArrayMap<>();
	private final char controller;
	private int sliceLengthZ = -1;
	private int lineLengthX = -1;

	public MultiBlockStructureBuilderImpl(final char controllerKey, final Supplier<Block> controller) {
		this.mapping.put(' ', StructurePredicate.isAir());
		this.mapping.put(controllerKey, StructurePredicate.isBlock(controller));
		this.controller = controllerKey;
	}

	@Override
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

	@Override
	public MultiBlockStructureBuilder key(final char key, final StructurePredicate predicate, @Nullable final StructureCheckCallback callback) {
		if (this.mapping.containsKey(key)) {
			throw new IllegalStateException("Duplicate key " + key);
		}
		this.mapping.put(key, predicate);
		if (callback != null) {
			this.mappedCallbacks.put(key, callback);
		}
		return this;
	}

	@Override
	public MultiBlockStructureBuilder callback(final StructureCheckCallback callback) {
		this.globalCallbacks.add(callback);
		return this;
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

	private Tuple<StructurePredicate[][][], @Nullable StructureCheckCallback[][][]> validateAndBuild() {
		final StructurePredicate[][][] predicateArray = new StructurePredicate[this.lineLengthX][][];
		final @Nullable StructureCheckCallback[][][] callbackArray = new StructureCheckCallback[this.lineLengthX][][];
		for (int y = 0; y < this.pattern.size(); ++y) {
			//Pattern is written top-to-bottom but checked bottom-to-top
			final int realY = this.pattern.size() - 1 - y;
			final String[] slice = this.pattern.get(y);
			for (int z = 0; z < slice.length; ++z) {
				final String line = slice[z];
				for (int x = 0; x < line.length(); ++x) {
					if (predicateArray[x] == null) {
						predicateArray[x] = new StructurePredicate[this.pattern.size()][];
						callbackArray[x] = new StructureCheckCallback[this.pattern.size()][];
					}
					if (predicateArray[x][realY] == null) {
						predicateArray[x][realY] = new StructurePredicate[this.sliceLengthZ];
						callbackArray[x][realY] = new StructureCheckCallback[this.sliceLengthZ];
					}
					predicateArray[x][realY][z] = this.mapping.get(line.charAt(x));
					callbackArray[x][realY][z] = this.mappedCallbacks.get(line.charAt(x));
				}
			}
		}
		return new Tuple<>(predicateArray, callbackArray);
	}

	public MultiBlockStructure build() {
		if (this.pattern.isEmpty()) {
			throw new IllegalStateException("No pattern was registered");
		}
		final int[] controllerOffset = this.findControllerOffset();
		if (controllerOffset.length == 0) {
			throw new IllegalStateException("Controller was not found in structure");
		}
		final Tuple<StructurePredicate[][][], StructureCheckCallback[][][]> patternAndCallbacks = this.validateAndBuild();
		return new MultiBlockStructure(
			patternAndCallbacks.getA(),
			controllerOffset[0], controllerOffset[1], controllerOffset[2],
			patternAndCallbacks.getB(),
			Collections.unmodifiableList(this.globalCallbacks),
			Set.copyOf(this.mapping.values())
		);
	}
}
