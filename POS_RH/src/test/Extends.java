package test;

public class Extends extends Base {

	public void getC()
	{
		getA();
	}

	public void getB()
	{
		super.getB();
		System.out.println("this is extends getB");
	}
	
	public static void main(String[] args)
	{
		Extends ex =  new Extends();
		ex.getC();
	}
}
