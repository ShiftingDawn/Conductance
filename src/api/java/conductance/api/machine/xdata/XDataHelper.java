package conductance.api.machine.xdata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.Tag;
import io.netty.handler.codec.EncoderException;
import nl.appelgebakje22.xdata.adapter.NetworkInput;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;

public final class XDataHelper {

	public static void writeNbt(final NetworkOutput output, @Nullable final Tag tag) {
		final Tag nbt = tag == null ? EndTag.INSTANCE : tag;
		if (output instanceof final FriendlyNetworkOutput out) {
			out.writeNbt(nbt);
			return;
		}
		try {
			final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			final DataOutputStream dataOut = new DataOutputStream(byteOut);
			NbtIo.writeAnyTag(nbt, dataOut);
			dataOut.flush();
			final byte[] bytes = byteOut.toByteArray();
			output.write(bytes.length);
			output.write(bytes);
		} catch (final IOException e) {
			throw new EncoderException(e);
		}
	}

	public static Tag readNbt(final NetworkInput input) {
		if (input instanceof final FriendlyNetworkInput in) {
			return in.readNbt();
		}
		try {
			final byte[] bytes = new byte[input.readInt()];
			input.read(bytes);
			final DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(bytes));
			return NbtIo.read(dataIn, NbtAccounter.create(2097152L));
		} catch (final IOException e) {
			throw new EncoderException(e);
		}
	}

	private XDataHelper() {
	}
}
