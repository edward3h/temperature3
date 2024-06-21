package org.ethelred.temperature3;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MockDotenv implements Dotenv {
    private final Map<String, DotenvEntry> map;

    public MockDotenv(Map<String, String> values) {
        map = values.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new DotenvEntry(e.getKey(), e.getValue())));
    }

    @Override
    public Set<DotenvEntry> entries() {
        return Set.copyOf(map.values());
    }

    @Override
    public Set<DotenvEntry> entries(Filter filter) {
        return entries();
    }

    @Override
    public String get(String key) {
        return get(key, null);
    }

    @Override
    public String get(String key, String defaultValue) {
        if (map.containsKey(key)) {
            return map.get(key).getValue();
        }
        return defaultValue;
    }
}
