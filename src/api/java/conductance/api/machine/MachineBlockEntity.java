package conductance.api.machine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.api.IControllable;
import conductance.api.machine.api.IEventListener;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.api.IPlacerAware;
import conductance.api.machine.api.IRequesterBlockEntity;
import conductance.api.machine.api.IWorkable;

public class MachineBlockEntity<T extends MachineBlockEntity<T>> extends BaseBlockEntity implements IEventListener, IMachineCapabilityHolder, IPlacerAware, IRequesterBlockEntity, IControllable {


	private final @Getter Map<String, MachineCapability> capabilities = new LinkedHashMap<>();
	private final @Getter MachineType<T> machineType;
	@Setter
	@Getter
	private @Nullable UUID placer;
	private @Getter boolean processingAllowed = true;

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
	protected void saveAdditional(final ValueOutput output) {
		super.saveAdditional(output);
		output.putBoolean("processing_allowed", this.processingAllowed);
	}

	@Override
	protected void loadAdditional(final ValueInput input) {
		super.loadAdditional(input);
		this.processingAllowed = input.getBooleanOr("processing_allowed", this.processingAllowed);
	}

	@Override
	public void setProcessingAllowed(final boolean allowed) {
		if (allowed != this.processingAllowed) {
			if (!allowed && this instanceof final IWorkable workable) {
				workable.setWorking(false);
			}
			this.processingAllowed = allowed;
			this.setChanged();
		}
	}
}
