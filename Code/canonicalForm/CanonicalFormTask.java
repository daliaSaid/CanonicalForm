package canonicalForm;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CanonicalFormTask {

	/*
	 * Validates the format of a provided input String.
	 */
	public static boolean validInputFormat(String input) {

		String singleTermRegex = "([0-9]+(\\.[0-9]+)?)*([a-z]+(\\^[0-9]+)?)*";
		String sideOfEquationRegex = singleTermRegex + "([\\+-]"
				+ singleTermRegex + ")*";
		return input.replaceAll(" ", "").matches(
				sideOfEquationRegex + "=" + sideOfEquationRegex);
	}

	/*
	 * Makes sure that spaces are clean regardless of how the user uses spaces between terms in the equation.
	 */
	public static String processStringSpacing(String input) {
		input = input.replace(" ", "");
		input = input.replace("+", " + ");
		input = input.replace("-", " - ");
		input = input.replace("=", " = ");
		return input;
	}
	
	/*
	 * Checks that 2 terms have the same variables and thus can be added.
	 */
	public static boolean canBeAdded(Term term1, Term term2) {
		if (term1.getVariables().size() == term2.getVariables().size()) {
			for (int i = 0; i < term1.getVariables().size(); i++) {
				if (!term1.getVariables().contains(term2.getVariables().get(i))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/*
	 * Extracts the terms from an equation String.
	 */
	public static ArrayList<Term> getTerms(String input) {
		String[] splitInput = input.split(" ");
		Pattern pattern;
		Matcher matcher;
		boolean leftSide = true;
		ArrayList<Term> terms = new ArrayList<Term>();
		for (int i = 0; i < splitInput.length; i++) {
			Term term = new Term();
			pattern = Pattern.compile("[\\+\\-=]");
			matcher = pattern.matcher(splitInput[i]);
			if (matcher.find()) {
				String operator = matcher.group();
				if (operator.equals("=")) {
					leftSide = false;
				}
				if (operator.equals("+")) {
					term.setPositive(true);
				}
				if (operator.equals("-")) {
					term.setPositive(false);
				}
				if(i<splitInput.length-1){
					i++;
				}
				// move on there's no term to add
			}
			term.setLeftSide(leftSide);
			// put this in a method
			pattern = Pattern.compile("[a-z](\\^[0-9]+)?");
			matcher = pattern.matcher(splitInput[i]);
			// if there is a variable then there may or may not be a coefficient
			while (matcher.find()) {
				String foundVariable = matcher.group();
				splitInput[i] = splitInput[i].replace(foundVariable, "");
				String[] splitVariables = foundVariable.split("\\^");
				Variable variable;
				if (splitVariables.length == 1) {
					variable = new Variable(splitVariables[0], 1);
				} else {
					variable = new Variable(splitVariables[0],
							Integer.parseInt(splitVariables[1]));
				}
				term.getVariables().add(variable);
			}
			// coefficients are ready to be added (as if all were sent to the
			// left side)
			pattern = Pattern.compile("[0-9]+(\\.[0-9]+)?");
			matcher = pattern.matcher(splitInput[i]);
			if (!term.isLeftSide()) {
				term.setPositive(!term.isPositive());
			}
			if (matcher.find()) {
				Double coeff = Double.parseDouble(matcher.group());
				if (!term.isPositive()) {
					coeff *= -1;
				}
				term.setCoefficient(coeff);
			} else {

				if (!term.isPositive()) {
					term.setCoefficient(-1.0);
				} else {
					term.setCoefficient(1.0);
				}

			}
			terms.add(term);
		}
		return terms;
	}
	
	/*
	 * Processes the terms by adding like-variable terms.
	 */
	public static void processTerms(ArrayList<Term> terms) {
		for (int i = 0; i < terms.size(); i++) {
			for (int j = i + 1; j < terms.size(); j++) {
				if (canBeAdded(terms.get(i), terms.get(j))) {
					terms.get(i).setCoefficient(
							terms.get(i).getCoefficient()
									+ terms.get(j).getCoefficient());
					terms.remove(j);
					j--;
				}
			}
		}
		for (int i = 0; i < terms.size(); i++) {
			if (terms.get(i).getCoefficient() == 0) {
				terms.remove(i);
				i--;
			}
		}
	}
	
	/*
	 *  Prints the output equation from an arraylist of terms
	 */
	public static String printOutput(ArrayList<Term> terms) {
		String output = "";
		for (int i = 0; i < terms.size(); i++) {
			if (i != 0) {
				if (terms.get(i).getCoefficient() > 0) {
					output += "+";
				}
			}
			output += terms.get(i).toString();
		}
		output += "=0";
		output = processStringSpacing(output);
		System.out.println(output);
		return output;
	}
	/*
	 *  Writes an output String to the file with the name provided by outputFileName
	 */
	public static void writeToFile(String output, String outputFileName){
		try{
		    PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
		    writer.print(output);
		    writer.close();
		} catch (Exception e) {
		   System.out.println("Output File Cannot Be Created");
		}
	}
	
	/*
	 *  Asks the user to specify the means by which input is to be provided.
	 *  If the input is typed it, it is validated and if it is of a valid format an output equation is printed to the console.
	 *  If the input is to be read from a file, the file "input.txt" is read line by line and each equation in it is processed
	 *  with the results written in an output.out file.
	 */
	public static void queryUser(){
		String input = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while(true){
	        try{
	        	System.out.print("If you prefer to type the equation manually, enter 0.\n If you prefer to read from file, type 1");
	        	int choice = Integer.parseInt(br.readLine());
	        	if(choice == 0){
	        		System.out.println("Please Enter the Equation.");
	        		input = br.readLine();
	        		if (validInputFormat(input)) {
	        			input = processStringSpacing(input);
	        			ArrayList<Term> terms = getTerms(input);
	        			processTerms(terms);
	        			System.out.print("Canonical Form: ");
	        			printOutput(terms);
	        			break;
	        		}else{
	        			System.out.println("The equation you entered is not properly formatted. Please try again.");
	        		}
	        	}else{
	        		if(choice==1){
	        			FileInputStream fileInstream = new FileInputStream("input.txt");
	        			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInstream));
	        			String line;
	        			String output = "";
	        			while ((line = bufferedReader.readLine()) != null) {
	        				if (validInputFormat(line)) {
	    	        			input = processStringSpacing(line);
	    	        			ArrayList<Term> terms = getTerms(line);
	    	        			processTerms(terms);
	    	        			System.out.print("Canonical Form: ");
	    	        			output+=printOutput(terms)+"\n";
	    	        		}else{
	    	        			System.out.println("The equation you entered is not properly formatted. Please try again.");
	    	        		}
	        			}
	        			writeToFile(output, "output.out");
	        			bufferedReader.close();
	        			break;
	        		}else{
	        			System.out.print("Invalid Input.\n If you prefer to type the equation manually, enter 0.\n If you prefer to read from file, type 1");
	        		}
	        	}

	        }catch(NumberFormatException nfe){
	            System.err.println("Invalid Format!");
	        } catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/*
	 * Initializes the program.
	 */
	public static void main(String[] args) {
		queryUser();
	}

}
