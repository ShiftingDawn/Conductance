package conductance.init.item;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import io.netty.buffer.ByteBuf;
import conductance.api.NCDataComponents;
import conductance.Conductance;

public record ProgramCircuitSetItemPacketC2S(InteractionHand hand, int program) implements CustomPacketPayload {

	public static final CustomPacketPayload.Type<ProgramCircuitSetItemPacketC2S> TYPE = new CustomPacketPayload.Type<>(Conductance.id("program_circuit"));
	public static final StreamCodec<ByteBuf, ProgramCircuitSetItemPacketC2S> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.idMapper(id -> InteractionHand.values()[id], InteractionHand::ordinal), ProgramCircuitSetItemPacketC2S::hand,
		ByteBufCodecs.VAR_INT, ProgramCircuitSetItemPacketC2S::program,
		ProgramCircuitSetItemPacketC2S::new
	);

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ProgramCircuitSetItemPacketC2S.TYPE;
	}

	public static void register(final PayloadRegistrar registrar) {
		registrar.playToServer(ProgramCircuitSetItemPacketC2S.TYPE, ProgramCircuitSetItemPacketC2S.STREAM_CODEC, ProgramCircuitSetItemPacketC2S::handle);
	}

	public static void handle(final ProgramCircuitSetItemPacketC2S packet, final IPayloadContext ctx) {
		final ItemStack stack = ctx.player().getItemInHand(packet.hand);
		if (stack.has(NCDataComponents.PROGRAM_CIRCUIT) && ctx.player().containerMenu instanceof final ProgramCircuitMenu menu) {
			menu.getDataSlot().set(Mth.clamp(packet.program, 0, 24));
		}
	}
}
