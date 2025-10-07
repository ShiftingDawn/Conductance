package conductance.api.machine.gui;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class MutableSizeImpl implements MutableSize {

	private ManagedInt width;
	private ManagedInt height;

	@Override
	public ManagedInt holderWidth() {
		return this.width;
	}

	@Override
	public void holderWidth(final ManagedInt newHolder) {
		this.width = newHolder;
	}

	@Override
	public ManagedInt holderHeight() {
		return this.height;
	}

	@Override
	public void holderHeight(final ManagedInt newHolder) {
		this.height = newHolder;
	}

	@Override
	public String toString() {
		return "MutableSize[" + this.width() + "," + this.height() + "]";
	}
}
