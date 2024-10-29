package conductance.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import conductance.api.CAPI;
import conductance.content.item.MaterialItem;
import conductance.core.apiimpl.ApiBridge;
import conductance.core.apiimpl.MaterialTaggedSet;

public final class ConductanceItems {

	public static void init() {
		CAPI.REGS.materials().forEach(material -> CAPI.REGS.materialTaggedSets().values().stream().filter(set -> set.canGenerateItem(material)).forEach(set -> {
			final String name = set.getUnlocalizedName(material);
			final ItemBuilder<MaterialItem, Registrate> itemBuilder = ApiBridge.REGISTRATE.item(name, props -> new MaterialItem(props, material, set)).model(NonNullBiConsumer.noop()).properties(p -> p.stacksTo(set.getMaxStackSize())).color(() -> MaterialItem::handleColorTint);
			if (((MaterialTaggedSet) set).getItemGeneratorCallback() != null) {
				((MaterialTaggedSet) set).getItemGeneratorCallback().accept(material, itemBuilder);
			}
			CAPI.MATERIALS.register(set, material, itemBuilder.register());
		}));
	}
}
