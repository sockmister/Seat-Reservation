
public class Driver {
	public static void main(String [] args){
		final int CAPACITY = 500;
		final int USERS_NO = 200;
		
		BookingClerk bc = new BookingClerk(CAPACITY);
		System.out.println("starting clerk thread...");
		new Thread(bc).start();
		
		User[]users = new User[USERS_NO];
		for(int i = 0; i < users.length; i++){
			users[i] = new User(bc);
			System.out.println("starting user " + i + " thread...");
			new Thread(users[i]).start();
		}
		
		try{
			Thread.sleep(1000);
			System.out.println("printing..");
			bc.printSeats();
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
