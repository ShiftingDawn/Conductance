package conductance.api.machine.gui;

import java.util.SequencedCollection;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

public final class WidgetLayouts {

	@RequiredArgsConstructor
	public static class VerticalList implements WidgetLayout {

		private final int widgetWidth;
		private final int widgetHeight;
		private final int spacing;
		private final boolean resizeParent;

		@Override
		public void apply(final int lastWidgetX, final int lastWidgetY, final int lastWidth, final int lastHeight, final IGuiWidget widget) {
			if (lastWidgetX >= 0) {
				widget.setX(lastWidgetX);
			}
			if (lastWidgetY >= 0) {
				widget.setY(lastWidgetY + lastHeight + Math.max(this.spacing, 0));
			}
			if (this.widgetWidth > 0) {
				widget.setWidth(this.widgetWidth);
			}
			if (this.widgetHeight > 0) {
				widget.setHeight(this.widgetHeight);
			}
		}

		@Override
		public @Nullable Size getParentResizeSize(final SequencedCollection<IGuiWidget> widgets) {
			if (!this.resizeParent) {
				return null;
			}
			final int width = widgets.stream().mapToInt(IGuiWidget::getWidth).max().orElse(0);
			final int height = widgets.getLast().getBounds().maxY() - widgets.getFirst().getY();
			return Size.of(width, height);
		}
	}

	private WidgetLayouts() {
	}
}
