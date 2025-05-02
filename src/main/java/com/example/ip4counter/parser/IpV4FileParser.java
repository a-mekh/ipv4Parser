package com.example.ip4counter.parser;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.converter.impl.IpV4Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IpV4FileParser {

    private static final long PROGRESS_UPDATE_INTERVAL_BYTES = (long) 1024 * 1024 * 1024; // 1 GB

    private final IpV4Converter parser;
    private final IntContainer finalSet;

    public IpV4FileParser(IntContainer finalSet) {
        this.parser = new IpV4Converter();
        this.finalSet = finalSet;
    }

    public void processFile(String filename) {
        Path path = Paths.get(filename);

        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII)) {
            long processedBytes = 0;
            long lastReported = 0;
            long fileSize = Files.size(path);

            String line;
            while ((line = reader.readLine()) != null) {
                byte[] ipBytes = line.getBytes(StandardCharsets.US_ASCII);
                processLine(ipBytes, ipBytes.length, finalSet);

                processedBytes += line.length() + 1; // +1 for newline character

                if (processedBytes - lastReported >= PROGRESS_UPDATE_INTERVAL_BYTES) {
                    double percent = 100.0 * processedBytes / fileSize;
                    System.out.printf("Processed: %.2f%% (%.2f GB)%n", percent, processedBytes / 1e9);
                    lastReported = processedBytes;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to process file", e);
        }
    }

    private void processLine(byte[] lineBuffer, int lineLength, IntContainer container) {
        try {
            long ip = parser.parse(lineBuffer, 0, lineLength);
            container.add(ip);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid IP: " + new String(lineBuffer, 0, lineLength));
        }
    }
}