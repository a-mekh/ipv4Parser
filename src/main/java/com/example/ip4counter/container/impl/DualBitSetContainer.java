package com.example.ip4counter.container.impl;

import com.example.ip4counter.container.IntContainer;

import java.util.BitSet;

/**
 * A container for storing unique IPv4 addresses, where each address is represented as a signed integer (`int`).
 * This implementation uses two `BitSet` instances:
 * - `positive`: stores non-negative IPv4 addresses (<= 0x7FFFFFFF).
 * - `negative`: stores negative integers representing IPv4 addresses (> 0x7FFFFFFF).
 *
 * <p>IPv4 addresses are unsigned 32-bit integers, but Java `int` is signed. By splitting the address space into
 * positive and negative ranges, we can efficiently track all unique IPv4 addresses using two separate `BitSet` instances,
 * ensuring memory efficiency without creating a single massive `BitSet`.</p>
 */
public class DualBitSetContainer implements IntContainer {

    private final BitSet positive = new BitSet(Integer.MAX_VALUE);
    private final BitSet negative = new BitSet(Integer.MAX_VALUE);

    @Override
    public void add(int i) {
        if (i >= 0) {
            positive.set(i);
        } else {
            negative.set(~i);// ~ip turns -1 → 0, -2 → 1, etc.
        }
    }

    @Override
    public long countUnique() {
        return (long) positive.cardinality() + negative.cardinality();
    }
}