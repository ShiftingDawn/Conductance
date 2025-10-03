package conductance.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.Nullable;

public interface IGridInteractable {

	/**
	 * Called when an interaction with the "block grid" happened
	 *
	 * @param interactType the tool that was used, or <code>null</code> if no tool was used
	 * @param ctx          the interaction context
	 * @param side         the side that was interaction with, according to the "block grid"
	 *                     <br><strong>NOTE: </strong>This is not the physical side that was interacted with.
	 *                     <br>The physical side can be obtained using the UseOnContext
	 * @return the interaction result, <code>PASS</code> to ignore the interaction
	 */
	InteractionResult onToolUsed(@Nullable InteractType interactType, UseOnContext ctx, Direction side);
}
