package me.voler.test;

import java.util.Arrays;

public class ThreadTest {

	/**
	 * the next position to add element
	 */
	private int next;
	private Integer[] array;

	/**
	 * 
	 * @param t
	 * 
	 * @see java.lang.ThreadGroup#add(Thread)
	 */
	public void add(Integer t) {
		if (array == null) {
			array = new Integer[4];
		} else if (next == array.length) {
			// expand capacity
			array = Arrays.copyOf(array, next * 2);
		}
		array[next] = t;
		next++;
	}

	/**
	 * 
	 * @param t
	 * 
	 * @see java.lang.ThreadGroup#remove(Thread)
	 */
	public void remove(Integer t) {
		for (int i = 0; i < next; i++) {
			// ==
			if (array[i] == t) {
				System.arraycopy(array, i + 1, array, i, --next - i);
				array[next] = null;
				break;
			}
		}
	}

}
