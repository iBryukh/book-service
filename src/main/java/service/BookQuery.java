package service;

public class BookQuery {
	private String field;
	private int type;
	private int limit;
	private int offset;
	private BookQuery(){}
	
	public BookQuery(int limit, int offset){
		this.limit = limit;
		this.offset = offset;
	}
	
	public String getField() {
		return field;
	}
	public int getLimit() {
		return limit;
	}
	public int getOffset() {
		return offset;
	}

	public int getType() {
		return type;
	}
}
