package com.example.ip4counter;

import com.example.ip4counter.container.impl.DualBitSetContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DualBitSetContainerTest {

    DualBitSetContainer container = new DualBitSetContainer(10000);

    @Test
    void testAddAndCountUnique() {
        container.add(0x01020304L);       // 1.2.3.4
        container.add(0xC0A80102L);       // 192.168.1.2
        container.add(0xFFFFFFFFL);       // 255.255.255.255
        container.add(0xFFFFFFFFL);       // duplicate

        assertEquals(3, container.countUnique());

        container.add(0x7F000001L);       // 127.0.0.1
        container.add(0x80000000L);       // 128.0.0.0

        assertEquals(5, container.countUnique());
    }

    @Test
    void testEdgeCasesAndDuplicates() {
        container.add(0x7FFFFFFFL);       // Integer.MAX_VALUE
        container.add(0x80000000L);       // Integer.MIN_VALUE as unsigned long
        container.add(0xFFFFFFFFL);       // 255.255.255.255
        container.add(0L);               // 0.0.0.0
        container.add(1L);               // 0.0.0.1

        container.add(0x7FFFFFFFL);
        container.add(0x80000000L);
        container.add(0xFFFFFFFFL);
        container.add(0L);
        container.add(1L);

        assertEquals(5, container.countUnique());
    }

    @Test
    void testPositiveAndNegativeCollision() {
        container.add(0L);
        container.add(0xFFFFFFFFL);

        assertEquals(2, container.countUnique());
    }

    @Test
    void testNegativeConversionCorrectness() {
        long ip = 0xFFFFFFFFL;
        container.add(ip);

        assertEquals(1, container.countUnique());

        container.add(ip);
        assertEquals(1, container.countUnique());
    }

    @Test
    void testAddNegativeIp() {
        container.add(0x80000000L);       // 128.0.0.0
        container.add(0xC0A80102L);       // 192.168.1.2
        container.add(0xFFFFFFFFL);       // 255.255.255.255

        assertEquals(3, container.countUnique());
    }

    @Test
    void testAddPositiveIp() {
        container.add(0x01020304L);       // 1.2.3.4
        assertEquals(1, container.countUnique());
    }

    @Test
    void testDuplicateIp() {
        container.add(0x01020304L);
        container.add(0x01020304L);
        assertEquals(1, container.countUnique());
    }

    @Test
    void testManyUniqueIps() {
        for (long i = 0; i < 10000L; i++) {
            container.add(i);
        }
        assertEquals(10000, container.countUnique());
    }
}
