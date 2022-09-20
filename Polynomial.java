public class Polynomial
{
	protected double[] coeff;
	public Polynomial(){
		coeff = new double[1];
		coeff[0] = 0;
	}
	public Polynomial(double[] coeffs)
	{
		//If array coeffs is not referenced elsewhere --> coeff = coeffs;
		coeff = new double[coeffs.length];
		for(int i = 0; i < coeffs.length; i++)
		{
			coeff[i] = coeffs[i];	
		}
	}
	public void display()
	{
		for(int i = 0; i < coeff.length; i++)
		{
			if(i == 0) System.out.print("\n" + coeff[i]);
			else if(coeff[i] != 0) System.out.print(coeff[i] + "x^" + i);
			if(i != coeff.length - 1 && coeff[i] != 0) System.out.print("+");
		}
		System.out.println();
	}
	public Polynomial add(Polynomial other)
	{
		int size = Math.max(other.coeff.length, coeff.length);
		double[] newCoeff = new double[size];
		for(int i = 0; i < size; i++)
		{
			if(other.coeff.length <= i)
				newCoeff[i] = coeff[i];
			else if(coeff.length <= i)
				newCoeff[i] = other.coeff[i];
			else
				newCoeff[i] = other.coeff[i] + coeff[i];
		} 
		Polynomial result = new Polynomial(newCoeff);
		return result;
	}
	public double evaluate(double x)
	{
		double result = 0;
		for(int i = 0; i < coeff.length; i++)
		{
			result += (coeff[i] * Math.pow(x, i));
		}
		return result;
	}
	public boolean hasRoot(double x)
	{
		return evaluate(x) == 0;
	}
}