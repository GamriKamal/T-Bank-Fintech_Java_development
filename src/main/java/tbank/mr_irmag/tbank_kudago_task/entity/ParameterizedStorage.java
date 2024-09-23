package tbank.mr_irmag.tbank_kudago_task.entity;

import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class ParameterizedStorage<K, V> {
    private final ConcurrentHashMap<K, V> hashMap = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        hashMap.put(key, value);
    }

    public V get(K key) {
        return hashMap.get(key);
    }

    public void remove(K key) {
        hashMap.remove(key);
    }

    public boolean containsKey(K key) {
        return hashMap.containsKey(key);
    }

    public int size() {
        return hashMap.size();
    }

    public Set<Map.Entry<K, V>> getEntry() {
        return hashMap.entrySet();
    }
}
