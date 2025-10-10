package conductance.api.machine.gui;

import java.util.SequencedCollection;
import org.jetbrains.annotations.Nullable;

public interface WidgetLayout {

	void apply(int lastWidgetX, int lastWidgetY, int lastWidth, int lastHeight, IGuiWidget widget);

	default @Nullable Size getParentResizeSize(final SequencedCollection<IGuiWidget> widgets) {
		return null;
	}
}
