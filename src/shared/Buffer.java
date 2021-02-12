package shared;
import java.util.LinkedList;

public class Buffer<T> {
	private LinkedList<T> buffer = new LinkedList<T>();
	private boolean locked = false;
	
	public void write(T obj) {
		while(locked) {};
		locked = true;
		buffer.add(obj);
		locked = false;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<T> read(){
		while(locked) {};
		locked = true;
		LinkedList<T> temp = new LinkedList<T>();
		temp = (LinkedList<T>) buffer.clone();
		buffer.clear();
		locked = false;
		return temp;
	}

}
