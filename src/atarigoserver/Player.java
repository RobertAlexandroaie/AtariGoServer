package atarigoserver;

import java.io.Serializable;

abstract public class Player implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	Player(String newname){
		name=newname;
	}
	abstract Pair<Integer,Integer> generatemove(int limit);
	
}
