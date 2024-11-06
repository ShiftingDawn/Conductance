package conductance.api.machine.xdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.ArrayAdapter;
import nl.appelgebakje22.xdata.adapter.BaseAdapter;
import nl.appelgebakje22.xdata.adapter.BaseArrayAdapter;
import nl.appelgebakje22.xdata.adapter.NullTypeAdapter;

class NbtArrayAdapter extends BaseArrayAdapter {

	NbtArrayAdapter(final NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(final ArrayAdapter adapter) {
		return XData.make(new ListTag(), nbt -> {
			for (int i = 0; i < adapter.size(); ++i) {
				final CompoundTag entry = new CompoundTag();
				entry.putInt("i", i);
				final Tag serializedTag = NbtAdapterFactory.toTag(adapter.get(i));
				if (serializedTag != null) {
					entry.put("dat", serializedTag);
				}
				nbt.add(entry);
			}
		});
	}

	public static ArrayAdapter fromTag(final AdapterFactory adapters, final ListTag list) {
		final BaseAdapter[] adapterArray = new BaseAdapter[list.size()];
		for (final Tag entry : list) {
			if (entry instanceof final CompoundTag compoundTag) {
				final int index = compoundTag.getInt("i");
				if (compoundTag.contains("dat")) {
					adapterArray[index] = NbtAdapterFactory.fromTag(adapters, compoundTag.get("dat"));
				}
			}
		}
		return XData.make(adapters.array(), result -> {
			for (final BaseAdapter baseAdapter : adapterArray) {
				result.add(baseAdapter != null ? baseAdapter : new NullTypeAdapter());
			}
		});
	}
}
