package conductance.api.machine.multi;

public interface IMultiBlockController {

	MultiBlockStructure getStructure();

	MultiBlockInfo getMultiBlockInfo();

	void onStructureFormed(StructureCheckContext ctx);

	void onStructureInvalid(StructureCheckContext ctx);

	default boolean checkStructure(final StructureCheckContext ctx) {
		final MultiBlockStructure structure = this.getStructure();
		final MultiBlockInfo info = this.getMultiBlockInfo();
		return StructureHelper.checkStructure(info.level(), info.controllerPos(), info.facing(), structure, ctx);
	}

	boolean hasPart(IMultiBlockPart part);

	void addPart(IMultiBlockPart part);

	void removePart(IMultiBlockPart part);
}
