package atarigoserver;


import java.util.Scanner;

public class HumanPlayer extends Player {
	private static final long serialVersionUID = 1L;

	HumanPlayer(String newname) {
		super(newname);
	}

	Pair<Integer,Integer> generatemove(int limit) {
		Scanner citire=new Scanner(System.in);
		Integer line;
		Integer column;
		Pair<Integer,Integer> nextmove;
		while(true){
			String option;
			System.out.print("Introdu linia: ");
			option=citire.next();
			if(option.matches("0|([1-9][0-9]*)"))
				line=Integer.parseInt(option);
			else{
				line=-1;
			}
			if(line<0 || line>limit)
				System.out.println("Valoare nerecunoscuta! Mai incearca o data!");
			else
				break;
		}
		if(line!=0) {
			while(true){
				String option;
				System.out.print("Introdu coloana: ");
				option=citire.next();
				if(option.matches("0|([1-9][0-9]*)"))
					column=Integer.parseInt(option);
				else{
					column=-1;
				}
				if(column<0 || column>limit)
					System.out.println("Valoare nerecunoscuta! Mai incearca o data!");
				else
					break;
			}
		} else {
			column=0;
		}
		nextmove=new Pair<Integer,Integer>(line,column);
		return nextmove;
	}

}
