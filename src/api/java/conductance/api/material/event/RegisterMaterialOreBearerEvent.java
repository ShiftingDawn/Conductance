package conductance.api.material.event;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.MaterialOreBearer;
import conductance.api.plugin.IConductancePluginEvent;

public interface RegisterMaterialOreBearerEvent extends IConductancePluginEvent {

	MaterialOreBearer register(String registryName, ResourceLocation bearingBlockModel, MapColor mapColor, SoundType soundType, @Nullable Consumer<MaterialOreBearerBuilder> builder);
}
