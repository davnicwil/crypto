package com.davnicwil.crypto.impl;

import com.davnicwil.crypto.RandomStringGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import java.security.SecureRandom;
import java.util.Random;

@Singleton
public class RandomStringGeneratorImpl implements RandomStringGenerator {

    private static final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879".toCharArray();
    private static final int CHARS_LENGTH = CHARS.length;

    private int length;
    private SecureRandom sr;
    private int calls;

    @Inject
    public RandomStringGeneratorImpl(@Named("tokenLength") Integer length) {
        this.length = length;
        sr = new SecureRandom();
    }

    public String generate() {
        Random rand = new Random(generateUnguessableSeed());
        char[] chars = new char[length];
        for(int i = 0; i < length; i++) chars[i] = getRandomChar(rand);
        return new String(chars);
    }

    private char getRandomChar(Random rand) {
        return CHARS[rand.nextInt(CHARS_LENGTH)];
    }

    // SecureRandom + number of calls so far generates an unguessable seed
    private long generateUnguessableSeed() {
        return sr.nextLong() + calls++;
    }
}
