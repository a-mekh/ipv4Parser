package com.example.ip4counter;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.container.impl.DualBitSetContainer;
import com.example.ip4counter.parser.IpV4FileParser;

/**
 * Main application for counting unique IPv4 addresses in a large file using memory-mapped I/O.
 * <p>
 * This version avoids allocating String objects for each line. It processes raw bytes directly,
 * using a low-level parser to convert IP addresses into 32-bit integers. Memory-mapped I/O allows
 * efficient access to large files without fully loading them into memory.
 */
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java -jar ipV4Counter.jar <filename>");
            System.exit(1);
        }

        String filename = args[0];
        System.out.println("Reading from: " + filename);

        long startTime = System.currentTimeMillis();
        DualBitSetContainer container = new DualBitSetContainer(Integer.MAX_VALUE);

        new IpV4FileParser(container).processFile(filename);

        long duration = System.currentTimeMillis() - startTime;
        logFinalResults(container, duration);
    }

    private static void logFinalResults(IntContainer finalSet, long duration) {
        System.out.println("Unique IPs: " + finalSet.countUnique());
        System.out.printf("Completed in %.3f seconds.%n", duration / 1000.0);
    }
}