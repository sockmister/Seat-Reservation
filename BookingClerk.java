
public class BookingClerk implements Runnable{
	final int READ_TURN = 0;
	final int WRITE_TURN = 1;
	
	//private variables
	private boolean [] seats;
	int readers;
	boolean writer;
	//int turn;	
	
	//methods
	public BookingClerk(int capacity){
		seats = new boolean[capacity];
		for(int i = 0; i < capacity; i++){
			seats[i] = false;
		}
		readers = 0;
		writer = false;
		//turn = READ_TURN;
	}
	
	@Override
	public void run(){
		//passive object
	} 
	
	public synchronized boolean[] getSeats() throws InterruptedException{
		System.out.println("readers: " + readers + " writers: " + writer);// + " turn: " + turn);
		while(writer == true ) wait(); 
		readers++;
		boolean [] toReturn = seats;
		readers--;
		//if(readers == 0)
		//	turn = WRITE_TURN;
		notifyAll();
		return toReturn;
	}
	
	public synchronized boolean bookSeats(int [] seatsBooking) throws InterruptedException{
		System.out.println("readers: " + readers + " writers: " + writer);// + " turn: " + turn);
		while(writer == true && readers > 0 ) wait();
		
		writer=true;
		//check first if seats are available now
		for(int i = 0; i < seatsBooking.length; i++){
			if(seats[seatsBooking[i]] == true)	{//seats are taken
				writer=false;
				notifyAll();
				return false;
			}
		}
		
		//seats are available, let's book
		for(int i = 0; i < seatsBooking.length; i++){
			seats[seatsBooking[i]]= true;
		}
		writer=false;
		//turn = READ_TURN;
		notifyAll();
		
		return true;
	}
	
	public synchronized void printSeats() throws InterruptedException{
		while(writer == true ) wait();
		readers++;
		for(int i = 0; i < seats.length; i++){
			System.out.println("seat number " + i + ": " + seats[i]);
		}
		readers--;
		//if(readers == 0)
		//	turn = WRITE_TURN;
		notifyAll();
	}
}
