package uk.ac.aber.cs26110.eightpuzzle;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MyPriorityQueue<E> extends PriorityQueue<E> {
	MyPriorityQueue(int initialCapacity, Comparator<? super E> comparator){
		super(initialCapacity, comparator);
	}
	
	public void push(E item){
		add(item);
	}
	
	public E pop(){
		return poll();
	}
}
