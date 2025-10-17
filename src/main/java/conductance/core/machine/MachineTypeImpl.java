package conductance.core.machine;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeModifier;
import conductance.api.recipe.MachineRecipeType;

@RequiredArgsConstructor
class MachineTypeImpl<T extends MachineBlockEntity<T>> implements MachineType<T> {

	private final @Getter String descriptionId;
	private final @Getter MutableComponent name;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineBlock<T>> block;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<MachineBlockItem<T>> item;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private Supplier<BlockEntityType<T>> blockEntityType;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private MachineRecipeType[] recipeTypes;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private @Nullable MachineRecipeModifier recipeModifier;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private @Nullable GuiSetup guiSetup;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private BlockRotationType rotationType;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private @Nullable Supplier<List<Component>> tooltipFactory;
}
