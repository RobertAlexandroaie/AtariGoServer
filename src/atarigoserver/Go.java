package atarigoserver;

public interface Go {
	
	int validate(Pair<Integer,Integer> playerMove);
	int nextMove();
	
}
