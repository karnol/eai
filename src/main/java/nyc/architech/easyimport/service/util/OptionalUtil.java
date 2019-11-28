package nyc.architech.easyimport.service.util;

import java.util.Optional;
import java.util.function.Supplier;

public final class OptionalUtil {

    private OptionalUtil() {
    }

    public static <T> Optional<T> orGet(Optional<T> first, Supplier<Optional<T>> supplier) {
        return (first.isPresent()) ? first : supplier.get();
    }
}
