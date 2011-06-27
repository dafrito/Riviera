/**
 * 
 */
package com.bluespot.parsing;

public class RuleSet<T extends Token<?>> {

	/**
	 * 
	 * @param strings
	 * @return
	 */
	public Group<T> group(String... strings) {
		return new Group<T>().start(strings);
	}

	/**
	 * 
	 * @param chars
	 * @return
	 */
	public Group<T> group(char... chars) {
		return new Group<T>().start(chars);
	}

}