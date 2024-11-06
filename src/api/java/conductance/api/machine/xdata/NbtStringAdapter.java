package conductance.api.machine.xdata;

import nl.appelgebakje22.xdata.adapter.BaseStringAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;

class NbtStringAdapter extends BaseStringAdapter implements CharAdapter {

	@Override
	public void setChar(final char value) {
		this.setString(String.valueOf(value));
	}

	@Override
	public char getChar() {
		final String s = this.getString();
		return !s.isEmpty() ? s.charAt(0) : 0;
	}

	@Override
	public char getChar(final char fallback) {
		final char result = this.getChar();
		return result != 0 ? result : fallback;
	}
}
