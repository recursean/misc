import java.util.Arrays;
import java.util.Random;

public class ThirstyPersonTest{
    public static class ProducerConsumer{
	private int in = 0, sz = 3, out = 0, count = 0;
	private int[] buffer = new int[sz];

	public ProducerConsumer(){
	    for(int i = 0; i < sz; i++)
		buffer[i] = -1;
	}

	public ProducerConsumer(int s){
	    sz = s;
	    buffer = new int[sz];
	    for(int i = 0; i < sz; i ++)
		buffer[i] = -1;
	}

	private void add(int item){
	    buffer[in] = item;
	    in = (in + 1) % buffer.length;
	    System.out.println(Thread.currentThread().getName() + " adds " + item + ": " + Arrays.toString(buffer));
	}

	private int remove(){
	    int item = buffer[out];
	    buffer[out] = -1;
	    out = (out + 1) % buffer.length;
	    System.out.println(Thread.currentThread().getName() + " gets " + item + ": " + Arrays.toString(buffer));
	    return item;
	}

	private synchronized void addToBuffer(int item){
	    System.out.println(Thread.currentThread().getName() + " enters with " + item);
	    while(count == buffer.length)
		try{
		    System.out.println(Thread.currentThread().getName() + " goes to sleep");
		    wait();
		}
		catch(InterruptedException ie){
		}
	    add(item);
	    count++;
	    if(count == 1)
		notifyAll();
	}

	public synchronized int removeFromBuffer(){
	    System.out.println(Thread.currentThread().getName() + " enters");
	    while(count == 0)
		try{
		    System.out.println(Thread.currentThread().getName() + " goes to sleep");
		    wait();
		}
		catch(InterruptedException ie){
		}
	    int item = remove();
	    count--;
	    if(count == buffer.length - 1)
		notifyAll();
	    return item;
	}
    }

    public static class Producer extends Thread{
	private ProducerConsumer producerConsumer;
	public Producer(int i, ProducerConsumer pc){
	    super("producer" + i);
	    producerConsumer = pc;
	}
	
	private int produce(){
	    int item = (new Random()).nextInt(100);
	    System.out.println(getName() + " produces " + item);
	    return item;
	}
	
	public void run(){
	    while(true){
		int item = produce();
		producerConsumer.addToBuffer(item);
		try{
		    sleep(300);
		}
		catch(InterruptedException ie){
		}
	    }
	}
    }

    public static class Consumer extends Thread{
	private ProducerConsumer producerConsumer;
	public Consumer(int i, ProducerConsumer pc){
	    super("consumer" + i);
	    producerConsumer = pc;
	}

	private void consume(int item){
	    System.out.println(getName() + " consumers " + item);
	}

	public void run(){
	    while(true){
		int item = producerConsumer.removeFromBuffer();
		consume(item);
		try{
		    sleep(300);
		}
		catch(InterruptedException ie){
		}
	    }
	}
    }

    //  public class TestProducerConsumer{
	public static void main(String[] args){
	    ProducerConsumer pc = new ProducerConsumer(5);
	    for(int i = 1; i <= 3; i++)
		(new Consumer(i,pc)).start();
	    for(int i = 1; i <= 1; i++)
		(new Producer(i,pc)).start();
	    Thread a[] = new Thread[Thread.activeCount()];
	    Thread.enumerate(a);
	    System.out.println(Arrays.toString(a));
	}
    // }
}
