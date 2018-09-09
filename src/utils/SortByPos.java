package utils;

import java.util.Comparator;

import elements.Connection;

public class SortByPos implements Comparator<Connection> {

	@Override
	public int compare(Connection o1, Connection o2) {
		return o1.getPosition() - o2.getPosition();
	}

}
