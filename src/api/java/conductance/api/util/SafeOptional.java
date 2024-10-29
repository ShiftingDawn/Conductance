package conductance.api.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SafeOptional<T> {

	private final T value;
	private final boolean fallback;

	public static <T> SafeOptional<T> of(final T value) {
		return new SafeOptional<>(value, false);
	}

	public static <T> SafeOptional<T> ofFallback(final T value) {
		return new SafeOptional<>(value, true);
	}
}
