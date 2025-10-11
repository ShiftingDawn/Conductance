package conductance.api.machine.multi;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.RecipeHandler;
import conductance.api.machine.RecipeHandlerStatus;
import conductance.api.util.TextHelper;

public interface IMultiBlockController<T extends MultiMachineBlockEntity<T>> {

	Level getLevel();

	MultiMachineType<T> getMachineType();

	MultiBlockStructure getStructure();

	MultiBlockInfo getMultiBlockInfo();

	boolean isStructureFormed();

	void onStructureFormed(StructureCheckContext ctx);

	void onStructureInvalid(StructureCheckContext ctx);

	Lock getStructureCheckLock();

	/**
	 * Check the multiblock structure. This function can be called from an async thread and is unsafe to use.
	 * <br>
	 * Use {@link IMultiBlockController#checkStructureAsyncLocked(StructureCheckContext)} or
	 * {@link IMultiBlockController#checkStructureAsyncLockedBlocking(StructureCheckContext)} instead
	 *
	 * @param ctx the shared context to store information in
	 * @return <code>true if the structure is valid</code> or <code>false</code> if it's not
	 */
	@Deprecated
	default boolean checkStructure(final StructureCheckContext ctx) {
		final MultiBlockStructure structure = this.getStructure();
		final MultiBlockInfo info = this.getMultiBlockInfo();
		return StructureHelper.checkStructure(info.level(), info.controllerPos(), info.facing(), structure, ctx);
	}

	default boolean checkStructureAsyncLocked(final StructureCheckContext ctx) {
		final Lock lock = this.getStructureCheckLock();
		if (lock.tryLock()) {
			try {
				return this.checkStructure(ctx);
			} finally {
				lock.unlock();
			}
		} else {
			return false;
		}
	}

	default boolean checkStructureAsyncLockedBlocking(final StructureCheckContext ctx) {
		final Lock lock = this.getStructureCheckLock();
		lock.lock();
		try {
			return this.checkStructure(ctx);
		} finally {
			lock.unlock();
		}
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
					list.add(Component.translatable("guiWidget.conductance.multiblock.recipe.progress",
						TextHelper.getFormattedTicks(handler.getProgressCurrent()), TextHelper.getFormattedTicks(handler.getProgressMax())
					));
				}
			});
		}
	}
}
