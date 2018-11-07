import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.lang.management.*; 
import javax.management.*;

/*
	This class is used to test different components of a JVM: threads, beans, and garbage collection.


	JVMTest usage:

	***NOTE: All times specified in seconds***

        Threads:
                To spawn <numThreads> threads for <waitTime> seconds and then return:
                        java JVMTest thread <waitTime> <numThreads>

                To iteratively spawn <numThreads> threads and then stop <numThreadsDel> threads with <waitTime> seconds in between, looping <iterationCnt> times:
                        java JVMTest thread <waitTime> <numThreads> <numThreadsDel> <iterationCnt>

                To force a deadlock between 2 threads for <waitTime> seconds:
                        java JVMTest thread deadlock <waitTime>

                To create 6 threads each respectively having a different state (NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED) and then waiting <waitTime>:
                        java JVMTest thread states <waitTime>

        Beans:
                To create <numBeans> bean objects for <waitTime> seconds and then return:
                        java JVMTest bean <waitTime> <numBeans>

        Garbage Collection:
                To wait <waitTime> seconds, create <numObjs> anonymous objects, wait <waitTime> seconds and then request garbage collection:
                        java JVMTest gc <waitTime> <numObjs> force

                To wait <waitTime> seconds, create <numObjs> anonymous objects, wait <waitTime> seconds and hope that the garbage collector runs on its own:
                        java JVMTest gc <waitTime> <numObjs>

        Heap:
                To create an iteratively growing linked list, with 1,000,000 objects being added and then waiting for <waitTime> seconds every iteration:
                        java JVMTest heap <waitTime>

                To create an iteratively growing linked list, with 1,000,000 objects being added and then waiting for <waitTime> seconds every iteration,
                stopping when used memory is within <stopVal> MB of total memory and then waiting for <waitTime2> seconds:
                        java JVMTest heap <waitTime> stop <stopVal> <waitTime2>
*/

public class JVMTest {

//***   CLASSES   ***

	// thread class used to test the spawning / deletion of threads
	static class TestThread extends Thread {

		// determines if this is the iterative version of method where threads are started/interrupted in a loop
		boolean dynamic;

		int sleepTime;

		TestThread(boolean dynamic, int sleepTime){
			this.dynamic = dynamic;
			this.sleepTime = sleepTime;
		}

		// main thread method; if not iterative the thread will sleep then return, else it will loop until interrupted
		public void run(){
			System.out.println("Thread " + this + " started");
			try{
				if(!dynamic){
					this.sleep(sleepTime * 1000);
				}
				else{
					while(true){
						this.sleep(1000);
						if(this.interrupted()){
							System.out.println("Thread interrupted");
							break;
						}
					}
				}
			}
			catch(InterruptedException interr){
				System.out.println("Thread interrupted");
			}			
		}
	}

	// thread class used to create a deadlock
	static class DeadlockThread extends Thread {
		Integer resource1;
		Integer resource2;

		DeadlockThread(Integer resource1, Integer resource2){
			this.resource1 = resource1;
			this.resource2 = resource2;
		}

		// main thread method; both threads will get lock on opposite resource and then wait indefinitely
		public void run(){
			synchronized(resource1){
				System.out.println(this + " got lock on " + resource1);

				try{
					this.sleep(1000);
					System.out.println(this + " waiting on " + resource2);
				}
				catch(InterruptedException interr){
					System.out.println("Thread interrupted");
				}

				synchronized(resource2){
					System.out.println(this + " got lock on " + resource2);
				}			
			}
		}
	}

	// thread class used to put a thread in WAITING state
	static class WaitingThread extends Thread {
		Thread threadToWaitOn;

		WaitingThread(Thread threadToWaitOn){
			this.threadToWaitOn = threadToWaitOn;
		}

		// main thread method; thread will indefinitely wait on other thread
		public void run(){
			synchronized(threadToWaitOn){
				try{
					threadToWaitOn.wait();
				}
				catch(InterruptedException interr){
					System.out.println("Thread interrupted");
				}
			}
		}
	}

	// thread class used to put a thread in RUNNABLE state
	static class RunnableThread extends Thread {

		// main thread method; thread will indefinitely run
		public void run(){
			while(true){}
		}
	}

	// generic class that adheres to Java MBean standards
	// https://docs.oracle.com/javase/tutorial/jmx/mbeans/standard.html
	public static class Test implements TestMBean { 
	    public void sayHello() { 
	        System.out.println("hello, world"); 
	    } 
	     
	    public int add(int x, int y) { 
	        return x + y; 
	    } 
	     
	    public String getName() { 
	        return this.name; 
	    }  
	     
	    public int getCacheSize() { 
	        return this.cacheSize; 
	    } 
	     
	    public synchronized void setCacheSize(int size) {	    
	        this.cacheSize = size; 
	        System.out.println("Cache size now " + this.cacheSize); 
	    } 
	     
	    private final String name = "Reginald"; 
	    private int cacheSize = DEFAULT_CACHE_SIZE; 
	    private static final int 
	        DEFAULT_CACHE_SIZE = 200; 
	}


//***   INTERFACES   ***
	// generic interace for MBean class
	// https://docs.oracle.com/javase/tutorial/jmx/mbeans/standard.html
	public interface TestMBean { 
	 
	    public void sayHello(); 
	    public int add(int x, int y); 
	    
	    public String getName(); 
	     
	    public int getCacheSize(); 
	    public void setCacheSize(int size); 
	} 


//***   UTILITY METHODS   ***

	// print out the usage of the program for help
	static void printUsage(){
		String usageString = "\nJVMTest usage: \n" +
					  "\n***NOTE: All times specified in seconds***\n" +	
					  "\n\tThreads: \n" +
					  "\t\tTo spawn <numThreads> threads for <waitTime> seconds and then return: \n" +
					  "\t\t\tjava JVMTest thread <waitTime> <numThreads>\n" +
					  "\n\t\tTo iteratively spawn <numThreads> threads and then stop <numThreadsDel> threads with <waitTime> seconds in between, looping <iterationCnt> times: \n" +
					  "\t\t\tjava JVMTest thread <waitTime> <numThreads> <numThreadsDel> <iterationCnt>\n" +
					  "\n\t\tTo force a deadlock between 2 threads for <waitTime> seconds: \n" +
					  "\t\t\tjava JVMTest thread deadlock <waitTime>\n" +
					  "\n\t\tTo create 6 threads each respectively having a different state (NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED) and then waiting <waitTime>: \n" +
					  "\t\t\tjava JVMTest thread states <waitTime>\n" +
					  "\n\tBeans: \n" +
					  "\t\tTo create <numBeans> bean objects for <waitTime> seconds and then return: \n" +
					  "\t\t\tjava JVMTest bean <waitTime> <numBeans>\n" +
					  "\n\tGarbage Collection: \n" +
					  "\t\tTo wait <waitTime> seconds, create <numObjs> anonymous objects, wait <waitTime> seconds and then request garbage collection: \n" +
					  "\t\t\tjava JVMTest gc <waitTime> <numObjs> force\n" +
					  "\n\t\tTo wait <waitTime> seconds, create <numObjs> anonymous objects, wait <waitTime> seconds and hope that the garbage collector runs on its own: \n" +
					  "\t\t\tjava JVMTest gc <waitTime> <numObjs>\n" +
					  "\n\tHeap: \n" +
					  "\t\tTo create an iteratively growing linked list, with 1,000,000 objects being added and then waiting for <waitTime> seconds every iteration: \n" +
					  "\t\t\tjava JVMTest heap <waitTime>\n" +
					  "\n\t\tTo create an iteratively growing linked list, with 1,000,000 objects being added and then waiting for <waitTime> seconds every iteration, \n" + 
        			  "\t\tstopping when used memory is within <stopVal> MB of total memory and then waiting for <waitTime2> seconds: \n" +
					  "\t\t\tjava JVMTest heap <waitTime> stop <stopVal> <waitTime2>\n";

		System.out.print(usageString);
		System.exit(1);
	}

	// parse the arguments passed and call the correct tester method
	static void parseArgs(String[] args){
		//parse other args
		switch(args[0].toLowerCase()){
			case "thread":
				testThreads(args);
				break;
			case "gc":
				testGC(args);
				break;
			case "bean":
				testBean(args);
				break;
			case "heap":
				testHeap(args);
				break;
			case "help":
				printUsage();
				break;
			default:
				System.out.println("Invalid arg specified: " + args[0]);
				printUsage();
				break;
		}
	}

	// stop processing of program for specified amount of time
	static void sleep(int sleepTime){
		try{
   			System.out.println("Going to sleep for " + sleepTime + " seconds...");
			TimeUnit.SECONDS.sleep(sleepTime);
   			System.out.println("Waking up...");
		}
		catch(InterruptedException interr){
			System.out.println("Sleep interrupted");
		}			
	}

	static void printMemoryStats(){
		int mb = 1024*1024;
		
		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		
		System.out.println("##### Heap utilization statistics [MB] #####");
		
		//Print used memory
		System.out.println("Used Memory:  " 
			+ (runtime.totalMemory() - runtime.freeMemory()) / mb);

		//Print free memory
		System.out.println("Free Memory:  " 
			+ runtime.freeMemory() / mb);
		
		//Print total available memory
		System.out.println("Total Memory: " + runtime.totalMemory() / mb);

		//Print Maximum available memory
		System.out.println("Max Memory:   " + runtime.maxMemory() / mb);
	}


//***   THREAD TEST METHODS   ***

	// main processing method of thread testing; handles creation / interruption of threads
	static void spawnThreads(int sleepTime, int numThreads, int numThreadsDel, int iterationCnt){
		System.out.println("Starting to spawn " + numThreads + " threads");
		
		ArrayList<TestThread> threadContainer = new ArrayList<TestThread>();

		// if non-iterative test specified, this loop will only be executed once
		for(int i = 0; i < iterationCnt; i++){

			// create new threads, store them in the container, and then start them
			for(int j = 0; j < numThreads; j++){
				threadContainer.add(new TestThread(numThreadsDel != -1, sleepTime));
				threadContainer.get(threadContainer.size() - 1).start();	//start the most recent thread
			}

			System.out.println("Current number of threads: " + threadContainer.size());

			// make sure to sleep before interrupting threads
			if(numThreadsDel != -1){
				System.out.println("Sleeping for " + sleepTime + " seconds before interrupting");
				sleep(sleepTime);
			}

			// interrupt (stop) the specified number of threads
			// if non-iterative test specified, numThreadsDel will be 0
			for(int j = 0; j < numThreadsDel; j++){
				threadContainer.get(threadContainer.size() - 1).interrupt();
				threadContainer.remove(threadContainer.size() - 1);

			}

   			if(numThreadsDel != -1){
				System.out.println("Current number of threads: " + threadContainer.size());
   			}

			sleep(sleepTime);
		}
	}

	// create a deadlock between 2 threads for specified amount of time
	static void forceDeadlock(int lockTime){
		System.out.println("Forcing deadlock for " + lockTime + " seconds...");

		Integer resource1 = new Integer(1);
		Integer resource2 = new Integer(2);

		new DeadlockThread(resource1, resource2).start();
		new DeadlockThread(resource2, resource1).start();

		sleep(lockTime);
	}

	// create 6 threads, each representing a different state
	static void spawnThreadsAllStates(int sleepTime){
		// new
		TestThread newThread = new TestThread(false, 0);
		newThread.setName("JVMTest-NEW_THREAD");

		// runnable
		RunnableThread runnableThread = new RunnableThread();
		runnableThread.setName("JVMTest-RUNNABLE_THREAD");
		runnableThread.start();

		// blocked
		Integer resource1 = new Integer(1);
		Integer resource2 = new Integer(2);

		DeadlockThread deadlockThread1 = new DeadlockThread(resource1, resource2);
		DeadlockThread deadlockThread2 = new DeadlockThread(resource2, resource1);
		deadlockThread1.setName("JVMTest-BLOCKED1_THREAD");
		deadlockThread2.setName("JVMTest-BLOCKED2_THREAD");

		deadlockThread1.start();
		deadlockThread2.start();

		// waiting
		WaitingThread waitingThread = new WaitingThread(newThread);
		waitingThread.setName("JVMTest-WAITING_THREAD");
		waitingThread.start();

		// timed_waiting
		TestThread timedWaitingThread = new TestThread(true, 0);
		timedWaitingThread.setName("JVMTest-TIMED_WAITING_THREAD");
		timedWaitingThread.start();

		// terminated
		TestThread terminatedThread = new TestThread(false, 0);
		newThread.setName("JVMTest-TERMINATED_THREAD");
		terminatedThread.start();

		// give some time to make sure all threads are in correct state
		sleep(2);

		System.out.println("NEW: " + newThread.getState());
		System.out.println("RUNNABLE: " + runnableThread.getState());
		System.out.println("BLOCKED: " + deadlockThread1.getState());
		System.out.println("BLOCKED: " + deadlockThread2.getState());
		System.out.println("WAITING: " + waitingThread.getState());
		System.out.println("TIMED_WAITING: " + timedWaitingThread.getState());
		System.out.println("TERMINATED: " + terminatedThread.getState());

		sleep(sleepTime);
	}

	// main thread container method for extracting arguments and calling correct thread test methods
	static void testThreads(String[] args){
		if(args[1].toLowerCase().equals("deadlock")){
			try{
				forceDeadlock(Integer.parseInt(args[2]));
			}
			catch(ArrayIndexOutOfBoundsException numErr){
				System.out.println("Must provide time for deadlock to stay locked for");
				printUsage();
			}
			catch(NumberFormatException numErr){
				System.out.println("Must provide time for deadlock to stay locked for");
				printUsage();
			}
		}
		else if(args[1].toLowerCase().equals("states")){
			try{
				spawnThreadsAllStates(Integer.parseInt(args[2]));
			}
			catch(ArrayIndexOutOfBoundsException numErr){
				System.out.println("Must provide time for thread states to wait for");
				printUsage();
			}
			catch(NumberFormatException numErr){
				System.out.println("Must provide time for thread states to wait for");
				printUsage();
			}
		}
		else{
			int sleepTime = 1;
			int cnt = -1;
			int dcnt = -1;
			int iterationCnt = 1;

			for(int i = 1; i < args.length; i++){
				try{
					switch(i){
						case 1:
							sleepTime = Integer.parseInt(args[i]);
							break;
						case 2:
							cnt = Integer.parseInt(args[i]);
							break;
						case 3:
							dcnt = Integer.parseInt(args[i]);
							break;
						case 4:
							iterationCnt = Integer.parseInt(args[i]);
							break;
						default:
							break;
					}
				}
				catch(NumberFormatException numErr){
					System.out.println("Invalid argument to thread test: " + args[i]);
					printUsage();
				}
			}
			
			spawnThreads(sleepTime, cnt, dcnt, iterationCnt);
		}
	}


//***   GC TEST METHODS   ***

	// generate specified number of anonymous objects and request garbage collection if specified
	static void generateAndGarbageCollect(int numObjs, int sleepTime, boolean force){
		printMemoryStats();

		System.out.println("Sleeping before creating objects...");
		sleep(sleepTime);

		System.out.println("Creating " + numObjs + " 4-byte ints and storing in array");
		System.out.println("Array should be around " + 4*numObjs + " bytes");

		int[] arr = new int[numObjs];
		for(int i = 0; i < numObjs; i++){
			arr[i] = i;
		}

		System.out.println("Array filled. Sleeping before requesting GC");

		printMemoryStats();
		sleep(sleepTime);

		System.out.println("Nullifying array");
		arr = null;

		// System.gc() does not run garbage collection, it merely requests that the system runs the garbage collector; so there's no guarantee it will run
		if(force){
			System.out.println("Requesting garbage collection");
			System.gc();
		}

		sleep(sleepTime);
		printMemoryStats();

		//System.out.println("Garbage collection collected " + collected + " unreferenced objects");
	}

	// main garbage collection container method for extracting arguments and calling correct GC test methods
	static void testGC(String[] args){
		int sleepTime = 1;
		int cnt = -1;
		boolean force = false;

		for(int i = 1; i < args.length; i++){
			try{
				switch(i){
					case 1:
						sleepTime = Integer.parseInt(args[i]);
						break;
					case 2:
						cnt = Integer.parseInt(args[i]);
						break;
					case 3:
						if(!args[i].toLowerCase().equals("force"))
							throw new NumberFormatException();
						else
							force = true;
						break;
					default:
						break;
				}
			}
			catch(NumberFormatException numErr){
				System.out.println("Invalid argument to garbage collection test: " + args[i]);
				printUsage();
			}
		}

		generateAndGarbageCollect(cnt, sleepTime, force);
	}


//***   BEAN TEST METHODS   ***

	// generate specified number of bean objects
	static void generateBeans(int sleepTime, int numBeans){
		System.out.println("Creating " + numBeans + " beans ending after " + sleepTime + " seconds");

        try{
        	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
			Test mBean = new Test();

        	for(int i = 0; i < numBeans; i++){
		        ObjectName name = new ObjectName("JVMTest." + i + ".com:type=TestBean"); 
	            mbs.registerMBean(mBean, name);
			}		
        }
        catch(InstanceAlreadyExistsException instErr){
        	System.out.println(instErr);
        }
        catch(MalformedObjectNameException nameErr){
        	System.out.println(nameErr);
        }
        catch(MBeanRegistrationException regErr){
        	System.out.println(regErr);
        }
        catch(NotCompliantMBeanException complErr){
        	System.out.println(complErr);
        }

		sleep(sleepTime);
	}

	// main bean container method for extracting arguments and calling correct bean test methods
	static void testBean(String[] args){
		int sleepTime = 1;
		int cnt = -1;

		for(int i = 1; i < args.length; i++){
			try{
				switch(i){
					case 1:
						sleepTime = Integer.parseInt(args[i]);
						break;
					case 2:
						cnt = Integer.parseInt(args[i]);
						break;
					default:
						break;
				}
			}
			catch(NumberFormatException numErr){
				System.out.println("Invalid argument to bean test: " + args[i]);
				printUsage();
			}
		}

		generateBeans(sleepTime, cnt);
	}


//***   HEAP TEST METHODS   ***

	// adds elements to a link list, up to 2,147,000,000, in order to test filling up heap storage
	static void fillHeapStorage(int sleepTime){
		System.out.println("Starting heap storage test...");
		LinkedList objList = new LinkedList();

		// add 1,000,000 elements to end of linked list each of 2,147 iterations
		for(int i = 1; i <= 2147; i++){
			for(int j = 0; j < 1000000; j++){
				try{
					objList.add(new Object());
				}
				catch(OutOfMemoryError memErr){
					System.out.println("Out of memory at linked list size: " + objList.size());
					System.out.println(memErr);
					System.exit(1);
				}
			}

			System.out.println(i + ": size of linked list = " + objList.size());
			printMemoryStats();
			sleep(sleepTime);
		}

		// will probably never reach this line
		System.out.println("Heap storage test complete");
	}

	// adds elements to a link list, up to Integer.MAX_VALUE, until the used memory is within <stopVal> MB of total memory, in order to test filling up heap storage
	static void fillHeapStorageAndStopWhenClose(int sleepTime, int stopVal, int sleepTimeStop){
		System.out.println("Starting heap storage and stoppping when used memory within " + stopVal + " MB of max memory...");

		Runtime runtime = Runtime.getRuntime();
		int mb = 1024*1024;
		LinkedList objList = new LinkedList();
		boolean stop = false;

		// add <increment> elements to end of linked list each iteration until close to max heap size
		for(int i = 1; i <= Integer.MAX_VALUE; i++){
			for(int j = 0; j < 1000000; j++){
				try{
					objList.add(new Object());
					
					if( ( (runtime.maxMemory() / mb) - ((runtime.totalMemory() - runtime.freeMemory()) / mb) ) < stopVal ){
						System.out.println("Within " + stopVal + " MB of max memory, not adding any more objects");
						printMemoryStats();
						stop = true;
						break;
					}
				}
				catch(OutOfMemoryError memErr){
					System.out.println("Out of memory at linked list size: " + objList.size());
					System.out.println(memErr);
					System.exit(1);
				}
			}
			
			System.out.println(i + ": size of linked list = " + objList.size());
			printMemoryStats();
			sleep(sleepTime);

			if(stop){
				sleep(sleepTimeStop);
				break;
			}
		}

		// will probably never reach this line
		System.out.println("Heap storage test complete");
	}

	// main heap container method for extracting arguments and calling correct heap test methods
	static void testHeap(String[] args){
		int sleepTime = 1;
		boolean stop = false;
		int stopVal = 0;
		int sleepTimeStop = 1;

		for(int i = 1; i < args.length; i++){
			System.out.println(args[i]);
			try{
				switch(i){
					case 1:
						sleepTime = Integer.parseInt(args[i]);
						break;						
					case 2:
						if(!args[i].toLowerCase().equals("stop"))
							throw new NumberFormatException();
						else
							stop = true;
						break;		
					case 3:
						stopVal = Integer.parseInt(args[i]);
						break;
					case 4:
						sleepTimeStop = Integer.parseInt(args[i]);
						break;
					default:
						break;
				}
			}
			catch(NumberFormatException numErr){
				System.out.println("Invalid argument to heap test: " + args[i]);
				printUsage();
			}
		}			
		
		if(!stop){
			fillHeapStorage(sleepTime);
		}
		else{
			fillHeapStorageAndStopWhenClose(sleepTime, stopVal, sleepTimeStop);
		}
	}


//***   MAIN   ***
	public static void main(String[] args){

		// if no arguments are supplied, read from stdin for input
		if(args.length == 0){
			Scanner scanner = new Scanner(System.in);

			// temp list to hold args read in; useful as it determines the length of array we have to create
			ArrayList<String> argList = new ArrayList<String>();

			// reads in complete tokens (separated by white space)
			while(scanner.hasNext()){
				argList.add(scanner.next());
			}

			//create the new args array and add the args from argList to it
			args = new String[argList.size()];
			for(int i = 0; i < argList.size(); i++){
				args[i] = (String)argList.get(i);
			}
		}

		parseArgs(args);
  		System.out.println("Testing complete. Exiting with code 0.");
		System.exit(0);
	}
}
