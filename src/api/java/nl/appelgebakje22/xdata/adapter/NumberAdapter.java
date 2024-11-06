package nl.appelgebakje22.xdata.adapter;

public interface NumberAdapter<TYPE extends Number> extends BaseAdapter {

	TYPE getNumber();

	default byte getByte() {
		final Number num = this.getNumber();
		return num != null ? num.byteValue() : 0;
	}

	default short getShort() {
		final Number num = this.getNumber();
		return num != null ? num.shortValue() : 0;
	}

	default int getInt() {
		final Number num = this.getNumber();
		return num != null ? num.intValue() : 0;
	}

	default long getLong() {
		final Number num = this.getNumber();
		return num != null ? num.longValue() : 0;
	}

	default float getFloat() {
		final Number num = this.getNumber();
		return num != null ? num.floatValue() : 0;
	}

	default double getDouble() {
		final Number num = this.getNumber();
		return num != null ? num.doubleValue() : 0;
	}
}
