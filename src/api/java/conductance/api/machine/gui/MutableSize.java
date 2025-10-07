package conductance.api.machine.gui;

import conductance.api.CAPI;

public interface MutableSize extends Size {

	@Override
	default int width() {
		return this.holderWidth().getAsInt();
	}

	default int width(final int newWidth) {
		final int oldWidth = this.width();
		this.holderWidth().accept(newWidth);
		return oldWidth;
	}

	@Override
	default int height() {
		return this.holderHeight().getAsInt();
	}

	default int height(final int newHeight) {
		final int oldHeight = this.height();
		this.holderHeight().accept(newHeight);
		return oldHeight;
	}

	ManagedInt holderWidth();

	void holderWidth(ManagedInt newHolder);

	ManagedInt holderHeight();

	void holderHeight(ManagedInt newHolder);

	static MutableSize of(final Size size) {
		return size instanceof final MutableSize mut ? mut : MutableSize.of(size.width(), size.height());
	}

	static MutableSize of(final ManagedInt width, final ManagedInt height) {
		return new MutableSizeImpl(width, height);
	}

	static MutableSize of(final int width, final int height) {
		final IntHolder holderWidth = CAPI.make(new IntHolder(), holder -> holder.setValue(width));
		final IntHolder holderHeight = CAPI.make(new IntHolder(), holder -> holder.setValue(height));
		return MutableSize.of(new ManagedInt(holderWidth::setValue, holderWidth::getValue), new ManagedInt(holderHeight::setValue, holderHeight::getValue));
	}
}
