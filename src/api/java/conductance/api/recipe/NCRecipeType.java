package conductance.api.recipe;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import conductance.api.machine.gui.GuiTheme;
import conductance.api.machine.gui.MachineGuiTemplate;
import conductance.api.registry.IRegistryObject;

public interface NCRecipeType extends IRegistryObject<ResourceLocation>, RecipeType<IRecipe> {

	int getMaxInputs(IRecipeElementType<?> elementType);

	int getMaxOutputs(IRecipeElementType<?> elementType);

	NCRecipeSerializer getSerializer();

	ResourceTexture getProgressBar();

	ProgressTexture.FillDirection getProgressBarDirection();

	WidgetGroup createGuiTemplate(DoubleSupplier progressSupplier, IItemHandlerModifiable inputItems, IItemHandlerModifiable outputItems, IFluidHandler inputFluids, IFluidHandler outputFluids, GuiTheme theme);

	MachineGuiTemplate<WidgetGroup, RecipeHolder> createGuiTemplate();

	boolean isHidden();

	Supplier<ItemStack> getRecipeTypeIcon();

	String getDescriptionId();

	Component getName();
}
