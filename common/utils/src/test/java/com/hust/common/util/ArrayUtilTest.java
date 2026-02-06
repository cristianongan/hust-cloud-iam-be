package com.hust.common.util;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @author: LinhLH
 **/
public class ArrayUtilTest {
    @Test
    void appendBooleanArrays_emptyArrays() {
        boolean[] a1 = {};
        boolean[] a2 = {};
        boolean[] result = ArrayUtil.append(a1, a2);
        assertArrayEquals(new boolean[]{}, result);
    }

    @Test
    void appendBooleanArrays_singleArray() {
        boolean[] a1 = {true, false};
        boolean[] result = ArrayUtil.append(a1);
        assertArrayEquals(new boolean[]{true, false}, result);
    }

    @Test
    void appendBooleanArrays_multipleArrays() {
        boolean[] a1 = {true, false};
        boolean[] a2 = {false, true};
        boolean[] a3 = {true, true};
        boolean[] result = ArrayUtil.append(a1, a2, a3);
        assertArrayEquals(new boolean[]{true, false, false, true, true, true}, result);
    }

    @Test

    public void testAppendBoolean() {

        // Test case 1: Append to an empty array

        boolean[] emptyArray = new boolean[]{};

        boolean[] result1 = ArrayUtil.append(emptyArray, true);

        assertArrayEquals(new boolean[]{true}, result1);


        // Test case 2: Append to a non-empty array

        boolean[] originalArray = new boolean[]{true, false};

        boolean[] result2 = ArrayUtil.append(originalArray, false);

        assertArrayEquals(new boolean[]{true, false, false}, result2);


        // Test case 3: Append different boolean values

        boolean[] mixedArray = new boolean[]{true, true, false};

        boolean[] result3 = ArrayUtil.append(mixedArray, true);

        assertArrayEquals(new boolean[]{true, true, false, true}, result3);

    }

    @Test
    public void testAppendByteArrays() {
        // Test case 1: Append two byte arrays
        byte[] a1 = new byte[]{(byte)0xe0, 0x4f};
        byte[] a2 = new byte[]{(byte)0xba, (byte)0x8a};
        byte[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new byte[]{(byte)0xe0, 0x4f, (byte)0xba, (byte)0x8a}, result1);

        // Test case 2: Append multiple byte arrays
        byte[] a3 = new byte[]{0x01, 0x02};
        byte[] a4 = new byte[]{0x03, 0x04};
        byte[] a5 = new byte[]{0x05, 0x06};
        byte[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06}, result2);

        // Test case 3: Append empty arrays
        byte[] emptyArray = new byte[]{};
        byte[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new byte[]{}, result3);
    }

    @Test
    public void testAppendByte() {
        // Test case 1: Append to an empty array
        byte[] emptyArray = new byte[]{};
        byte[] result1 = ArrayUtil.append(emptyArray, (byte)0x01);
        assertArrayEquals(new byte[]{(byte)0x01}, result1);

        // Test case 2: Append to a non-empty array
        byte[] originalArray = new byte[]{(byte)0xe0, 0x4f};
        byte[] result2 = ArrayUtil.append(originalArray, (byte)0xba);
        assertArrayEquals(new byte[]{(byte)0xe0, 0x4f, (byte)0xba}, result2);

        // Test case 3: Append different byte values
        byte[] mixedArray = new byte[]{(byte)0x11, (byte)0x22, (byte)0x33};
        byte[] result3 = ArrayUtil.append(mixedArray, (byte)0x44);
        assertArrayEquals(new byte[]{(byte)0x11, (byte)0x22, (byte)0x33, (byte)0x44}, result3);
    }

    @Test
    public void testAppendCharArrays() {
        // Test case 1: Append two char arrays
        char[] a1 = new char[]{'a', 'b'};
        char[] a2 = new char[]{'c', 'd'};
        char[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new char[]{'a', 'b', 'c', 'd'}, result1);

        // Test case 2: Append multiple char arrays
        char[] a3 = new char[]{'x'};
        char[] a4 = new char[]{'y', 'z'};
        char[] a5 = new char[]{'1', '2', '3'};
        char[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new char[]{'x', 'y', 'z', '1', '2', '3'}, result2);

        // Test case 3: Append empty arrays
        char[] emptyArray = new char[]{};
        char[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new char[]{}, result3);

        // Test case 4: Append arrays with special characters
        char[] specialChars1 = new char[]{'@', '#'};
        char[] specialChars2 = new char[]{'$', '%'};
        char[] result4 = ArrayUtil.append(specialChars1, specialChars2);
        assertArrayEquals(new char[]{'@', '#', '$', '%'}, result4);
    }
    @Test
    public void testAppendChar() {
        // Test case 1: Append to an empty array
        char[] emptyArray = new char[]{};
        char[] result1 = ArrayUtil.append(emptyArray, 'x');
        assertArrayEquals(new char[]{'x'}, result1);

        // Test case 2: Append to a non-empty array
        char[] originalArray = new char[]{'a', 'b'};
        char[] result2 = ArrayUtil.append(originalArray, 'c');
        assertArrayEquals(new char[]{'a', 'b', 'c'}, result2);

        // Test case 3: Append different types of characters
        char[] mixedArray = new char[]{'1', '2', '3'};
        char[] result3 = ArrayUtil.append(mixedArray, 'A');
        assertArrayEquals(new char[]{'1', '2', '3', 'A'}, result3);

        // Test case 4: Append special characters
        char[] specialCharArray = new char[]{'@', '#'};
        char[] result4 = ArrayUtil.append(specialCharArray, '$');
        assertArrayEquals(new char[]{'@', '#', '$'}, result4);
    }

    @Test
    public void testAppendDoubleArrays() {
        // Test case 1: Append two double arrays
        double[] a1 = new double[]{1.1, 2.2};
        double[] a2 = new double[]{3.3, 4.4};
        double[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new double[]{1.1, 2.2, 3.3, 4.4}, result1, 0.0001);

        // Test case 2: Append multiple double arrays
        double[] a3 = new double[]{0.5};
        double[] a4 = new double[]{1.5, 2.5};
        double[] a5 = new double[]{3.5, 4.5, 5.5};
        double[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new double[]{0.5, 1.5, 2.5, 3.5, 4.5, 5.5}, result2, 0.0001);

        // Test case 3: Append empty arrays
        double[] emptyArray = new double[]{};
        double[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new double[]{}, result3, 0.0001);

        // Test case 4: Append arrays with negative and zero values
        double[] negativeArray1 = new double[]{-1.1, -2.2};
        double[] negativeArray2 = new double[]{0.0, 0.0};
        double[] result4 = ArrayUtil.append(negativeArray1, negativeArray2);
        assertArrayEquals(new double[]{-1.1, -2.2, 0.0, 0.0}, result4, 0.0001);
    }

    @Test
    public void testAppendDouble() {
        // Test case 1: Append to an empty array
        double[] emptyArray = new double[]{};
        double[] result1 = ArrayUtil.append(emptyArray, 1.1);
        assertArrayEquals(new double[]{1.1}, result1, 0.0001);

        // Test case 2: Append to a non-empty array
        double[] originalArray = new double[]{1.1, 2.2};
        double[] result2 = ArrayUtil.append(originalArray, 3.3);
        assertArrayEquals(new double[]{1.1, 2.2, 3.3}, result2, 0.0001);

        // Test case 3: Append different types of double values
        double[] mixedArray = new double[]{-1.1, 0.0, 1.1};
        double[] result3 = ArrayUtil.append(mixedArray, 2.2);
        assertArrayEquals(new double[]{-1.1, 0.0, 1.1, 2.2}, result3, 0.0001);

        // Test case 4: Append very small and very large numbers
        double[] precisionArray = new double[]{1e-10, 1e10};
        double[] result4 = ArrayUtil.append(precisionArray, 5.5);
        assertArrayEquals(new double[]{1e-10, 1e10, 5.5}, result4, 0.0001);

        // Test case 5: Append NaN and Infinity
        double[] specialArray = new double[]{Double.NaN, Double.POSITIVE_INFINITY};
        double[] result5 = ArrayUtil.append(specialArray, Double.NEGATIVE_INFINITY);
        assertArrayEquals(new double[]{Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}, result5, 0.0001);
    }


    @Test
    public void testAppendFloatArrays() {
        // Test case 1: Append two float arrays
        float[] a1 = new float[]{1.1f, 2.2f};
        float[] a2 = new float[]{3.3f, 4.4f};
        float[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new float[]{1.1f, 2.2f, 3.3f, 4.4f}, result1, 0.0001f);

        // Test case 2: Append multiple float arrays
        float[] a3 = new float[]{0.5f};
        float[] a4 = new float[]{1.5f, 2.5f};
        float[] a5 = new float[]{3.5f, 4.5f, 5.5f};
        float[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new float[]{0.5f, 1.5f, 2.5f, 3.5f, 4.5f, 5.5f}, result2, 0.0001f);

        // Test case 3: Append empty arrays
        float[] emptyArray = new float[]{};
        float[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new float[]{}, result3, 0.0001f);

        // Test case 4: Append arrays with negative and zero values
        float[] negativeArray1 = new float[]{-1.1f, -2.2f};
        float[] negativeArray2 = new float[]{0.0f, 0.0f};
        float[] result4 = ArrayUtil.append(negativeArray1, negativeArray2);
        assertArrayEquals(new float[]{-1.1f, -2.2f, 0.0f, 0.0f}, result4, 0.0001f);

        // Test case 5: Append arrays with very small and very large numbers
        float[] precisionArray1 = new float[]{1e-5f, 1e5f};
        float[] precisionArray2 = new float[]{2e-5f, 2e5f};
        float[] result5 = ArrayUtil.append(precisionArray1, precisionArray2);
        assertArrayEquals(new float[]{1e-5f, 1e5f, 2e-5f, 2e5f}, result5, 0.0001f);
    }

    @Test
    public void testAppendFloat() {
        // Test case 1: Append to an empty array
        float[] emptyArray = new float[]{};
        float[] result1 = ArrayUtil.append(emptyArray, 1.1f);
        assertArrayEquals(new float[]{1.1f}, result1, 0.0001f);

        // Test case 2: Append to a non-empty array
        float[] originalArray = new float[]{1.1f, 2.2f};
        float[] result2 = ArrayUtil.append(originalArray, 3.3f);
        assertArrayEquals(new float[]{1.1f, 2.2f, 3.3f}, result2, 0.0001f);

        // Test case 3: Append different types of float values
        float[] mixedArray = new float[]{-1.1f, 0.0f, 1.1f};
        float[] result3 = ArrayUtil.append(mixedArray, 2.2f);
        assertArrayEquals(new float[]{-1.1f, 0.0f, 1.1f, 2.2f}, result3, 0.0001f);

        // Test case 4: Append very small and very large numbers
        float[] precisionArray = new float[]{1e-5f, 1e5f};
        float[] result4 = ArrayUtil.append(precisionArray, 5.5f);
        assertArrayEquals(new float[]{1e-5f, 1e5f, 5.5f}, result4, 0.0001f);
    }

    @Test
    public void testAppendIntArrays() {
        // Test case 1: Append two int arrays
        int[] a1 = new int[]{1, 2};
        int[] a2 = new int[]{3, 4};
        int[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new int[]{1, 2, 3, 4}, result1);

        // Test case 2: Append multiple int arrays
        int[] a3 = new int[]{5};
        int[] a4 = new int[]{6, 7};
        int[] a5 = new int[]{8, 9, 10};
        int[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new int[]{5, 6, 7, 8, 9, 10}, result2);

        // Test case 3: Append empty arrays
        int[] emptyArray = new int[]{};
        int[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new int[]{}, result3);

        // Test case 4: Append arrays with negative and positive values
        int[] negativeArray1 = new int[]{-1, -2};
        int[] positiveArray = new int[]{3, 4};
        int[] result4 = ArrayUtil.append(negativeArray1, positiveArray);
        assertArrayEquals(new int[]{-1, -2, 3, 4}, result4);
    }

    @Test
    public void testAppendLongArrays() {
        // Test case 1: Append two long arrays
        long[] a1 = new long[]{1L, 2L};
        long[] a2 = new long[]{3L, 4L};
        long[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new long[]{1L, 2L, 3L, 4L}, result1);

        // Test case 2: Append multiple long arrays
        long[] a3 = new long[]{5L};
        long[] a4 = new long[]{6L, 7L};
        long[] a5 = new long[]{8L, 9L, 10L};
        long[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new long[]{5L, 6L, 7L, 8L, 9L, 10L}, result2);

        // Test case 3: Append empty arrays
        long[] emptyArray = new long[]{};
        long[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new long[]{}, result3);

        // Test case 4: Append arrays with negative and positive values
        long[] negativeArray1 = new long[]{-1L, -2L};
        long[] positiveArray = new long[]{3L, 4L};
        long[] result4 = ArrayUtil.append(negativeArray1, positiveArray);
        assertArrayEquals(new long[]{-1L, -2L, 3L, 4L}, result4);

        // Test case 5: Append arrays with large values
        long[] largeValueArray1 = new long[]{Long.MIN_VALUE, -1000000L};
        long[] largeValueArray2 = new long[]{Long.MAX_VALUE, 1000000L};
        long[] result5 = ArrayUtil.append(largeValueArray1, largeValueArray2);
        assertArrayEquals(new long[]{Long.MIN_VALUE, -1000000L, Long.MAX_VALUE, 1000000L}, result5);
    }


    // Unit Test
    @Test
    public void testAppendLong() {
        // Test case 1: Append to an empty array
        long[] emptyArray = new long[]{};
        long[] result1 = ArrayUtil.append(emptyArray, 1L);
        assertArrayEquals(new long[]{1L}, result1);

        // Test case 2: Append to a non-empty array
        long[] originalArray = new long[]{1L, 2L};
        long[] result2 = ArrayUtil.append(originalArray, 3L);
        assertArrayEquals(new long[]{1L, 2L, 3L}, result2);

        // Test case 3: Append different types of long values
        long[] mixedArray = new long[]{-1L, 0L, 1L};
        long[] result3 = ArrayUtil.append(mixedArray, 2L);
        assertArrayEquals(new long[]{-1L, 0L, 1L, 2L}, result3);

        // Test case 4: Append large values
        long[] largeValueArray = new long[]{Long.MIN_VALUE, -1000000L};
        long[] result4 = ArrayUtil.append(largeValueArray, Long.MAX_VALUE);
        assertArrayEquals(new long[]{Long.MIN_VALUE, -1000000L, Long.MAX_VALUE}, result4);

        // Test case 5: Append zero to various arrays
        long[] zeroTestArray1 = new long[]{1L, 2L, 3L};
        long[] result5 = ArrayUtil.append(zeroTestArray1, 0L);
        assertArrayEquals(new long[]{1L, 2L, 3L, 0L}, result5);

        long[] zeroTestArray2 = new long[]{};
        long[] result6 = ArrayUtil.append(zeroTestArray2, 0L);
        assertArrayEquals(new long[]{0L}, result6);
    }

    @Test
    public void testAppendShortArrays() {
        // Test case 1: Append two short arrays
        short[] a1 = new short[]{1, 2};
        short[] a2 = new short[]{3, 4};
        short[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new short[]{1, 2, 3, 4}, result1);

        // Test case 2: Append multiple short arrays
        short[] a3 = new short[]{5};
        short[] a4 = new short[]{6, 7};
        short[] a5 = new short[]{8, 9, 10};
        short[] result2 = ArrayUtil.append(a3, a4, a5);
        assertArrayEquals(new short[]{5, 6, 7, 8, 9, 10}, result2);

        // Test case 3: Append empty arrays
        short[] emptyArray = new short[]{};
        short[] result3 = ArrayUtil.append(emptyArray, emptyArray);
        assertArrayEquals(new short[]{}, result3);

        // Test case 4: Append arrays with negative and positive values
        short[] negativeArray1 = new short[]{-1, -2};
        short[] positiveArray = new short[]{3, 4};
        short[] result4 = ArrayUtil.append(negativeArray1, positiveArray);
        assertArrayEquals(new short[]{-1, -2, 3, 4}, result4);

        // Test case 5: Append arrays with boundary values
        short[] boundaryArray1 = new short[]{Short.MIN_VALUE, -100};
        short[] boundaryArray2 = new short[]{Short.MAX_VALUE, 100};
        short[] result5 = ArrayUtil.append(boundaryArray1, boundaryArray2);
        assertArrayEquals(new short[]{Short.MIN_VALUE, -100, Short.MAX_VALUE, 100}, result5);
    }

    @Test
    public void testAppendShort() {
        // Test case 1: Append to an empty array
        short[] emptyArray = new short[]{};
        short[] result1 = ArrayUtil.append(emptyArray, (short)1);
        assertArrayEquals(new short[]{1}, result1);

        // Test case 2: Append to a non-empty array
        short[] originalArray = new short[]{1, 2};
        short[] result2 = ArrayUtil.append(originalArray, (short)3);
        assertArrayEquals(new short[]{1, 2, 3}, result2);

        // Test case 3: Append different types of short values
        short[] mixedArray = new short[]{-1, 0, 1};
        short[] result3 = ArrayUtil.append(mixedArray, (short)2);
        assertArrayEquals(new short[]{-1, 0, 1, 2}, result3);

        // Test case 4: Append large values
        short[] largeValueArray = new short[]{Short.MIN_VALUE, -100};
        short[] result4 = ArrayUtil.append(largeValueArray, Short.MAX_VALUE);
        assertArrayEquals(new short[]{Short.MIN_VALUE, -100, Short.MAX_VALUE}, result4);

        // Test case 5: Append zero to various arrays
        short[] zeroTestArray1 = new short[]{1, 2, 3};
        short[] result5 = ArrayUtil.append(zeroTestArray1, (short)0);
        assertArrayEquals(new short[]{1, 2, 3, 0}, result5);

        short[] zeroTestArray2 = new short[]{};
        short[] result6 = ArrayUtil.append(zeroTestArray2, (short)0);
        assertArrayEquals(new short[]{0}, result6);
    }

    // Unit Test
    @Test
    public void testAppendObjectArrays() {
        // Test case 1: Append Long arrays
        Long[] a1 = new Long[]{1L, 2L};
        Long[] a2 = new Long[]{3L, 4L};
        Long[] result1 = ArrayUtil.append(a1, a2);
        assertArrayEquals(new Long[]{1L, 2L, 3L, 4L}, result1);

        // Test case 2: Append String arrays
        String[] s1 = new String[]{"Hello", "World"};
        String[] s2 = new String[]{"Java", "Programming"};
        String[] result2 = ArrayUtil.append(s1, s2);
        assertArrayEquals(new String[]{"Hello", "World", "Java", "Programming"}, result2);

        // Test case 3: Append multiple arrays
        Integer[] i1 = new Integer[]{1, 2};
        Integer[] i2 = new Integer[]{3, 4};
        Integer[] i3 = new Integer[]{5, 6};
        Integer[] result3 = ArrayUtil.append(i1, i2, i3);
        assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6}, result3);

        // Test case 4: Append arrays with mixed types
        Object[] o1 = new Object[]{"String", 42, 3.14};
        Object[] o2 = new Object[]{true, "Another"};
        Object[] result4 = ArrayUtil.append(o1, o2);
        assertArrayEquals(new Object[]{"String", 42, 3.14, true, "Another"}, result4);

        // Test case 5: Append empty arrays
        Double[] d1 = new Double[]{};
        Double[] d2 = new Double[]{};
        Double[] result5 = ArrayUtil.append(d1, d2);
        assertArrayEquals(new Double[]{}, result5);

        // Test case 6: Append custom object arrays
        class Person {
            final String name;
            Person(String name) { this.name = name; }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Person person = (Person) obj;
                return Objects.equals(name, person.name);
            }
        }

        Person[] p1 = new Person[]{new Person("Alice"), new Person("Bob")};
        Person[] p2 = new Person[]{new Person("Charlie")};
        Person[] result6 = ArrayUtil.append(p1, p2);
        assertArrayEquals(new Person[]{new Person("Alice"), new Person("Bob"), new Person("Charlie")}, result6);
    }

    @Test
    public void testAppendGenericObject() {
        // Test with Long array
        Long[] longArray = new Long[]{1L, 2L};
        Long[] resultLong = ArrayUtil.append(longArray, 3L);
        assertArrayEquals(new Long[]{1L, 2L, 3L}, resultLong);

        // Test with String array
        String[] stringArray = new String[]{"Hello", "World"};
        String[] resultString = ArrayUtil.append(stringArray, "Java");
        assertArrayEquals(new String[]{"Hello", "World", "Java"}, resultString);

        // Test with Integer array
        Integer[] intArray = new Integer[]{1, 2, 3};
        Integer[] resultInt = ArrayUtil.append(intArray, 4);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, resultInt);

        // Test with empty array
        Double[] emptyArray = new Double[]{};
        Double[] resultEmpty = ArrayUtil.append(emptyArray, 1.0);
        assertArrayEquals(new Double[]{1.0}, resultEmpty);

        // Test with custom object
        class Person {
            final String name;
            Person(String name) { this.name = name; }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Person person = (Person) obj;
                return Objects.equals(name, person.name);
            }
        }

        Person[] personArray = new Person[]{new Person("Alice")};
        Person[] resultPerson = ArrayUtil.append(personArray, new Person("Bob"));
        assertArrayEquals(
                new Person[]{new Person("Alice"), new Person("Bob")},
                resultPerson
        );
    }

    @Test
    public void testAppendTwoGenericArrays() {
        // Test with Long arrays
        Long[] longArray1 = new Long[]{1L, 2L};
        Long[] longArray2 = new Long[]{3L, 4L};
        Long[] resultLong = ArrayUtil.append(longArray1, longArray2);
        assertArrayEquals(new Long[]{1L, 2L, 3L, 4L}, resultLong);

        // Test with String arrays
        String[] stringArray1 = new String[]{"Hello", "World"};
        String[] stringArray2 = new String[]{"Java", "Programming"};
        String[] resultString = ArrayUtil.append(stringArray1, stringArray2);
        assertArrayEquals(new String[]{"Hello", "World", "Java", "Programming"}, resultString);

        // Test with Integer arrays
        Integer[] intArray1 = new Integer[]{1, 2};
        Integer[] intArray2 = new Integer[]{3, 4};
        Integer[] resultInt = ArrayUtil.append(intArray1, intArray2);
        assertArrayEquals(new Integer[]{1, 2, 3, 4}, resultInt);

        // Test with one empty array
        Double[] doubleArray1 = new Double[]{1.0, 2.0};
        Double[] doubleArray2 = new Double[]{};
        Double[] resultDoubleWithEmpty = ArrayUtil.append(doubleArray1, doubleArray2);
        assertArrayEquals(new Double[]{1.0, 2.0}, resultDoubleWithEmpty);

        // Test with two empty arrays
        Float[] floatArray1 = new Float[]{};
        Float[] floatArray2 = new Float[]{};
        Float[] resultEmptyArrays = ArrayUtil.append(floatArray1, floatArray2);
        assertArrayEquals(new Float[]{}, resultEmptyArrays);

        // Test with custom objects
        class Person {
            final String name;
            Person(String name) { this.name = name; }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Person person = (Person) obj;
                return Objects.equals(name, person.name);
            }
        }

        Person[] personArray1 = new Person[]{new Person("Alice")};
        Person[] personArray2 = new Person[]{new Person("Bob")};
        Person[] resultPerson = ArrayUtil.append(personArray1, personArray2);
        assertArrayEquals(
                new Person[]{new Person("Alice"), new Person("Bob")},
                resultPerson
        );
    }

    @Test
    public void testAppendToTwoDimensionalArray() {
        // Test with Long arrays
        Long[] a1 = new Long[]{1L, 2L};
        Long[] a2 = new Long[]{3L, 4L};
        Long[] a3 = new Long[]{5L, 6L};

        Long[][] initialArray = new Long[][]{a1, a2};
        Long[][] resultLong = ArrayUtil.append(initialArray, a3);

        assertArrayEquals(new Long[][]{a1, a2, a3}, resultLong);

        // Test with String arrays
        String[] s1 = new String[]{"Hello", "World"};
        String[] s2 = new String[]{"Java", "Programming"};
        String[] s3 = new String[]{"OpenAI", "ChatGPT"};

        String[][] initialStringArray = new String[][]{s1, s2};
        String[][] resultString = ArrayUtil.append(initialStringArray, s3);

        assertArrayEquals(new String[][]{s1, s2, s3}, resultString);

        // Test with Integer arrays
        Integer[] i1 = new Integer[]{1, 2};
        Integer[] i2 = new Integer[]{3, 4};
        Integer[] i3 = new Integer[]{5, 6};

        Integer[][] initialIntArray = new Integer[][]{i1, i2};
        Integer[][] resultInt = ArrayUtil.append(initialIntArray, i3);

        assertArrayEquals(new Integer[][]{i1, i2, i3}, resultInt);

        // Test with empty arrays
        Double[] d1 = new Double[]{1.0, 2.0};
        Double[][] emptyArray = new Double[][]{};
        Double[][] resultEmpty = ArrayUtil.append(emptyArray, d1);

        assertArrayEquals(new Double[][]{d1}, resultEmpty);

        // Test with custom objects
        class Person {
            final String name;
            Person(String name) { this.name = name; }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Person person = (Person) obj;
                return Objects.equals(name, person.name);
            }
        }

        Person[] p1 = new Person[]{new Person("Alice")};
        Person[] p2 = new Person[]{new Person("Bob")};

        Person[][] personArray = new Person[][]{p1};
        Person[][] resultPerson = ArrayUtil.append(personArray, p2);

        assertArrayEquals(new Person[][]{p1, p2}, resultPerson);
    }


    // Unit Test
    @Test
    public void testAppendTwoDimensionalArrays() {
        // Test with Long arrays
        Long[] a1 = new Long[]{1L, 2L};
        Long[] a2 = new Long[]{3L, 4L};
        Long[] a3 = new Long[]{5L, 6L};
        Long[] a4 = new Long[]{7L, 8L};

        Long[][] aa1 = new Long[][]{a1, a2};
        Long[][] aa2 = new Long[][]{a3, a4};
        Long[][] resultLong = ArrayUtil.append(aa1, aa2);

        assertArrayEquals(new Long[][]{a1, a2, a3, a4}, resultLong);

        // Test with String arrays
        String[] s1 = new String[]{"Hello", "World"};
        String[] s2 = new String[]{"Java", "Programming"};
        String[] s3 = new String[]{"OpenAI", "ChatGPT"};
        String[] s4 = new String[]{"GitHub", "Code"};

        String[][] ss1 = new String[][]{s1, s2};
        String[][] ss2 = new String[][]{s3, s4};
        String[][] resultString = ArrayUtil.append(ss1, ss2);

        assertArrayEquals(new String[][]{s1, s2, s3, s4}, resultString);

        // Test with Integer arrays
        Integer[] i1 = new Integer[]{1, 2};
        Integer[] i2 = new Integer[]{3, 4};
        Integer[] i3 = new Integer[]{5, 6};
        Integer[] i4 = new Integer[]{7, 8};

        Integer[][] ii1 = new Integer[][]{i1, i2};
        Integer[][] ii2 = new Integer[][]{i3, i4};
        Integer[][] resultInt = ArrayUtil.append(ii1, ii2);

        assertArrayEquals(new Integer[][]{i1, i2, i3, i4}, resultInt);

        // Test with empty arrays
        Double[][] emptyArray1 = new Double[][]{};
        Double[][] emptyArray2 = new Double[][]{};
        Double[][] resultEmpty = ArrayUtil.append(emptyArray1, emptyArray2);

        assertArrayEquals(new Double[][]{}, resultEmpty);

        // Test with mixed empty and non-empty arrays
        Float[] f1 = new Float[]{1.0f, 2.0f};
        Float[][] mixedArray1 = new Float[][]{f1};
        Float[][] mixedArray2 = new Float[][]{};
        Float[][] resultMixed = ArrayUtil.append(mixedArray1, mixedArray2);

        assertArrayEquals(new Float[][]{f1}, resultMixed);

        // Test with custom objects
        class Person {
            final String name;
            Person(String name) { this.name = name; }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;
                Person person = (Person) obj;
                return Objects.equals(name, person.name);
            }
        }

        Person[] p1 = new Person[]{new Person("Alice")};
        Person[] p2 = new Person[]{new Person("Bob")};
        Person[] p3 = new Person[]{new Person("Charlie")};

        Person[][] personArray1 = new Person[][]{p1, p2};
        Person[][] personArray2 = new Person[][]{p3};
        Person[][] resultPerson = ArrayUtil.append(personArray1, personArray2);

        assertArrayEquals(new Person[][]{p1, p2, p3}, resultPerson);
    }

    @Test
    public void testCloneBooleanArray() {
        // Test with a non-empty array containing true and false values
        boolean[] originalArray = new boolean[]{true, false, true, true, false};
        boolean[] clonedArray = ArrayUtil.clone(originalArray);

        // Check that the cloned array has the same length
        assertEquals(originalArray.length, clonedArray.length);

        // Check that the cloned array has the same elements
        assertArrayEquals(originalArray, clonedArray);

        // Test with an array containing only true values
        boolean[] trueArray = new boolean[]{true, true, true};
        boolean[] clonedTrueArray = ArrayUtil.clone(trueArray);

        assertArrayEquals(trueArray, clonedTrueArray);

        // Test with an array containing only false values
        boolean[] falseArray = new boolean[]{false, false, false};
        boolean[] clonedFalseArray = ArrayUtil.clone(falseArray);

        assertArrayEquals(falseArray, clonedFalseArray);

        // Test with an empty array
        boolean[] emptyArray = new boolean[]{};
        boolean[] clonedEmptyArray = ArrayUtil.clone(emptyArray);

        assertArrayEquals(emptyArray, clonedEmptyArray);

        // Verify that modifying the cloned array does not affect the original array
        boolean[] originalModifyArray = new boolean[]{true, false};
        boolean[] modifiedClonedArray = ArrayUtil.clone(originalModifyArray);
        modifiedClonedArray[0] = false;

        assertNotSame(originalModifyArray, modifiedClonedArray);
        assertNotEquals(originalModifyArray[0], modifiedClonedArray[0]);
    }

    @Test
    public void testCloneBooleanArrayWithRange() {
        // Test normal range extraction
        boolean[] originalArray = new boolean[]{true, false, true, true, false, true};
        boolean[] clonedArray = ArrayUtil.clone(originalArray, 2, 4);
        assertArrayEquals(new boolean[]{true, true}, clonedArray);

        // Test extracting from the beginning of the array
        boolean[] startArray = new boolean[]{true, false, true, false};
        boolean[] clonedStartArray = ArrayUtil.clone(startArray, 0, 2);
        assertArrayEquals(new boolean[]{true, false}, clonedStartArray);

        // Test extracting to the end of the array
        boolean[] endArray = new boolean[]{true, false, true, false};
        boolean[] clonedEndArray = ArrayUtil.clone(endArray, 2, 4);
        assertArrayEquals(new boolean[]{true, false}, clonedEndArray);

        // Test with an array of all true values
        boolean[] trueArray = new boolean[]{true, true, true, true};
        boolean[] clonedTrueArray = ArrayUtil.clone(trueArray, 1, 3);
        assertArrayEquals(new boolean[]{true, true}, clonedTrueArray);

        // Test with an array of all false values
        boolean[] falseArray = new boolean[]{false, false, false, false};
        boolean[] clonedFalseArray = ArrayUtil.clone(falseArray, 1, 3);
        assertArrayEquals(new boolean[]{false, false}, clonedFalseArray);

        // Test with a mixed array
        boolean[] mixedArray = new boolean[]{true, false, true, false, true};
        boolean[] clonedMixedArray = ArrayUtil.clone(mixedArray, 1, 4);
        assertArrayEquals(new boolean[]{false, true, false}, clonedMixedArray);

        // Boundary test: full array clone
        boolean[] fullArray = new boolean[]{true, false, true};
        boolean[] clonedFullArray = ArrayUtil.clone(fullArray, 0, fullArray.length);
        assertArrayEquals(fullArray, clonedFullArray);
    }

    @Test
    public void testCloneBooleanArrayWithInvalidRange() {
        boolean[] array = new boolean[]{true, false, true};

        // Test negative from index
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayUtil.clone(array, -1, 3));

        // Test from index greater than to index
        assertThrows(NegativeArraySizeException.class, () -> ArrayUtil.clone(array, 2, 1));
    }


}
