package conductance.api.machine;

public interface IPaintable {

	void setPaintColor(int paintColor);

	int getPaintColor();

	int getPaintColorDefault();

	default boolean isPainted() {
		return this.getPaintColor() != -1 && this.getPaintColor() != this.getPaintColorDefault();
	}

	default int getRealColor() {
		return this.isPainted() ? this.getPaintColor() : this.getPaintColorDefault();
	}
}
