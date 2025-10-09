package conductance.api.machine.multi;

import java.util.Set;

public interface IMultiBlockController<T extends MultiMachineBlockEntity<T>> {

	MultiMachineType<T> getMachineType();

	MultiBlockStructure getStructure();

	MultiBlockInfo getMultiBlockInfo();

	boolean isStructureFormed();

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

	Set<IMultiBlockPart> getParts();
}
