package conductance.api.util;

public record SafeOptional<T>(T value, boolean fallback) {

	public static <T> SafeOptional<T> of(final T value) {
		return new SafeOptional<>(value, false);
	}

	public static <T> SafeOptional<T> ofFallback(final T value) {
		return new SafeOptional<>(value, true);
	}
}
