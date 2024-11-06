package conductance.api.machine.xdata;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import nl.appelgebakje22.xdata.XData;
import nl.appelgebakje22.xdata.adapter.AdapterFactory;
import nl.appelgebakje22.xdata.adapter.BaseTableAdapter;
import nl.appelgebakje22.xdata.adapter.TableAdapter;

class NbtTableAdapter extends BaseTableAdapter {

	NbtTableAdapter(final NbtAdapterFactory adapters) {
		super(adapters);
	}

	public static Tag toTag(final TableAdapter adapter) {
		return XData.make(new CompoundTag(), nbt -> {
			for (final String key : adapter.getKeys()) {
				final Tag serializedTag = NbtAdapterFactory.toTag(adapter.get(key));
				if (serializedTag != null) {
					nbt.put(key, serializedTag);
				}
			}
		});
	}

	public static TableAdapter fromTag(final AdapterFactory adapters, final CompoundTag compoundTag) {
		final TableAdapter result = adapters.table();
		compoundTag.getAllKeys().forEach(key -> {
			result.set(key, NbtAdapterFactory.fromTag(adapters, compoundTag.get(key)));
		});
		return result;
	}
}
