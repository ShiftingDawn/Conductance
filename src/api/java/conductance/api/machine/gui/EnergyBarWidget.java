package conductance.api.machine.gui;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.widget.ProgressWidget;
import org.jetbrains.annotations.NotNull;
import conductance.api.capability.energy.IEnergyHandler;
import conductance.api.util.TextHelper;

public class EnergyBarWidget extends ProgressWidget {

	private final IEnergyHandler handler;

	public EnergyBarWidget(final GuiTheme theme, final IEnergyHandler handler) {
		super(ProgressWidget.JEIProgress, 0, 0, 162, 6);
		this.handler = handler;
		this.setProgressSupplier(() -> handler.getEnergyStored() * 1.0 / handler.getEnergyCapacity());
		this.setProgressTexture(IGuiTexture.EMPTY, theme.getEnergyBarOverlay());
		this.setBackground(theme.getEnergyBar());
		this.setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void drawInForeground(@NotNull final GuiGraphics graphics, final int mouseX, final int mouseY, final float partialTicks) {
		if (this.isMouseOverElement(mouseX, mouseY) && this.getHoverElement(mouseX, mouseY) == this && this.gui != null && this.gui.getModularUIGui() != null) {
			this.gui.getModularUIGui().setHoverTooltip(List.of(
					Component.translatable("tooltip.conductance.energy_bar.title"),
					Component.translatable("tooltip.conductance.energy_bar.stored", this.handler.getEnergyStored()).append(TextHelper.ENERGY_FORMAT),
					Component.translatable("tooltip.conductance.energy_bar.capacity", this.handler.getEnergyCapacity()).append(TextHelper.ENERGY_FORMAT)
			), ItemStack.EMPTY, null, null);
		}
	}
}
