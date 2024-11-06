package nl.appelgebakje22.xdata.adapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import nl.appelgebakje22.xdata.XData;
import org.jetbrains.annotations.UnknownNullability;

public class BaseTableAdapter implements TableAdapter {

	private final Map<String, BaseAdapter> elements = new HashMap<>();
	private final AdapterFactory adapters;

	public BaseTableAdapter(final AdapterFactory adapters) {
		this.adapters = adapters;
	}

	@Override
	public void set(final String key, final boolean value) {
		this.set(key, this.adapters.ofBoolean(value));
	}

	@Override
	public void set(final String key, final byte value) {
		this.set(key, this.adapters.ofByte(value));
	}

	@Override
	public void set(final String key, final short value) {
		this.set(key, this.adapters.ofShort(value));
	}

	@Override
	public void set(final String key, final int value) {
		this.set(key, this.adapters.ofInt(value));
	}

	@Override
	public void set(final String key, final long value) {
		this.set(key, this.adapters.ofLong(value));
	}

	@Override
	public void set(final String key, final float value) {
		this.set(key, this.adapters.ofFloat(value));
	}

	@Override
	public void set(final String key, final double value) {
		this.set(key, this.adapters.ofDouble(value));
	}

	@Override
	public void set(final String key, final char value) {
		this.set(key, this.adapters.ofChar(value));
	}

	@Override
	public void set(final String key, final String value) {
		this.set(key, this.adapters.ofString(value));
	}

	@Override
	public void set(final String key, final BaseAdapter value) {
		if (value != null) {
			this.elements.put(this.checkKey(key), value);
		}
	}

	@Override
	public ArrayAdapter setArray(final String key) {
		return XData.make(this.adapters.array(), array -> this.set(key, array));
	}

	@Override
	public TableAdapter setTable(final String key) {
		return XData.make(this.adapters.table(), table -> this.set(key, table));
	}

	@Override
	public boolean has(final String key) {
		return this.elements.containsKey(this.checkKey(key));
	}

	@Override
	public boolean getBoolean(final String key, final boolean fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final BooleanAdapter booleanAdapter ? booleanAdapter.getBoolean() : fallback;
	}

	@Override
	public byte getByte(final String key, final byte fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getByte() : fallback;
	}

	@Override
	public short getShort(final String key, final short fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getShort() : fallback;
	}

	@Override
	public int getInt(final String key, final int fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getInt() : fallback;
	}

	@Override
	public long getLong(final String key, final long fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getLong() : fallback;
	}

	@Override
	public float getFloat(final String key, final float fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getFloat() : fallback;
	}

	@Override
	public double getDouble(final String key, final double fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getDouble() : fallback;
	}

	@Override
	public char getChar(final String key, final char fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final CharAdapter charAdapter ? charAdapter.getChar() : fallback;
	}

	@Override
	public @UnknownNullability String getString(final String key, final String fallback) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final StringAdapter stringAdapter ? stringAdapter.getString() : fallback;
	}

	@Override
	public @UnknownNullability ArrayAdapter getArray(final String key) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final ArrayAdapter arrayAdapter ? arrayAdapter : null;
	}

	@Override
	public @UnknownNullability TableAdapter getTable(final String key) {
		final BaseAdapter adapter = this.get(key);
		return adapter instanceof final TableAdapter tableAdapter ? tableAdapter : null;
	}

	@Override
	public @UnknownNullability BaseAdapter get(final String key) {
		return this.elements.get(this.checkKey(key));
	}

	@Override
	public void remove(final String key) {
		this.elements.remove(this.checkKey(key));
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public String[] getKeys() {
		return this.elements.keySet().toArray(new String[0]);
	}

	private String checkKey(final String key) {
		return Objects.requireNonNull(key);
	}
}
