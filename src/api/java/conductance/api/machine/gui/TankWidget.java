package conductance.api.machine.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidActionResult;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import conductance.api.machine.CapIO;
import conductance.api.machine.IFluidHandlerModifiable;
import conductance.api.machine.TankAwareFluidHandler;
import conductance.api.util.GuiUtils;
import conductance.api.util.ModelUtils;
import conductance.api.util.TextHelper;
import static conductance.api.util.GuiUtils.tooltipMoreInfo;
import static conductance.api.util.GuiUtils.tooltipTranslatable;

public final class TankWidget extends GuiWidget {

	private final @Getter IFluidHandler handler;
	private final @Getter int tank;
	private final CapIO io;

	public TankWidget(final int x, final int y, final int width, final int height, final IFluidHandler handler, final int tank, final CapIO io) {
		super(x, y, width, height);
		this.io = io;
		if (handler instanceof final IFluidHandlerModifiable modifiable && !(handler instanceof TankAwareFluidHandler)) {
			this.handler = new TankAwareFluidHandler(modifiable, tank);
		} else {
			this.handler = handler;
		}
		this.tank = tank;
		this.addTooltipCallback(tooltip -> {
			if (this.getMenu().getCarried().isEmpty()) {
				final int capacity = this.handler.getTankCapacity(this.tank);
				final FluidStack fluid = this.handler.getFluidInTank(this.tank);
				if (fluid.isEmpty()) {
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.empty");
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.capacity", Component.literal(TextHelper.NUMBER_FORMAT.format(capacity)).withStyle(ChatFormatting.BLUE));
				} else {
					GuiUtils.tooltip(tooltip, fluid.getHoverName());
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.stored",
						Component.literal(TextHelper.NUMBER_FORMAT.format(fluid.getAmount())).withStyle(ChatFormatting.YELLOW),
						Component.literal(TextHelper.NUMBER_FORMAT.format(capacity)).withStyle(ChatFormatting.BLUE)
					);
				}
				GuiUtils.tooltip(tooltip, Component.empty());
				tooltipMoreInfo(tooltip, () -> {
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.info.1");
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.info.2");
					tooltipTranslatable(tooltip, "guiWidget.conductance.tank.info.3", Minecraft.getInstance().options.keyShift.getKey().getDisplayName());
				});
			}
		});
	}

	public TankWidget(final int x, final int y, final IFluidHandler handler, final int tank, final CapIO io) {
		this(x, y, 18, 18, handler, tank, io);
	}

	@SuppressWarnings("resource")
	@Override
	public void renderBackground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
		if (this.containsMouse(mouseX, mouseY)) {
			this.getTheme().getSlotHighlightBack().draw(guiGraphics, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		final FluidStack fluid = this.handler.getFluidInTank(this.tank);
		if (!fluid.isEmpty()) {
			final IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(fluid.getFluid());
			final ResourceLocation texture = extensions.getStillTexture(fluid);
			final int color = ARGB.opaque(extensions.getTintColor(fluid));
			TextureAtlasSprite sprite = ModelUtils.getBlockSprite(texture);
			if (sprite == null) {
				sprite = ModelUtils.getBlockSprite(null);
			}
			final GuiSpriteScaling.Tile tileScaling = new GuiSpriteScaling.Tile(sprite.contents().width(), sprite.contents().height());
			guiGraphics.enableScissor(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1);
			guiGraphics.blitTiledSprite(
				RenderPipelines.GUI_TEXTURED, sprite,
				this.getX() + 1, this.getY() + 1, this.getWidth() - 2, this.getHeight() - 2,
				0, 0, tileScaling.width(), tileScaling.height(), tileScaling.width(), tileScaling.height(),
				color
			);
			guiGraphics.disableScissor();
		}
	}

	@Override
	public void renderForeground(final GuiGraphics guiGraphics, final int mouseX, final int mouseY, final float partialTick) {
		super.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
		if (this.containsMouse(mouseX, mouseY)) {
			this.getTheme().getSlotHighlightFront().draw(guiGraphics, mouseX, mouseY, this.getX(), this.getY(), this.getWidth(), this.getHeight());
		}
		final FluidStack fluid = this.handler.getFluidInTank(this.tank);
		if (!fluid.isEmpty()) {
			final String txt = TextHelper.getFormattedFluidAmount(fluid.getAmount());
			final float scale = Math.min((float) (this.getWidth() - 2) / (float) this.getFont().width(txt), 0.7f);
			guiGraphics.pose().pushMatrix();
			guiGraphics.pose().translate(this.getX() + this.getWidth() - this.getFont().width(txt) * scale, this.getY() + this.getHeight() - this.getFont().lineHeight * scale);
			guiGraphics.pose().scale(scale, scale);
			guiGraphics.drawString(this.getFont(), txt, 0, 0, -1, true);
			guiGraphics.pose().popMatrix();
		}
	}

	@Override
	public boolean onMouseClicked(final int mouseX, final int mouseY, final int button) {
		if (this.io != CapIO.NONE) {
			final ItemStack carried = this.getMenu().getCarried();
			if (!carried.isEmpty()) {
				final IFluidHandler itemHandler = carried.getCapability(Capabilities.FluidHandler.ITEM);
				if (itemHandler != null) {
					final boolean[] fill = {button == 0};
					if (fill[0] && itemHandler.getTanks() > 0 && itemHandler.getFluidInTank(0).isEmpty()) {
						//Left-clicking with an empty fluid container should drain fluid instead
						fill[0] = false;
					}
					if ((fill[0] && this.io.isInput()) || (!fill[0] && this.io.isOutput())) {
						this.sendToServer(1, output -> {
							output.putBoolean("fill", fill[0]);
							output.putBoolean("mult", GuiUtils.isShiftDown());
						});
					}
					return true;
				}
			}
		}
		return super.onMouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void handleClientRequest(final int requestId, final ValueInput input) {
		super.handleClientRequest(requestId, input);
		if (requestId == 1) {
			final boolean fill = input.getBooleanOr("fill", true);
			final boolean multiple = input.getBooleanOr("mult", false);
			final ItemStack newCarriedStack = fill ? this.tryFillIntoHandler(multiple) : this.tryDrainFromHandler(multiple);
			if (newCarriedStack != null) {
				this.getMenu().setCarried(newCarriedStack);
			}
		}
	}

	private @Nullable ItemStack tryFillIntoHandler(final boolean multiple) {
		if (!this.io.isInput()) {
			return null;
		}
		final ItemStack carried = this.getMenu().getCarried();
		if (carried.isEmpty() || carried.getCapability(Capabilities.FluidHandler.ITEM) == null) {
			return null;
		}
		ItemStack newStack = carried;
		for (int i = 0; i < (multiple ? carried.getCount() : 1); ++i) {
			final FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(newStack, this.handler, this.getMenu().getPlayerInventory().player.getCapability(Capabilities.ItemHandler.ENTITY),
				this.handler.getTankCapacity(this.tank), this.getMenu().getPlayerInventory().player, true);
			if (result.success) {
				newStack = result.result;
			}
		}
		return newStack == carried ? null : newStack;
	}

	private @Nullable ItemStack tryDrainFromHandler(final boolean multiple) {
		if (!this.io.isOutput()) {
			return null;
		}
		final ItemStack carried = this.getMenu().getCarried();
		if (carried.isEmpty() || carried.getCapability(Capabilities.FluidHandler.ITEM) == null) {
			return null;
		}
		ItemStack newStack = carried;
		for (int i = 0; i < (multiple ? carried.getCount() : 1); ++i) {
			final FluidActionResult result = FluidUtil.tryFillContainerAndStow(carried, this.handler, this.getMenu().getPlayerInventory().player.getCapability(Capabilities.ItemHandler.ENTITY),
				this.handler.getFluidInTank(this.tank).getAmount(), this.getMenu().getPlayerInventory().player, true);
			if (result.success) {
				newStack = result.result;
			}
		}
		return newStack == carried ? null : newStack;
	}
}
