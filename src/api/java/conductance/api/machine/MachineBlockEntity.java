package conductance.api.machine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.NCBlockStateProperties;
import conductance.api.machine.api.IEventListener;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.api.IPlacerAware;
import conductance.api.machine.api.IRequesterBlockEntity;
import conductance.api.machine.api.IWorkable;

public class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BaseBlockEntity implements IEventListener, IMachineCapabilityHolder, IPlacerAware, IRequesterBlockEntity, IWorkable {


	private final @Getter Map<String, MachineCapability> capabilities = new LinkedHashMap<>();
	private final @Getter MachineType<T> machineType;
	@Setter
	@Getter
	private @Nullable UUID placer;

	public MachineBlockEntity(final MachineType<T> type, final BlockPos pos, final BlockState blockState) {
		super(type.getBlockEntityType().get(), pos, blockState);
		this.machineType = type;
	}

	@Override
	public final void registerCapability(final String key, final MachineCapability capability) {
		if (this.capabilities.containsKey(key) || this.capabilities.containsValue(capability)) {
			return;
		}
		this.capabilities.put(key, capability);
	}

	@Override
	public void setWorking(final boolean working) {
		if (working != this.isWorking()) {
			this.onServer(level -> level.setBlockAndUpdate(this.getBlockPos(), this.getBlockState().setValue(NCBlockStateProperties.WORKING, working)));
		}
	}

	@Override
	public boolean isWorking() {
		return this.getBlockState().getValue(NCBlockStateProperties.WORKING);
	}
}
