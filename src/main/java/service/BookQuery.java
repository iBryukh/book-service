package service;

public class BookQuery {
	private String field;
	private int limit;
	private int offset;
	private BookQuery(){}
	
	public String getField() {
		return field;
	}
	public int getLimit() {
		return limit;
	}
	public int getOffset() {
		return offset;
	}
	
}
