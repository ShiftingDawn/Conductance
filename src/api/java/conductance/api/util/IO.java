package conductance.api.util;

import java.util.Locale;

public enum IO {
	IN, OUT;

	private final String name = super.toString().toLowerCase(Locale.ROOT);

	@Override
	public String toString() {
		return this.name;
	}
}
