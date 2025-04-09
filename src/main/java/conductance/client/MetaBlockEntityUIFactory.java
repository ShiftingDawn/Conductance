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
import conductance.api.machine.gui.MetaBlockEntityGuiHolder;
import conductance.Conductance;

public class MetaBlockEntityUIFactory extends UIFactory<MetaBlockEntity<?>> {

	public static final MetaBlockEntityUIFactory INSTANCE = new MetaBlockEntityUIFactory();

	public MetaBlockEntityUIFactory() {
		super(Conductance.id("meta_block_entity"));
	}

	public static <T extends MetaBlockEntity<?> & MetaBlockEntityGuiHolder> ModularUI createGui(final T mbe, final Player player) {
		return new ModularUI(GuiHelper.GUI_WIDTH, GuiHelper.GUI_HEIGHT, mbe, player).widget(new RootWidget(mbe));
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
