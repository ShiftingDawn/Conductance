package conductance.api.machine.xdata;

import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;

@RequiredArgsConstructor
public final class FriendlyNetworkOutput implements NetworkOutput {

	@Getter
	private final RegistryFriendlyByteBuf buffer;

	@Override
	public void write(final boolean data) {
		this.buffer.writeBoolean(data);
	}

	@Override
	public void write(final byte data) {
		this.buffer.writeByte(data);
	}

	@Override
	public void write(final short data) {
		this.buffer.writeShort(data);
	}

	@Override
	public void write(final int data) {
		this.buffer.writeVarInt(data);
	}

	@Override
	public void write(final long data) {
		this.buffer.writeVarLong(data);
	}

	@Override
	public void write(final float data) {
		this.buffer.writeFloat(data);
	}

	@Override
	public void write(final double data) {
		this.buffer.writeDouble(data);
	}

	@Override
	public void write(final char data) {
		this.buffer.writeChar(data);
	}

	@Override
	public void write(final String data) {
		this.buffer.writeUtf(data);
	}

	@Override
	public void write(final byte[] bytes, final int start, final int length) {
		this.buffer.writeBytes(bytes, start, length);
	}

	public void writeNbt(final Tag tag) {
		this.buffer.writeNbt(tag);
	}
}
