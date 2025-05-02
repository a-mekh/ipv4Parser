package com.example.ip4counter.container;

public interface IntContainer {

    /**
     * Adds a parsed IPv4 integer to the container.
     *
     * @param ip the signed int representing an IPv4 address
     */
    void add(long ip);

    /**
     * Returns the number of unique IP addresses stored.
     *
     * @return total unique IPs
     */
    long countUnique();

}
