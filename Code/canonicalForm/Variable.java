package canonicalForm;
public class Variable {
	
	private String name;
	private int power;
	
	public Variable(){}
	
	public Variable(String name, int power) {
		this.name = name;
		this.power = power;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}

	@Override
	public boolean equals(Object obj) {
		Variable var = (Variable) obj;
		if(this.name.equals(var.getName()) && this.power == var.getPower()){
			return true;
		}
		return false;
	}
	
	

}
