/*
 * How the implementation prevents double booking:
 * 
 * The implementation uses a light version of readers-writers. 
 * A getSeats() method is considered to be a read, and bookSeats() considered to be write.
 * Therefore, getSeats() waits when there are no writers. We use the boolean writer to do this check.
 * And, bookSeats() waits when there are any reader or writer. We use a integer to keep track
 * of the number of readers.
 * 
 * Furtheremore, since there it is possible that a User attempting to book the seats can choose
 * seats that were booked by another User, we check to make sure all bookings can happen before 
 * writing the booking. This is a all-or-nothing transaction.
 */


public class BookingClerk implements Runnable{
	final int READ_TURN = 0;
	final int WRITE_TURN = 1;
	
	//private variables
	private boolean [] seats;
	int readers;
	boolean writer;	
	
	//methods
	public BookingClerk(int capacity){
		seats = new boolean[capacity];
		for(int i = 0; i < capacity; i++){
			seats[i] = false;
		}
		readers = 0;
		writer = false;
	}
	
	@Override
	public void run(){
		//passive object
	} 
	
	/*
	 * getSeats() wait() when somebody is writing to our seats[] array.
	 */
	public synchronized boolean[] getSeats() throws InterruptedException{
		while(writer == true ) wait(); 
		readers++;
		boolean [] toReturn = seats;
		readers--;
		notifyAll();
		return toReturn;
	}
	
	/*
	 * bookSeats() wait() as long as there are readers/writers. It also checks to make 
	 * sure that ALL seats that are requested to be booked, can actually be booked. 
	 * A false is returned if the above condition does not hold, and no bookings are made.
	 * Otherwise, all the bookings are made and true is returned.
	 */
	public synchronized boolean bookSeats(int [] seatsBooking) throws InterruptedException{
		while(writer == true && readers > 0 ) wait();
		
		writer=true;
		System.out.println("entered write");
		//check first if seats are available now
		for(int i = 0; i < seatsBooking.length; i++){
			System.out.println("checking seat " + seatsBooking[i]);
			if(seats[seatsBooking[i]] == true)	{//seats are taken
				System.out.println("seat " + seatsBooking[i] + " taken");
				writer=false;
				notifyAll();
				return false;
			}
		}
		
		//seats are available, let's book
		for(int i = 0; i < seatsBooking.length; i++){
			System.out.println("booking seat number " + seatsBooking[i]);
			seats[seatsBooking[i]]= true;
		}
		System.out.println("leaving write");
		writer=false;
		notifyAll();
		
		return true;
	}
	
	
	/*
	 * Method to print seats. It waits on writer==true so that nobody is writing to it
	 * at the moment.
	 */
	public synchronized void printSeats() throws InterruptedException{
		while(writer == true ) wait();
		readers++;
		for(int i = 0; i < seats.length; i++){
			System.out.println("seat number " + i + ": " + seats[i]);
		}
		readers--;
		notifyAll();
	}
}
