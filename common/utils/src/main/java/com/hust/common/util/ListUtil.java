package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.list.UnmodifiableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author linhlh2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ListUtil {

	public static <T> List<T> copy(List<T> master) {
		if (master == null) {
			return Collections.emptyList();
		}

		return new ArrayList<>(master);
	}

	public static <T> void copy(List<T> master, List<T> copy) {
		if ((master == null) || (copy == null)) {
			return;
		}

		copy.clear();

		copy.addAll(master);
	}

	public static <T> void distinct(List<T> list) {
		distinct(list, null);
	}

	public static <T> void distinct(List<T> list, Comparator<T> comparator) {
		if (Validator.isNull(list)) {
			return;
		}

		Set<T> set;

		if (comparator == null) {
			set = new TreeSet<>();
		} else {
			set = new TreeSet<>(comparator);
		}

		Iterator<T> itr = list.iterator();

		while (itr.hasNext()) {
			T obj = itr.next();

			if (set.contains(obj)) {
				itr.remove();
			} else {
				set.add(obj);
			}
		}
	}

	public static <T> List<T> fromArray(T[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<T> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static <T> List<T> fromCollection(Collection<T> c) {
		if (Objects.nonNull(c) && (c instanceof List)) {
			return (List<T>) c;
		}

		if (Validator.isNull(c)) {
			return Collections.emptyList();
		}

		List<T> list = new ArrayList<>(c.size());

		list.addAll(c);

		return list;
	}

	public static <T> List<T> fromEnumeration(Enumeration<T> enu) {
		List<T> list = new ArrayList<>();

		while (enu.hasMoreElements()) {
			T obj = enu.nextElement();

			list.add(obj);
		}

		return list;
	}

    public static List<String> fromFile(File file) throws IOException {
        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String s;

            while ((s = br.readLine()) != null) {
                list.add(s);
            }
        }

        return list;
    }

	public static List<String> fromFile(String fileName) throws IOException {
		return fromFile(new File(fileName));
	}

	public static List<String> fromString(String s) {
		return fromArray(StringUtil.split(s, StringPool.NEW_LINE));
	}

	public static <T> List<T> sort(List<T> list) {
		return sort(list, null);
	}

	public static <T> List<T> sort(List<T> list, Comparator<T> comparator) {
		if (list instanceof UnmodifiableList<T>) {
			list = copy(list);
		}

		list.sort(comparator);

		return list;
	}

	public static <T> List<T> subList(List<T> list, int start, int end) {
		List<T> newList = new ArrayList<>();

		int normalizedSize = list.size() - 1;

		if ((start < 0) || (start > normalizedSize) || (end < 0) || (start > end)) {

			return newList;
		}

		for (int i = start; i < end && i <= normalizedSize; i++) {
			newList.add(list.get(i));
		}

		return newList;
	}

	public static List<Boolean> toList(boolean[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Boolean> list = new ArrayList<>(array.length);

		for (boolean value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Boolean> toList(Boolean[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Boolean> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<Double> toList(double[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Double> list = new ArrayList<>(array.length);

		for (double value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Double> toList(Double[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Double> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<Float> toList(float[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Float> list = new ArrayList<>(array.length);

		for (float value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Float> toList(Float[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Float> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<Integer> toList(int[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Integer> list = new ArrayList<>(array.length);

		for (int value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Integer> toList(Integer[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Integer> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<Long> toList(long[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Long> list = new ArrayList<>(array.length);

		for (long value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Long> toList(Long[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Long> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<Short> toList(short[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Short> list = new ArrayList<>(array.length);

		for (short value : array) {
			list.add(value);
		}

		return list;
	}

	public static List<Short> toList(Short[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<Short> list = new ArrayList<>(array.length);

		Collections.addAll(list, array);

		return list;
	}

	public static List<String> toList(String[] array) {
		if ((array == null) || (array.length == 0)) {
			return Collections.emptyList();
		}

		List<String> list = new ArrayList<>(array.length);

		list.addAll(Arrays.asList(array));

		return list;
	}
}
