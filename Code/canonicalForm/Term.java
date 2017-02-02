package canonicalForm;
import java.util.ArrayList;


public class Term {
	
	private double coefficient;
	private ArrayList<Variable> variables;
	private boolean positive;
	private boolean leftSide;
	
	public Term(){
		positive = true;
		variables = new ArrayList<Variable>();
	}

	public Term(double coefficient, ArrayList<Variable> variables, int power, boolean positive) {
		this.coefficient = coefficient;
		this.variables = variables;
		this.positive = positive;
	}

	public double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public ArrayList<Variable> getVariables() {
		return variables;
	}

	public void setVariables(ArrayList<Variable> variables) {
		this.variables = variables;
	}

	public boolean isPositive() {
		return positive;
	}

	public void setPositive(boolean positive) {
		this.positive = positive;
	}
	
	public boolean isLeftSide() {
		return leftSide;
	}

	public void setLeftSide(boolean leftSide) {
		this.leftSide = leftSide;
	}

	@Override
	public String toString() {
		String term = "";
		if(coefficient == -1.0){
			term+="-";
		}else{
			if(coefficient != 1.0){
				term += coefficient;
			}
		}
		for(int i=0; i<variables.size(); i++){
			if(variables.get(i).getPower() == 1){
				term+=variables.get(i).getName();
			}else{
				term+=variables.get(i).getName()+"^"+variables.get(i).getPower();
			}

		}
		return term;
	}
	
}
