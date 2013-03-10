import java.util.Random;

public class User implements Runnable{
	private BookingClerk clerk;
	private boolean [] seatDisplay;
	private int numOfFreeSeats;
	private int numOfSeatsToBook;
	private int [] toBook;
	private Random rnd;
	
	public User(BookingClerk c){
		clerk = c;
		rnd = new Random();
		numOfFreeSeats = numOfSeatsToBook = 0;
	}
	
	@Override
	public void run() {
		try {
			do{
				//get display from booking clerk
				seatDisplay = clerk.getSeats();
				
				//randomly choose number of seats to book
				numOfFreeSeats = getNumberOfFreeSeats();
				if(numOfFreeSeats == 0)
					return;
				numOfSeatsToBook = rnd.nextInt(numOfFreeSeats)/2 + 1;
				
				//randomly assign seats to book
				toBook = new int[numOfSeatsToBook];
				assignSeats();
			} while(!clerk.bookSeats(toBook) && numOfFreeSeats >= 0);		
		} catch (InterruptedException ie){
			ie.printStackTrace();
		}
	}
	
	private int getNumberOfFreeSeats(){
		int free = 0;
		for(int i = 0; i < seatDisplay.length; i++){
			if(seatDisplay[i] == false)
				free++;
		}
		
		return free;
	}
	
	private void assignSeats(){
		int maxSeats = seatDisplay.length;
		int ranSeat;
		for(int i = 0; i < numOfSeatsToBook; i++){
			do{
				ranSeat = rnd.nextInt(maxSeats);
			} while(seatDisplay[ranSeat] == true);
			toBook[i] = ranSeat;
		}
	}
}
