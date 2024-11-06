package nl.appelgebakje22.xdata.adapter;

import java.util.ArrayList;
import java.util.List;
import nl.appelgebakje22.xdata.XData;
import org.jetbrains.annotations.UnknownNullability;

public class BaseArrayAdapter implements ArrayAdapter {

	private final List<BaseAdapter> elements = new ArrayList<>();
	private final AdapterFactory adapters;

	public BaseArrayAdapter(final AdapterFactory adapters) {
		this.adapters = adapters;
	}

	@Override
	public void add(final boolean value) {
		this.add(this.adapters.ofBoolean(value));
	}

	@Override
	public void add(final byte value) {
		this.add(this.adapters.ofByte(value));
	}

	@Override
	public void add(final short value) {
		this.add(this.adapters.ofShort(value));
	}

	@Override
	public void add(final int value) {
		this.add(this.adapters.ofInt(value));
	}

	@Override
	public void add(final long value) {
		this.add(this.adapters.ofLong(value));
	}

	@Override
	public void add(final float value) {
		this.add(this.adapters.ofFloat(value));
	}

	@Override
	public void add(final double value) {
		this.add(this.adapters.ofDouble(value));
	}

	@Override
	public void add(final char value) {
		this.add(this.adapters.ofChar(value));
	}

	@Override
	public void add(final String value) {
		this.add(this.adapters.ofString(value));
	}

	@Override
	public void add(final BaseAdapter value) {
		if (value != null) {
			this.elements.add(value);
		}
	}

	@Override
	public ArrayAdapter addArray() {
		return XData.make(this.adapters.array(), this.elements::add);
	}

	@Override
	public TableAdapter addTable() {
		return XData.make(this.adapters.table(), this.elements::add);
	}

	@Override
	public void set(final int index, final boolean value) {
		this.set(index, this.adapters.ofBoolean(value));
	}

	@Override
	public void set(final int index, final byte value) {
		this.set(index, this.adapters.ofByte(value));
	}

	@Override
	public void set(final int index, final short value) {
		this.set(index, this.adapters.ofShort(value));
	}

	@Override
	public void set(final int index, final int value) {
		this.set(index, this.adapters.ofInt(value));
	}

	@Override
	public void set(final int index, final long value) {
		this.set(index, this.adapters.ofLong(value));
	}

	@Override
	public void set(final int index, final float value) {
		this.set(index, this.adapters.ofFloat(value));
	}

	@Override
	public void set(final int index, final double value) {
		this.set(index, this.adapters.ofDouble(value));
	}

	@Override
	public void set(final int index, final char value) {
		this.set(index, this.adapters.ofChar(value));
	}

	@Override
	public void set(final int index, final String value) {
		this.set(index, this.adapters.ofString(value));
	}

	@Override
	public void set(final int index, final BaseAdapter value) {
		if (value != null) {
			this.elements.set(this.checkIndex(index), value);
		}
	}

	@Override
	public ArrayAdapter setArray(final int index) {
		return XData.make(this.adapters.array(), array -> this.set(index, array));
	}

	@Override
	public TableAdapter setTable(final int index) {
		return XData.make(this.adapters.table(), table -> this.set(index, table));
	}

	@Override
	public boolean has(final int index) {
		return index >= 0 && index < this.elements.size();
	}

	@Override
	public boolean getBoolean(final int index, final boolean fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final BooleanAdapter booleanAdapter ? booleanAdapter.getBoolean() : fallback;
	}

	@Override
	public byte getByte(final int index, final byte fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getByte() : fallback;
	}

	@Override
	public short getShort(final int index, final short fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getShort() : fallback;
	}

	@Override
	public int getInt(final int index, final int fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getInt() : fallback;
	}

	@Override
	public long getLong(final int index, final long fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getLong() : fallback;
	}

	@Override
	public float getFloat(final int index, final float fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getFloat() : fallback;
	}

	@Override
	public double getDouble(final int index, final double fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final NumberAdapter<?> numberAdapter ? numberAdapter.getDouble() : fallback;
	}

	@Override
	public char getChar(final int index, final char fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final CharAdapter charAdapter ? charAdapter.getChar() : fallback;
	}

	@Override
	public @UnknownNullability String getString(final int index, final String fallback) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final StringAdapter stringAdapter ? stringAdapter.getString() : fallback;
	}

	@Override
	public @UnknownNullability ArrayAdapter getArray(final int index) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final ArrayAdapter arrayAdapter ? arrayAdapter : null;
	}

	@Override
	public @UnknownNullability TableAdapter getTable(final int index) {
		final BaseAdapter adapter = this.get(index);
		return adapter instanceof final TableAdapter tableAdapter ? tableAdapter : null;
	}

	@Override
	public @UnknownNullability BaseAdapter get(final int index) {
		return this.elements.get(this.checkIndex(index));
	}

	@Override
	public void remove(final int index) {
		this.elements.remove(this.checkIndex(index));
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	private int checkIndex(final int index) {
		if (!this.has(index)) {
			throw new IndexOutOfBoundsException("Index of %s does satisfy 0 <= index < %s".formatted(index, this.size()));
		}
		return index;
	}
}
