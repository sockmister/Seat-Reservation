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
	
	/*
	 * In general, run() attempts to make bookings. The steps are:
	 * 	1) Get the current booking status from the clerk.
	 *  2) Based on the number of free seats, generate a random number of seats book.
	 *  3) For that number of seats to book, generate a random seat number for each seat.
	 *  4) Attempt to make the booking. If the booking fails, then we repeat Steps 1 to 3. 
	 */
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
	
	/*
	 * This is a helper method to search through the booking and find out number of free seats
	 * we can book. 
	 */
	private int getNumberOfFreeSeats(){
		int free = 0;
		for(int i = 0; i < seatDisplay.length; i++){
			if(seatDisplay[i] == false)
				free++;
		}
		
		return free;
	}
	
	/*
	 * This is a helper method to randomly assign seats based on number of seats we want to book.
	 * The seats assigned are not taken, but the information might be outdated.
	 */
	private void assignSeats(){
		int maxSeats = seatDisplay.length;
		int ranSeat;
		for(int i = 0; i < numOfSeatsToBook; i++){
			do{
				ranSeat = rnd.nextInt(maxSeats);
			} while(seatDisplay[ranSeat] == true && notExist(ranSeat));
			toBook[i] = ranSeat;
		}
	}
	
	/*
	 * This is a helper method to ensure that the randomly assigned seats was not assigned
	 * before in the assignSeats() method.
	 */
	private boolean notExist(int seat){
		for(int i = 0; i < toBook.length; ++i){
			if(toBook[i] == seat){
				return false;
			}
		}
		return true;
	}
}
