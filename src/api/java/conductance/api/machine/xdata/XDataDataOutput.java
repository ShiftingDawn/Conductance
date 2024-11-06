package conductance.api.machine.xdata;

import java.io.DataOutput;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import nl.appelgebakje22.xdata.adapter.NetworkOutput;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class XDataDataOutput implements DataOutput {

	private final NetworkOutput output;

	@Override
	public void write(final int b) throws IOException {
		this.output.write(b);
	}

	@Override
	public void write(@NotNull final byte[] b) throws IOException {
		this.output.write(b);
	}

	@Override
	public void write(@NotNull final byte[] b, final int off, final int len) throws IOException {
		this.output.write(b, off, len);
	}

	@Override
	public void writeBoolean(final boolean v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeByte(final int v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeShort(final int v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeChar(final int v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeInt(final int v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeLong(final long v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeFloat(final float v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeDouble(final double v) throws IOException {
		this.output.write(v);
	}

	@Override
	public void writeBytes(@NotNull final String s) throws IOException {
		this.output.write(s);
	}

	@Override
	public void writeChars(@NotNull final String s) throws IOException {
		this.output.write(s);
	}

	@Override
	public void writeUTF(@NotNull final String s) throws IOException {
		this.output.write(s);
	}
}
