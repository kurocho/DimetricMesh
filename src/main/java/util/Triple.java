package util;

import java.util.Objects;

public class Triple<T, U, K> {
    private final T key;
    private final U value;
    private final K z;

    public Triple(T key, U value, K z) {
        this.key = key;
        this.value = value;
        this.z = z;
    }

    public T get1() {
        return this.key;
    }

    public U get2() {
        return this.value;
    }

    public K get3() {
        return this.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equals(key, triple.key) && Objects.equals(value, triple.value) && Objects.equals(z, triple.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, z);
    }
}
