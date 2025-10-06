package conductance.api.machine.multi;

public interface IMultiBlockController {

	MultiBlockStructure getStructure();

	MultiBlockInfo getMultiBlockInfo();

	default boolean checkStructure() {
		final MultiBlockStructure structure = this.getStructure();
		final MultiBlockInfo info = this.getMultiBlockInfo();
		return StructureHelper.checkStructure(info.level(), info.controllerPos(), info.facing(), structure, new StructureCheckContext());
	}

	boolean hasPart(IMultiBlockPart part);

	void addPart(IMultiBlockPart part);

	void removePart(IMultiBlockPart part);
}
