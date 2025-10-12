package conductance.lib.pipenet;

import lombok.Getter;
import conductance.api.NCMaterialGenerationHandlers;
import conductance.api.material.MaterialGenerationHandler;

@Getter
public enum WireType {

	WIRE_1X(NCMaterialGenerationHandlers.WIRE_1X, 4, 1),
	WIRE_2X(NCMaterialGenerationHandlers.WIRE_2X, 6, 2),
	WIRE_4X(NCMaterialGenerationHandlers.WIRE_4X, 8, 4),
	WIRE_8X(NCMaterialGenerationHandlers.WIRE_8X, 10, 8),
	WIRE_12X(NCMaterialGenerationHandlers.WIRE_12X, 12, 12),
	WIRE_16X(NCMaterialGenerationHandlers.WIRE_16X, 14, 16);

	private final MaterialGenerationHandler handler;
	private final int voxels;
	private final int amperage;

	WireType(final MaterialGenerationHandler handler, final int voxels, final int amperage) {
		this.handler = handler;
		this.voxels = voxels;
		this.amperage = amperage;
	}

	public WireData getPhysicalProperties(final WireData baseProps) {
		return new WireData(baseProps.voltage(), baseProps.amperage() * this.amperage);
	}
}
