package conductance.core.machine;

import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.multi.MultiBlockStructure;
import conductance.api.machine.multi.MultiMachineType;

final class MultiMachineTypeImpl<T extends MachineBlockEntity<T>> extends MachineTypeImpl<T> implements MultiMachineType<T> {

	@Getter
	@Setter(AccessLevel.PACKAGE)
	private MultiBlockStructure structure;
	@Getter
	@Setter(AccessLevel.PACKAGE)
	private @Nullable Supplier<BlockState> casingAppearance;

	MultiMachineTypeImpl(final String descriptionId, final MutableComponent name) {
		super(descriptionId, name);
	}
}
