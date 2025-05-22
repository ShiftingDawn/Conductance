package conductance.api.machine.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import lombok.Getter;
import lombok.Setter;
import conductance.api.CAPI;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.sync.IManaged;
import conductance.api.machine.sync.ManagedDataMap;

public abstract class MachineCapability implements IManaged {

	private final ManagedDataMap dataMap = CAPI.syncHelper().requestDataMap(this);
	@Getter
	private final MachineBlockEntity<?> machineBlockEntity;
	@Setter
	protected Predicate<Direction> capabilityValidator;
	private final List<Runnable> listeners = new ArrayList<>();

	protected MachineCapability(final MachineBlockEntity<?> machineBlockEntity) {
		this.machineBlockEntity = machineBlockEntity;
		this.capabilityValidator = side -> true;
		machineBlockEntity.registerCapability(this);
	}

	public final boolean hasCapability(@Nullable final Direction side) {
		return this.capabilityValidator.test(side);
	}

	//region Events
	public final ISubscription addChangedListener(final Runnable listener) {
		this.listeners.add(listener);
		return () -> this.listeners.remove(listener);
	}

	public final void notifyListeners() {
		this.listeners.forEach(Runnable::run);
	}

	public void onLoad() {
	}

	public void onUnload() {
	}
	//endregion

	@Override
	public ManagedDataMap getDataMap() {
		return this.dataMap;
	}
}
