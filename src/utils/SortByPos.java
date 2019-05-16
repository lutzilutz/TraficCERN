package utils;

import java.util.Comparator;

import elements.Connection;

public class SortByPos implements Comparator<Connection> {

	@Override
	public int compare(Connection connection1, Connection connection2) {
		return connection1.getPosition() - connection2.getPosition();
	}

}
