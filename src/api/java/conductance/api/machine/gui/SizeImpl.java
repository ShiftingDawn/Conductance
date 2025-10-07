package conductance.api.machine.gui;

record SizeImpl(int width, int height) implements Size {

	@Override
	public String toString() {
		return "Size[" + this.width() + "," + this.height() + "]";
	}
}
