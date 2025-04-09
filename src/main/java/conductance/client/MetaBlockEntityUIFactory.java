package conductance.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import com.lowdragmc.lowdraglib.gui.factory.UIFactory;
import com.lowdragmc.lowdraglib.gui.modular.IUIHolder;
import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.MetaBlockEntity;
import conductance.api.machine.gui.GeneratedGuiHolder;
import conductance.Conductance;

public class MetaBlockEntityUIFactory extends UIFactory<MetaBlockEntity<?>> {

	public static final MetaBlockEntityUIFactory INSTANCE = new MetaBlockEntityUIFactory();
	public static final int GUI_WIDTH = 176;
	public static final int GUI_HEIGHT = 186;

	public MetaBlockEntityUIFactory() {
		super(Conductance.id("meta_block_entity"));
	}

	public static ModularUI createGui(final MetaBlockEntity<?> mbe, final GeneratedGuiHolder holder, final Player player) {
		return new ModularUI(MetaBlockEntityUIFactory.GUI_WIDTH, MetaBlockEntityUIFactory.GUI_HEIGHT, holder, player)
				.widget(new RootWidget(mbe));
	}

	@Override
	@Nullable
	protected ModularUI createUITemplate(final MetaBlockEntity<?> mbe, final Player player) {
		if (mbe instanceof final IUIHolder holder) {
			return holder.createUI(player);
		}
		return null;
	}

	@Override
	@Nullable
	protected MetaBlockEntity<?> readHolderFromSyncData(final RegistryFriendlyByteBuf buf) {
		final Level level = Minecraft.getInstance().level;
		if (level != null && level.getBlockEntity(buf.readBlockPos()) instanceof final MetaBlockEntity<?> mbe) {
			return mbe;
		}
		return null;
	}

	@Override
	protected void writeHolderToSyncData(final RegistryFriendlyByteBuf buf, final MetaBlockEntity<?> mbe) {
		buf.writeBlockPos(mbe.getBlockPos());
	}
}