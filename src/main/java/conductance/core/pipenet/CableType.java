package conductance.core.pipenet;

import lombok.Getter;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

public enum CableType {

	WIRE_1X(NCMaterialTaggedSets.WIRE_1X, .25f, 1),
	WIRE_2X(NCMaterialTaggedSets.WIRE_2X, .375f, 2),
	WIRE_4X(NCMaterialTaggedSets.WIRE_4X, .5f, 4),
	WIRE_8X(NCMaterialTaggedSets.WIRE_8X, .625f, 8),
	WIRE_12X(NCMaterialTaggedSets.WIRE_12X, .75f, 12),
	WIRE_16X(NCMaterialTaggedSets.WIRE_16X, .875f, 16);

	@Getter
	private final TaggedMaterialSet materialTaggedSet;
	private final float thickness;
	@Getter
	private final int amperage;

	CableType(final TaggedMaterialSet taggedSet, final float thickness, final int amperage) {
		this.materialTaggedSet = taggedSet;
		this.thickness = thickness;
		this.amperage = amperage;
	}

	public PipeModel createPipeModel(final Material material) {
		return new PipeModel(this.thickness,
				() -> NCTextureTypes.WIRE_SIDE.getBlockTexture(material.getTextureSet(), null, null),
				() -> NCTextureTypes.WIRE_BASE.getBlockTexture(material.getTextureSet(), null, null),
				null, null);
	}

	public CableData getPhysicalProperties(final CableData baseProps) {
		return new CableData(baseProps.voltage(), baseProps.amperage() * this.amperage);
	}
}
