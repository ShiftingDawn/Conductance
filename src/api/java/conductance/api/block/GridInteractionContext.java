package conductance.api.block;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public class GridInteractionContext extends UseOnContext {

	private final BlockState blockState;
	private final @Nullable BlockEntity blockEntity;
	private final @Nullable InteractType interactType;

	public GridInteractionContext(final Player player, final InteractionHand hand, final BlockHitResult hitResult) {
		super(player, hand, hitResult);
		this.blockState = this.getLevel().getBlockState(this.getClickedPos());
		this.blockEntity = this.getLevel().getBlockEntity(this.getClickedPos());
		this.interactType = InteractType.findTypeForStack(this.getItemInHand());
	}

	public GridInteractionContext(final Level level, @Nullable final Player player, final InteractionHand hand, final ItemStack itemStack, final BlockHitResult hitResult) {
		super(level, player, hand, itemStack, hitResult);
		this.blockState = this.getLevel().getBlockState(this.getClickedPos());
		this.blockEntity = this.getLevel().getBlockEntity(this.getClickedPos());
		this.interactType = InteractType.findTypeForStack(this.getItemInHand());
	}
}
