import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.lang.model.util.ElementScanner14;
public class Polynomial
{
	double[] coeff;
	int[] exp;
	public Polynomial(){
		coeff = null;
		exp = null;
	}
	public Polynomial(double[] coeffs, int[] exps)
	{
		exp = new int[exps.length];
		coeff = new double[exps.length];
		for(int i = 0; i < exps.length; i++){
			exp[i] = exps[i];
			coeff[i] = coeffs[i];
		}
		removeZero();
		sort();
		merger();
	}
	public Polynomial(File input) throws FileNotFoundException
	{
	 	Scanner read = new Scanner(input);
	 	String line = "";
	 	if(read.hasNextLine())
	 		line = read.nextLine().trim();
	 	String[] result = line.split("\\+");
		int size = result.length;
		for(String str:result)
		{
			if(str.indexOf("-") != -1)
				for(int j = 0; j < str.length(); j++)
					if(str.charAt(j) == '-')
						size++;
			if(str.charAt(0) == '-')
				size--;
		}
		String[] resultant = new String[size];
		int[] exps = new int[size];
		double[] coeffs = new double[size]; 
		int count = 0;
		for(int i = 0; i < result.length; i++){
			if(result[i].indexOf("-") != -1)
			{
				int sub = 0;
				for(int j = 0; j < result[i].length(); j++)
					if(result[i].charAt(j) == '-')
						sub++;
				int start = 0;
				int posSub = result[i].indexOf('-');
				if(posSub == 0)
				{
					sub--;
					start = posSub;
					posSub = result[i].indexOf('-', posSub + 1);
					if(posSub == -1)
						posSub = result[i].length();
				}
				while(sub >= 0){
					resultant[count++] = result[i].substring(start, posSub); //4
					start = posSub;
					posSub = result[i].indexOf('-', posSub + 1);
					if(posSub == -1)
						posSub = result[i].length();
					sub--;
				}
			}
			else
				resultant[count++] = result[i];
		}
		count = 0;
		for(String str:resultant){
			if(str.indexOf("x") >= 0 && str.substring(str.indexOf("x")).length() >= 2){
				exps[count] = Integer.parseInt(str.substring(str.indexOf("x") + 1));
				if(str.substring(0, str.indexOf("x")).length() > 0)
					coeffs[count] = Double.parseDouble(str.substring(0, str.indexOf("x")));
				else
					coeffs[count] = 1;
			}
			else if(str.indexOf("x") < 0){
				exps[count] = 0;
				coeffs[count] = Double.parseDouble(str);
			}
			else{
				exps[count] = 1;
				if(str.substring(0, str.indexOf("x")).equals("-"))
					coeffs[count] = -1;
				else if(str.substring(0, str.indexOf("x")).length() > 0)
					coeffs[count] = Double.parseDouble(str.substring(0, str.indexOf("x")));
				else
					coeffs[count] = 1;
			}
			count++;	
		}
		read.close();
		exp = exps;
		coeff = coeffs;
		removeZero();
		sort();
		merger();
	}

	public void merger()
	{
		if(this.exp == null || this.coeff == null) return;
		int uniq = 0;
		for(int i = 0; i< exp.length; i++){
			while(i < exp.length - 1 && exp[i] == exp[i + 1])
				i++;
			uniq++;
		}
		int[] exps = new int[uniq];
		double[] coeffs = new double[uniq];
		int count = 0;
		for(int i = 0; i < exp.length; i++)
		{
			if(i == 0){
				exps[count] = exp[i];
				coeffs[count++] = coeff[i];
			}
			else if(exp[i] == exp[i - 1])
				coeffs[count - 1] += coeff[i];
			else{
				exps[count] = exp[i];
				coeffs[count++] = coeff[i];
			}
		}
		this.exp = exps;
		this.coeff = coeffs;
		removeZero();
	}

	public void removeZero()
	{
		int keep = 0;
		for(int i = 0; i < exp.length; i++)
			if(coeff[i] != 0)
				keep++;
		if(keep == 0){
			coeff = null;
			exp = null;
		}
		else{
			double[] coeffs = new double[keep];
			int[] exps = new int[keep];
			int count = 0;
			for(int i = 0; i < exp.length; i++)
			{
				if(coeff[i] != 0){
					exps[count] = exp[i];
					coeffs[count] = coeff[i];
					count++;
				}	
				if(count == keep)
				{	
					coeff = coeffs;
					exp = exps;	
					break;
				}
			}
		}
	}
	public void sort()
	{
		if(exp == null || coeff == null) return;
		int size = exp.length;
		for(int i = 0; i < size - 1; i++)
		{
			int id = i;
			for(int j = i + 1; j < size; j++)
				if(exp[j] < exp[id])
					id = j;
			int temp = exp[id];
			double dTemp = coeff[id];
			exp[id] = exp[i];
			coeff[id] = coeff[i];
			exp[i] = temp;
			coeff[i] = dTemp;
		}
	}
	public Polynomial add(Polynomial other)
	{
		if(this == null && other == null || other.exp == null && this.exp == null || other.coeff == null && this.coeff == null)
			return new Polynomial();
		else if(other == null || other.exp == null || other.coeff == null)
			return new Polynomial(this.coeff, this.exp);
		else if(this == null || this.exp == null || this.coeff == null)
			return new Polynomial(other.coeff, other.exp);
		
		Polynomial min, max;
		if(other.exp.length >= this.exp.length){
			min = this;
			max = other;
		}
		else{
			min = other;
			max = this;
		}
		int[] rExp = new int[max.exp.length];
		for(int i = 0; i < max.exp.length; i++)
			rExp[i] = max.exp[i];
		for(int val:min.exp)
			if(!Arrays.asList(rExp).contains(val))
				rExp = addEle(val, rExp);
		Arrays.sort(rExp);
		double[] coeffs = new double[rExp.length];
		for(int i = 0; i < rExp.length; i++)
			coeffs[i] = 0;
		for(int i = 0; i < min.exp.length; i++)
			coeffs[Arrays.binarySearch(rExp, min.exp[i])] += min.coeff[i];
		for(int i = 0; i < max.exp.length; i++)
			coeffs[Arrays.binarySearch(rExp, max.exp[i])] += max.coeff[i];
		return new Polynomial(coeffs, rExp);
	}
	public int[] addEle(int num, int[] exp)
	{
		int size = exp.length;
		int[] newExp = new int[size+1];
		for(int i = 0; i < size; i++)
			newExp[i] = exp[i];
		newExp[size] = num;
		return newExp;
	}
	public double[] addEle(double num, double[] coeff)
	{
		int size = coeff.length;
		double[] newCoeff = new double[size+1];
		for(int i = 0; i < size; i++)
			newCoeff[i] = coeff[i];
		newCoeff[size] = num;
		return newCoeff;
	}
	public double evaluate(double x)
	{
		if(this.exp == null || this.coeff == null)
			return 0;
		double result = 0;
		for(int i = 0; i < coeff.length; i++)
			result += (coeff[i] * Math.pow(x, exp[i]));
		return result;
	}
	public boolean hasRoot(double x)
	{
		return evaluate(x) == 0;
	}
	public Polynomial multiply(Polynomial other)
	{
		if(this == null || other == null || other.exp == null || this.exp == null || other.coeff == null || this.coeff == null)
			return new Polynomial();

		Polynomial min, max;
		if(other.exp.length >= this.exp.length){
			min = this;
			max = other;
		}
		else{
			min = other;
			max = this;
		}
		double[] coeffs = new double[0];
		int[] exps = new int[0];
		for(int i = 0; i < min.exp.length; i++){
			for(int j = 0; j < max.exp.length; j++){
				exps = addEle(min.exp[i] + max.exp[j], exps);
				coeffs = addEle(min.coeff[i] * max.coeff[j], coeffs);
			}
		}
		return new Polynomial(coeffs, exps);
	}


	public String getString()
	{
		String result = "";
		if(this.exp == null || this.coeff == null)
			result += "0.0";
		else
			for(int i = 0; i < exp.length; i++){
				if(exp[i] == 0)
					result += coeff[i];
				else if(exp[i] > 0 && coeff[i] < 0)
					result += coeff[i] + "x" + exp[i];
				else if(result.length() == 0)
					result += coeff[i] + "x" + exp[i];
				else
					result += "+" + coeff[i] + "x" + exp[i];
			}
		return result;
	}

	public void saveToFile(String name) throws IOException
	{
		File file = new File(name);
		if(!file.exists())
			file.createNewFile();
		FileWriter fileIn = new FileWriter(name);
		fileIn.write(getString());
		fileIn.close();
	}
}
