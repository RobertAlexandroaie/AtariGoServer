package atarigoserver;

public class RandomComputerPlayer extends Player {

	private static final long serialVersionUID = 1L;

	RandomComputerPlayer(String newname) {
		super(newname);
	}

        @Override
	Pair<Integer, Integer> generatemove(int limit) {
		Pair<Integer,Integer> computerMove;
		Integer line=(int) (Math.random()*(limit-1))+1;
		Integer column=(int) (Math.random()*(limit-1))+1;
		computerMove=new Pair<Integer,Integer>(line,column);
		return computerMove;
	}

}
