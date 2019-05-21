//Author: Joseph Standerfer
//St_id: josephst
//Date: 4/8/2019

package edu.colorado.nodes.josephst;

import java.util.Objects;

public class KeyValuePair<V>{
    private V key;
    private int value;

    public KeyValuePair(V key, int value){
        this.key = key;
        this.value = value;
    }
    public V getKey() {
        return key;
    }
    public void setKey(V key) {
        this.key = key;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyValuePair)) return false;
        KeyValuePair<?> that = (KeyValuePair<?>) o;
        return value == that.value &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
