package conductance.core.machine;

import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.MachineType;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.util.Lazy;

final class MachineTypeImpl<T extends MachineBlockEntity<T>> implements MachineType<T> {

	private final Lazy<String> descriptionId = Lazy.of(() -> Util.makeDescriptionId("machine", this.getId()));
	private final Lazy<Component> name = Lazy.of(() -> Component.translatable(this.descriptionId.get()));

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
	private @Nullable GuiSetup guiSetup;


	@Override
	public String getDescriptionId() {
		return this.descriptionId.get();
	}

	@Override
	public Component getName() {
		return this.name.get();
	}
}
