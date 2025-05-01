package com.example.ip4counter.processor;

import com.example.ip4counter.container.IntContainer;
import com.example.ip4counter.parser.impl.IpV4Parser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class IpV4FileProcessor {

    private final IpV4Parser parser;
    private final IntContainer finalSet;

    public IpV4FileProcessor(IntContainer finalSet) {
        this.parser = new IpV4Parser();
        this.finalSet = finalSet;
    }

    public void processFile(String filename) {
        byte[] buffer = new byte[8192]; // 8KB read buffer
        byte[] lineBuffer = new byte[32]; // max IP line length < 16

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename))) {
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                processBuffer(buffer, bytesRead, lineBuffer);
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
    }

    private void processBuffer(byte[] buffer, int bytesRead, byte[] lineBuffer) {
        int lineLength = 0; // Reset line length for each buffer read
        for (int i = 0; i < bytesRead; i++) {
            byte b = buffer[i];

            if (b == '\n') {
                if (lineLength > 0) {
                    processLine(lineBuffer, lineLength);
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

    private void processLine(byte[] lineBuffer, int lineLength) {
        try {
            int ip = parser.parse(lineBuffer, 0, lineLength - 1);
            finalSet.add(ip);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid IP: " + new String(lineBuffer, 0, lineLength));
        }
    }

}