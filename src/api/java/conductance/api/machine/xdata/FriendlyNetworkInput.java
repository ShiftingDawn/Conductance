package conductance.api.machine.xdata;

import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.adapter.NetworkInput;

@RequiredArgsConstructor
public final class FriendlyNetworkInput implements NetworkInput {

	@Getter
	private final RegistryFriendlyByteBuf buffer;

	@Override
	public boolean readBoolean() {
		return this.buffer.readBoolean();
	}

	@Override
	public byte readByte() {
		return this.buffer.readByte();
	}

	@Override
	public short readShort() {
		return this.buffer.readShort();
	}

	@Override
	public int readInt() {
		return this.buffer.readVarInt();
	}

	@Override
	public long readLong() {
		return this.buffer.readVarLong();
	}

	@Override
	public float readFloat() {
		return this.buffer.readFloat();
	}

	@Override
	public double readDouble() {
		return this.buffer.readDouble();
	}

	@Override
	public char readChar() {
		return this.buffer.readChar();
	}

	@Override
	public String readString() {
		return this.buffer.readUtf();
	}

	@Override
	public void read(final byte[] bytes, final int start, final int length) {
		this.buffer.readBytes(bytes, start, length);
	}

	public Tag readNbt() {
		return this.buffer.readNbt(NbtAccounter.create(2097152L));
	}
}
