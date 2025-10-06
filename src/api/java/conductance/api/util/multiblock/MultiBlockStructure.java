package conductance.api.util.multiblock;

public record MultiBlockStructure(StructurePredicate[][][] expectedStates, int xOffset, int yOffset, int zOffset) {
}
