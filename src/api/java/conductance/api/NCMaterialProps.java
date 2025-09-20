package conductance.api;

import java.util.function.Supplier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import conductance.api.material.Material;
import conductance.api.material.MaterialProp;
import conductance.api.material.MaterialTraitKey;

public final class NCMaterialProps {

	public static final MaterialProp<TagKey<Block>> REQUIRED_TOOL_LEVEL = new MaterialProp<>();
	public static final MaterialProp<Integer> BURN_TIME = new MaterialProp<>();

	public static final MaterialProp<Supplier<Material>> MAGNETIZED_FORM = new MaterialProp<>();
	public static final MaterialProp<Supplier<Material>> DEMAGNETIZED_FORM = new MaterialProp<>();

	public static final MaterialProp<MaterialTraitKey<?>> DEFAULT_FLUID = new MaterialProp<>();

	private NCMaterialProps() {
	}
}
