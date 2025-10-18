package conductance.api.machine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.api.IMachineCapabilityHolder;
import conductance.api.machine.gui.IGuiWidget;
import conductance.api.machine.gui.MachineMenu;

public abstract class MachineCapability implements ValueIOSerializable {

	private final List<Runnable> changeListeners = new ArrayList<>();
	private final @Getter BaseBlockEntity owner;
	private final @Getter IMachineCapabilityHolder capabilityHolder;
	private @Setter Predicate<@Nullable Direction> capabilityValidator;
	private boolean hasChanged = false;

	protected MachineCapability(final String key, final BaseBlockEntity owner) {
		if (!(owner instanceof final IMachineCapabilityHolder capHolder)) {
			throw new IllegalArgumentException("Owner must implement " + IMachineCapabilityHolder.class.getName());
		}
		this.owner = owner;
		this.capabilityHolder = capHolder;
		this.capabilityValidator = side -> true;
		capHolder.registerCapability(key, this);
		this.changeListeners.add(owner::setChanged);
	}

	public final boolean isValid(@Nullable final Direction side) {
		return side == null || this.capabilityValidator.test(side);
	}

	public final ISubscription addChangedListener(final Runnable listener) {
		this.changeListeners.add(listener);
		return () -> this.changeListeners.remove(listener);
	}

	private void notifyListeners() {
		this.changeListeners.forEach(Runnable::run);
	}

	public final void setChanged() {
		this.hasChanged = true;
		this.notifyListeners();
	}

	public final boolean hasChanged() {
		return this.hasChanged;
	}

	public final void clearChanged() {
		this.hasChanged = false;
	}

	public void onLoad() {
	}

	public void onUnload() {
	}

	public void addGuiControls(final MachineMenu menu, final BiConsumer<String, IGuiWidget> adder) {
	}
}
