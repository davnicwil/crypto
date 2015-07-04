package com.davnicwil.crypto;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class RandomStringGeneratorUTestBase {

    private static final String NOT_RANDOM_ENOUGH_FAILURE = "%s.generate() failed to generate %s unique Strings of length %s (only %s unique Strings were produced). This probably means the generated Strings aren't random enough";
    private static final String INCORRECT_LENGTH_FAILURE = "%s.generate() generated the String '%s' of length %s instead of the expected length %s";

    private Class<? extends RandomStringGenerator> testImpl;
    private String testImplName;
    private int lengthRangeStart;
    private int lengthRangeEnd;
    private int generatedSampleSizeAtEachLength;

    public RandomStringGeneratorUTestBase(Class<? extends RandomStringGenerator> testImpl) {
        this(testImpl, 4, 128, 1000);
    }

    public RandomStringGeneratorUTestBase(Class<? extends RandomStringGenerator> testImpl, int lengthRangeStart, int lengthRangeEnd, int generatedSampleSizeAtEachLength) {
        this.testImpl = testImpl;
        this.testImplName = testImpl.getSimpleName();
        this.lengthRangeStart = lengthRangeStart;
        this.lengthRangeEnd = lengthRangeEnd;
        this.generatedSampleSizeAtEachLength = generatedSampleSizeAtEachLength;
    }

    @Test
    public void itShouldGeneratStringsOfTheSpecifiedLength() {
        IntStream
                .rangeClosed(lengthRangeStart, lengthRangeEnd)
                .forEach(this::itShouldGenerateStringsOfTheSpecifiedLength);
    }

    @Test
    public void itShouldGenerateSufficientlyRandomStrings() {
        IntStream
                .rangeClosed(lengthRangeStart, lengthRangeEnd)
                .forEach(this::itShouldGenerateSufficientlyRandomStrings);
    }

    @Test
    public void performanceTest() {
        int length = 128;
        int n = 1000000;
        RandomStringGenerator testObj = buildTestImpl(length);
        long start = System.currentTimeMillis();
        for(int i = 0; i < n; i++) { testObj.generate(); }
        long finish = System.currentTimeMillis();
        System.out.println(testImplName + ".generate() generated " + n + " Strings of length " + length + " in " + (finish-start) + " milliseconds");
    }

    private void itShouldGenerateStringsOfTheSpecifiedLength(int length) {
        RandomStringGenerator testObj = buildTestImpl(length);
        String result = testObj.generate();
        String failureMessage = String.format(INCORRECT_LENGTH_FAILURE, testImplName, result, result.length(), length);
        assertEquals(failureMessage, length, result.length());

    }

    private void itShouldGenerateSufficientlyRandomStrings(int length) {
        RandomStringGenerator testObj = buildTestImpl(length);
        Set<String> nRandomStrings = IntStream
                .range(0, generatedSampleSizeAtEachLength)
                .boxed()
                .map(i -> testObj.generate())
                .collect(Collectors.toSet());
        String failureMessage = String.format(NOT_RANDOM_ENOUGH_FAILURE, testImplName, generatedSampleSizeAtEachLength, length, nRandomStrings.size());
        assertEquals(failureMessage, generatedSampleSizeAtEachLength, nRandomStrings.size());
    }

    private RandomStringGenerator buildTestImpl(Integer length) {
        try {
            return testImpl.getDeclaredConstructor(Integer.class).newInstance(length);
        } catch (Exception e) {
            fail("Could not instantiate your implementation of " + RandomStringGenerator.class.getName() + ": " + testImpl.getClass().getName());
            throw new RuntimeException("test failed");
        }
    }
}
