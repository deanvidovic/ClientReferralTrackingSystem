package hr.clientreferraltrackingsystem.generics;

/**
 * A generic class representing a pair of key and value.
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class PairData<K, V> {
    private final K key;
    private final V value;

    /**
     * Constructs a new PairData with the specified key and value.
     *
     * @param key the key of the pair
     * @param value the value of the pair
     */
    public PairData(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of this pair.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Returns the value of this pair.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }
}
