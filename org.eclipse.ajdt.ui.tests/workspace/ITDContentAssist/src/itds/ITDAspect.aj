package itds;
import java.util.ArrayList;
import java.util.List;
import hasitds.HasITDs;



public aspect ITDAspect {
	public List<String> HasITDs.list;
	public java.util.List<String> HasITDs.makeList(int size) {
		return new ArrayList<String>();
	} 
	public HasITDs.new(int x) {
		this();
		list = new ArrayList<String>(); 
	} 
	 
	public static class Super {
		public int inside =9;
	}
	
	public int Super.value = 7;
	
	declare parents : HasITDs extends Super;
	
	public static void main(String[] args) {
	    args.length;
    }
	
	void around() : execution (void ITDAspect.main(..)) {
	    thisJoinPoint.getArgs();
	}
}