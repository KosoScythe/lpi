import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Formula {

	ArrayList<Formula> nazov = new ArrayList<>();

	public Formula[] subf() {
		Formula kopia[] = new Formula[nazov.size()];
		for (int i = 0; i < nazov.size(); i++) {
			kopia[i] = nazov.get(i);
		}
		return kopia;
	}

	public Set<String> vars() {
		Set<String> mnoz = new HashSet<>();
		for (Formula a : nazov) {
			mnoz.addAll(a.vars());
		}
		return mnoz;
	}

	public abstract String toString();

	public abstract boolean isSatisfied(Map<String, Boolean> v);
	
	public abstract Formula kopia();

	public boolean equals(Formula other) {
		return this.toString().equals(other.toString());
	}

	public abstract int deg();

	public abstract Formula substitute(Formula what, Formula replacement);
}

class Variable extends Formula {/*****************************************************************************************/
	String name = "";

	public Variable(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		return v.get(name);
	}

	/*
	 * @Override public boolean equals(Formula other) { // TODO Auto-generated
	 * method stub return this.toString().equals(other.toString(); }
	 */

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		if (kopia.toString().equals(what.toString())) {
			return replacement.kopia();
		}
		return kopia;
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Variable v = new Variable(name);		
		return v;
	}
	
	public Set<String> vars() {
		Set<String> mnoz = new HashSet<>();
		mnoz.add(name);
		return mnoz;
	}

}

class Negation extends Formula {/*****************************************************************************************/
	public Negation(Formula originalFormula) {
		nazov.add(originalFormula);
	}

	public Formula originalFormula() {
		return nazov.get(0);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "-" + originalFormula().toString();
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		return !(originalFormula().isSatisfied(v));
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		return 1 + originalFormula().deg();
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		if (kopia.equals(what)){
			return replacement.kopia();
		}
		
		Negation n = new Negation(originalFormula().substitute(what, replacement));
		
		return n;
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Negation n = new Negation(originalFormula());
		return n;
	}
}

class Disjunction extends Formula {/*****************************************************************************************/
	public Disjunction(Formula formula[]) {
		for (Formula a : formula) {
			nazov.add(a);
		}

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String pom = "(";
		String result = "";
		for (Formula a : nazov) {
			pom += a.toString() + "|";
		}

		for (int i = 0; i < pom.length() - 1; i++) {
			result += pom.charAt(i);
		}
		result += ")";
		return result;
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		for (Formula a : nazov) {
			if (a.isSatisfied(v) == true)
				return true;
		}
		return false;
	}


	@Override
	public int deg() {
		// TODO Auto-generated method stub
		int res = 0;
		for (Formula a : nazov) {
			res += a.deg();
		}
		return 1 + res;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		if (kopia.equals(what)){
			return replacement.kopia();
		}
		
		Formula[] x = new Formula[nazov.size()];  
		for (int i = 0; i < nazov.size(); i++) {
			if (nazov.get(i).equals(what)) {
				x[i] = replacement.kopia();
			}
			else {
				x[i] = nazov.get(i).substitute(what, replacement);
			}
			/*x[i] = nazov.get(i).kopia();*/
		}
		
		Disjunction d = new Disjunction(x);
		
		return d;
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Formula[] x = new Formula[nazov.size()];  
		for (int i = 0; i < nazov.size(); i++) {
			x[i] = nazov.get(i).kopia();
		}
		Disjunction d = new Disjunction(x);
		return d;
	}
}

class Conjunction extends Formula {/*****************************************************************************************/
	public Conjunction(Formula formula[]) {
		for (Formula a : formula) {
			nazov.add(a);
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String pom = "(";
		String result = "";
		for (Formula a : nazov) {
			pom += a.toString() + "&";
		}

		for (int i = 0; i < pom.length() - 1; i++) {
			result += pom.charAt(i);
		}
		result += ")";
		return result;
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		for (Formula a : nazov) {
			if (a.isSatisfied(v) == false)
				return false;
		}
		return true;
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		int res = 0;
		for (Formula a : nazov) {
			res += a.deg();
		}
		return  1 + res;
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		if (kopia.equals(what)){
			return replacement.kopia();
		}
		
		Formula[] x = new Formula[nazov.size()];  
		for (int i = 0; i < nazov.size(); i++) {
			if (nazov.get(i).equals(what)) {
				x[i] = replacement.kopia();
			}
			else {
				x[i] = nazov.get(i).substitute(what, replacement);
			}
			/*x[i] = nazov.get(i).kopia();*/
		}
		
		Conjunction d = new Conjunction(x);
		
		return d;
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Formula[] x = new Formula[nazov.size()];  
		for (int i = 0; i < nazov.size(); i++) {
			x[i] = nazov.get(i).kopia();
		}
		Conjunction d = new Conjunction(x);
		return d;
	}
}

abstract class BinaryFormula extends Formula { /*****************************************************************************************/
	Formula left;
	Formula right;

	public BinaryFormula(Formula leftSide, Formula rightSide) {
		left = leftSide;
		right = rightSide;
	}

	public Formula leftSide() {
		return left;
	}

	public Formula rightSide() {
		return right;
	}
	
	public Set<String> vars() {
		Set<String> mnoz = new HashSet<>();
		mnoz.addAll(left.vars());
		mnoz.addAll(right.vars());
		return mnoz;
	}
}

class Implication extends BinaryFormula { /*****************************************************************************************/
	
	public Implication(Formula leftSide, Formula rightSide) {
		super(leftSide, rightSide);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + leftSide().toString() + "->" + rightSide().toString() + ")";
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		if (leftSide().isSatisfied(v) == true && rightSide().isSatisfied(v) == false) return false;
		return true;
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub
		return 1 + leftSide().deg() + rightSide().deg();
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		
		if (kopia.equals(what)) {
			return replacement.kopia();
		}
		else {
			Implication i = new Implication(left.kopia().substitute(what, replacement), right.kopia().substitute(what, replacement));
			return i;
		}
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Implication i = new Implication(left, right);
		return i;
	}
}

class Equivalence extends BinaryFormula { /*****************************************************************************************/
	public Equivalence(Formula leftSide, Formula rightSide) {
		super(leftSide, rightSide);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "(" + leftSide().toString() + "<->" + rightSide().toString() + ")";
	}

	@Override
	public boolean isSatisfied(Map<String, Boolean> v) {
		// TODO Auto-generated method stub
		if (leftSide().isSatisfied(v) == rightSide().isSatisfied(v)) return true;
		return false;
	}

	@Override
	public int deg() {
		// TODO Auto-generated method stub 
		return 1 + leftSide().deg() + rightSide().deg();
	}

	@Override
	public Formula substitute(Formula what, Formula replacement) {
		// TODO Auto-generated method stub
		Formula kopia = this.kopia();
		
		if (kopia.equals(what)) {
			return replacement.kopia();
		}
		else {
			Equivalence i = new Equivalence(left.kopia().substitute(what, replacement), right.kopia().substitute(what, replacement));
			return i;
		}
	}

	@Override
	public Formula kopia() {
		// TODO Auto-generated method stub
		Equivalence i = new Equivalence(left, right);
		return i;
	}

}