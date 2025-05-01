package com.example.ip4counter;

import com.example.ip4counter.container.impl.DualBitSetContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DualBitSetContainerTest {

    DualBitSetContainer container = new DualBitSetContainer();

    @Test
    void testAddAndCountUnique() {


        // Add some IP addresses
        container.add(0x01020304);  // 1.2.3.4
        container.add(0xC0A80102);  // 192.168.1.2
        container.add(0xFFFFFFFF);  // 255.255.255.255
        container.add(-1);          // -1 -> 0xFFFFFFFF (same as above)

        // Count the unique IPs
        assertEquals(3, container.countUnique());  // Should be 3, as the last IP (-1) is duplicate

        // Add some more unique IPs
        container.add(0x7F000001);  // 127.0.0.1
        container.add(-2147483648); // -2147483648 -> 0x80000000 (128.0.0.0)

        // Verify the updated count of unique IPs
        assertEquals(5, container.countUnique());  // Should be 5 unique IPs
    }

    @Test
    void testAddNegativeIp() {

        // Add a negative IP address (represents 128.0.0.0)
        container.add(-2147483648); // -2147483648 -> 0x80000000 (128.0.0.0)

        // Count the unique IPs (should be 1)
        assertEquals(1, container.countUnique());
    }

    @Test
    void testAddPositiveIp() {

        // Add a positive IP address (represents 1.2.3.4)
        container.add(0x01020304);  // 1.2.3.4

        // Count the unique IPs (should be 1)
        assertEquals(1, container.countUnique());
    }

    @Test
    void testDuplicateIp() {

        // Add the same IP multiple times
        container.add(0x01020304);  // 1.2.3.4
        container.add(0x01020304);  // 1.2.3.4 (duplicate)

        // Count the unique IPs (should still be 1)
        assertEquals(1, container.countUnique());
    }
}