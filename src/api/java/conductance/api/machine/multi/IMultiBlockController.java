package conductance.api.machine.multi;

import java.util.List;
import java.util.Set;
import net.minecraft.network.chat.Component;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.RecipeHandlerStatus;
import conductance.api.util.TextHelper;

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

	default void addScreenInfo(final List<Component> list) {
		list.add(this.isStructureFormed()
			? Component.translatable("guiWidget.conductance.multiblock.structure.formed")
			: Component.translatable("guiWidget.conductance.multiblock.structure.invalid")
		);
		if (this instanceof final MachineBlockEntity<?> machine) {
			machine.getCapability(RecipeHandler.class).ifPresent(handler -> {
				list.add(switch (handler.getStatus()) {
					case IDLE -> Component.translatable("guiWidget.conductance.multiblock.recipe.idle");
					case PAUSED -> Component.translatable("guiWidget.conductance.multiblock.recipe.paused");
					case PROCESSING -> Component.translatable("guiWidget.conductance.multiblock.recipe.processing");
				});
				if (handler.getStatus() == RecipeHandlerStatus.PROCESSING) {
					final double current = handler.getProgressCurrent() / 20.0;
					list.add(Component.translatable("guiWidget.conductance.multiblock.recipe.progress",
						TextHelper.getFormattedTicks(handler.getProgressCurrent()), TextHelper.getFormattedTicks(handler.getProgressMax())
					));
				}
			});
		}
	}
}
