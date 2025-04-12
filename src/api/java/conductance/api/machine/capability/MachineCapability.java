package conductance.api.machine.capability;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import com.lowdragmc.lowdraglib.syncdata.IEnhancedManaged;
import com.lowdragmc.lowdraglib.syncdata.ISubscription;
import com.lowdragmc.lowdraglib.syncdata.field.FieldManagedStorage;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import conductance.api.machine.MachineBlockEntity;

public abstract class MachineCapability implements IEnhancedManaged {

	protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(MachineCapability.class);
	private final FieldManagedStorage syncStorage = new FieldManagedStorage(this);
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
	public void onChanged() {
		this.machineBlockEntity.onChanged();
	}

	@Override
	public void scheduleRenderUpdate() {
		this.machineBlockEntity.scheduleRenderUpdate();
	}

	@Override
	public FieldManagedStorage getSyncStorage() {
		return this.syncStorage;
	}

	@Override
	public ManagedFieldHolder getFieldHolder() {
		return MachineCapability.MANAGED_FIELD_HOLDER;
	}
}
