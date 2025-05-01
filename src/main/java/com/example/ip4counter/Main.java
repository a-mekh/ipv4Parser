package com.example.ip4counter;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.container.impl.IpV4DualBitSetContainer;
import com.example.ip4counter.parser.impl.IpV4Parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

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
            System.err.println("Usage: java -jar ipcounter.jar <filename>");
            System.exit(1);
        }

        String filename = args[0];
        System.out.println("Reading from: " + filename);

        long startTime = System.currentTimeMillis();

        IpV4DualBitSetContainer finalSet = new IpV4DualBitSetContainer();

        processFile(filename, finalSet);

        long duration = System.currentTimeMillis() - startTime;
        logFinalResults(finalSet, duration);
    }

    public static void processFile(String filename, IntContainer finalSet) {
        IpV4Parser parser = new IpV4Parser();
        byte[] buffer = new byte[8192]; // 8KB read buffer
        byte[] lineBuffer = new byte[32]; // max IP line length < 16
        int lineLength = 0;

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename))) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte b = buffer[i];

                    if (b == '\n') {
                        if (lineLength > 0) {
                            try {
                                int ip = parser.parse(lineBuffer, 0, lineLength - 1);
                                finalSet.add(ip);
                            } catch (IllegalArgumentException e) {
                                System.err.println("Invalid IP: " + new String(lineBuffer, 0, lineLength));
                                return;
                            }
                            lineLength = 0;
                        }
                    } else if (b != '\r') {
                        if (lineLength < lineBuffer.length) {
                            lineBuffer[lineLength++] = b;
                        } else {
                            throw new IllegalArgumentException("IP line too long");
                        }
                    }
                }
            }

            // Last line if not newline-terminated
            if (lineLength > 0) {
                try {
                    int ip = parser.parse(lineBuffer, 0, lineLength - 1);
                    finalSet.add(ip);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid final IP: " + new String(lineBuffer, 0, lineLength));
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void logFinalResults(IntContainer finalSet, long duration) {
        System.out.println("Unique IPs: " + finalSet.countUnique());
        System.out.printf("Completed in %.3f seconds.%n", duration / 1000.0);
    }
}