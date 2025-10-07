package conductance.api.machine.gui;

import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import com.mojang.datafixers.util.Either;

public class TextLabelWidget extends GuiWidget {

	private final Either<Component, Supplier<Component>> text;
	private final int color;

	public TextLabelWidget(final int x, final int y, final Component text, final int color) {
		super(x, y, 0, 0);
		this.setSize(MutableSize.of(
			new ManagedInt(null, () -> this.getFont().width(this.getComponent())),
			new ManagedInt(null, () -> this.getFont().lineHeight)
		));
		this.text = Either.left(text);
		this.color = color;
	}

	public TextLabelWidget(final int x, final int y, final Component text) {
		this(x, y, text, -1);
	}

	public TextLabelWidget(final int x, final int y, final Supplier<Component> text, final int color) {
		super(x, y, 0, 0);
		this.text = Either.right(text);
		this.color = color;
	}

	public TextLabelWidget(final int x, final int y, final Supplier<Component> text) {
		this(x, y, text, -1);
	}

	@Override
	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		guiGraphics.drawString(this.getFont(), this.getComponent(), this.getX(), this.getY(), this.color);
	}

	public Component getComponent() {
		return this.text.left().orElseGet(() -> this.text.right().orElseThrow().get());
	}
}
