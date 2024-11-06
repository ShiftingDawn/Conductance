package conductance.api.machine.xdata;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BaseBooleanAdapter;
import nl.appelgebakje22.xdata.adapter.BaseCharAdapter;
import nl.appelgebakje22.xdata.adapter.BaseDoubleAdapter;
import nl.appelgebakje22.xdata.adapter.BaseFloatAdapter;
import nl.appelgebakje22.xdata.adapter.BaseIntAdapter;
import nl.appelgebakje22.xdata.adapter.BaseLongAdapter;
import nl.appelgebakje22.xdata.adapter.BaseShortAdapter;
import nl.appelgebakje22.xdata.adapter.BooleanAdapter;
import nl.appelgebakje22.xdata.adapter.ByteAdapter;
import nl.appelgebakje22.xdata.adapter.CharAdapter;
import nl.appelgebakje22.xdata.adapter.DoubleAdapter;
import nl.appelgebakje22.xdata.adapter.FloatAdapter;
import nl.appelgebakje22.xdata.adapter.IntAdapter;
import nl.appelgebakje22.xdata.adapter.LongAdapter;
import nl.appelgebakje22.xdata.adapter.NullTypeAdapter;
import nl.appelgebakje22.xdata.adapter.ShortAdapter;
import nl.appelgebakje22.xdata.adapter.StringAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;
import nl.appelgebakje22.xdata.adapter.UnknownTypeAdapter;
import org.jetbrains.annotations.Nullable;

public class NbtAdapterFactory implements AdapterFactory {

	public static final NbtAdapterFactory INSTANCE = new NbtAdapterFactory();

	@Override
	public BooleanAdapter ofBoolean(final boolean value) {
		return XData.make(new BaseBooleanAdapter(), adapter -> adapter.setBoolean(value));
	}

	@Override
	public ByteAdapter ofByte(final byte value) {
		return XData.make(new NbtByteAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public ShortAdapter ofShort(final short value) {
		return XData.make(new BaseShortAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public IntAdapter ofInt(final int value) {
		return XData.make(new BaseIntAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public LongAdapter ofLong(final long value) {
		return XData.make(new BaseLongAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public FloatAdapter ofFloat(final float value) {
		return XData.make(new BaseFloatAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public DoubleAdapter ofDouble(final double value) {
		return XData.make(new BaseDoubleAdapter(), adapter -> adapter.setNumber(value));
	}

	@Override
	public CharAdapter ofChar(final char value) {
		return XData.make(new BaseCharAdapter(), adapter -> adapter.setChar(value));
	}

	@Override
	public StringAdapter ofString(@Nullable final String value) {
		return XData.make(new NbtStringAdapter(), adapter -> adapter.setString(value));
	}

	@Override
	public ArrayAdapter array() {
		return new NbtArrayAdapter(this);
	}

	@Override
	public TableAdapter table() {
		return new NbtTableAdapter(this);
	}

	public static BaseAdapter fromTag(final AdapterFactory adapters, final Tag tag) {
		return switch (tag) {
			case final ByteTag byteTag -> adapters.ofByte(byteTag.getAsByte());
			case final ShortTag shortTag -> adapters.ofShort(shortTag.getAsShort());
			case final IntTag intTag -> adapters.ofInt(intTag.getAsInt());
			case final LongTag longTag -> adapters.ofLong(longTag.getAsLong());
			case final FloatTag floatTag -> adapters.ofFloat(floatTag.getAsFloat());
			case final DoubleTag doubleTag -> adapters.ofDouble(doubleTag.getAsDouble());
			case final StringTag stringTag -> adapters.ofString(stringTag.getAsString());
			case final CompoundTag compoundTag -> NbtTableAdapter.fromTag(adapters, compoundTag);
			case final ListTag listTag -> NbtArrayAdapter.fromTag(adapters, listTag);
			case null -> new NullTypeAdapter();
			default -> UnknownTypeAdapter.of(tag);
		};
	}

	@Nullable
	public static Tag toTag(final BaseAdapter adapter) {
		return switch (adapter) {
			case final BooleanAdapter booleanAdapter -> ByteTag.valueOf(booleanAdapter.getBoolean());
			case final ByteAdapter byteAdapter -> ByteTag.valueOf(byteAdapter.getByte());
			case final ShortAdapter shortAdapter -> ShortTag.valueOf(shortAdapter.getShort());
			case final IntAdapter intAdapter -> IntTag.valueOf(intAdapter.getInt());
			case final LongAdapter longAdapter -> LongTag.valueOf(longAdapter.getLong());
			case final FloatAdapter floatAdapter -> FloatTag.valueOf(floatAdapter.getFloat());
			case final DoubleAdapter doubleAdapter -> DoubleTag.valueOf(doubleAdapter.getDouble());
			case final CharAdapter charAdapter -> StringTag.valueOf(String.valueOf(charAdapter.getChar()));
			case final StringAdapter stringAdapter -> StringTag.valueOf(stringAdapter.getString());
			case final ArrayAdapter arrayAdapter -> NbtArrayAdapter.toTag(arrayAdapter);
			case final TableAdapter tableAdapter -> NbtTableAdapter.toTag(tableAdapter);
			case final NullTypeAdapter nullAdapter -> null;
			case final UnknownTypeAdapter unknown -> unknown.get() instanceof final Tag tag ? tag : null;
			default -> throw new IllegalStateException("Unexpected value: " + adapter);
		};
	}
}
