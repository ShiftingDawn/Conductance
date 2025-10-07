package conductance.api.machine.multi;

import java.util.function.Supplier;

public record CheckToken<T>(Supplier<T> factory) {
}
