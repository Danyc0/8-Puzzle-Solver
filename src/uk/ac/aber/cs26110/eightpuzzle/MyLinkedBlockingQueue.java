package uk.ac.aber.cs26110.eightpuzzle;

import java.util.concurrent.LinkedBlockingQueue;

public class MyLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {
	public void push(E item){
		try {
			put(item);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public E pop(){
		return poll();
	}
}
