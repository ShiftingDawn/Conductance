package conductance.init.item;

import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import conductance.api.NCDataComponents;
import conductance.api.NCItems;

public final class ProgramCircuitItem extends Item {

	private static final Function<Integer, Component> COMPONENT_FACTORY =
		Util.memoize(program -> Component.translatable("item.conductance.program_circuit.with_program", Component.literal(String.valueOf(program)).withStyle(ChatFormatting.YELLOW)));

	public ProgramCircuitItem(final Properties properties) {
		super(properties.component(NCDataComponents.PROGRAM_CIRCUIT, 0));
	}

	@Override
	public Component getName(final ItemStack stack) {
		final int circuit = stack.getOrDefault(NCDataComponents.PROGRAM_CIRCUIT, -1);
		if (circuit != -1) {
			return ProgramCircuitItem.COMPONENT_FACTORY.apply(circuit);
		}
		return super.getName(stack);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if (player.isCrouching()) {
			player.openMenu(new MenuProvider() {
				@Override
				public Component getDisplayName() {
					return ProgramCircuitItem.this.getName();
				}

				@Override
				public AbstractContainerMenu createMenu(final int containerId, final Inventory playerInventory, final Player player) {
					final DataSlot slot = new DataSlot() {
						@Override
						public int get() {
							return player.getItemInHand(hand).getOrDefault(NCDataComponents.PROGRAM_CIRCUIT, -1);
						}

						@Override
						public void set(final int value) {
							player.getItemInHand(hand).set(NCDataComponents.PROGRAM_CIRCUIT, Mth.clamp(0, value, 24));
							player.getInventory().setChanged();
						}
					};
					return new ProgramCircuitMenu(containerId, player, hand, slot);
				}
			}, buf -> buf.writeEnum(hand));
			return InteractionResult.SUCCESS_SERVER;
		}
		return super.use(level, player, hand);
	}

	public static ItemStack makeStack(final int program) {
		return ProgramCircuitItem.makeStack(NCItems.PROGRAM_CIRCUIT.value(), program);
	}

	public static ItemStack makeStack(final Item item, final int program) {
		return Util.make(new ItemStack(item), stack -> stack.set(NCDataComponents.PROGRAM_CIRCUIT, program));
	}
}
