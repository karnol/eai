package nyc.architech.easyimport.service.util;

import java.util.Collection;
import java.util.stream.Stream;

public final class StreamUtil {

    private StreamUtil() {
    }

    public static <T> Stream<T> emptyIfNull(Collection<T> collection) {
        return (null == collection) ? Stream.empty() : collection.stream();
    }
}
