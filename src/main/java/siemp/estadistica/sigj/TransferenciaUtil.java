package siemp.estadistica.sigj;

public class TransferenciaUtil {
	
	public static int getIDEntidad(int idEntidadAux)
	{
		int idEntidad=idEntidadAux;
		if(idEntidadAux==2)
			idEntidad=18;
		if(idEntidadAux==9)
			idEntidad=12;
		if(idEntidadAux==10)
			idEntidad=13;
		if(idEntidadAux==11)
			idEntidad=14;
		if(idEntidadAux==12)
			idEntidad=16;
		if(idEntidadAux==501)
			idEntidad=15;
		if(idEntidadAux==601)
			idEntidad=17;
		if(idEntidadAux==309)
			idEntidad=9;
		if(idEntidadAux==301)
			idEntidad=10;
		if(idEntidadAux==302)
			idEntidad=11;
		if(idEntidadAux==13)
			idEntidad=19;
		if(idEntidadAux==14)
			idEntidad=20;
		if(idEntidadAux==15)
			idEntidad=21;
		if(idEntidadAux==16)
			idEntidad=22;
		if(idEntidadAux==604)
			idEntidad=23;
		if(idEntidadAux==605)
			idEntidad=24;
		
		return idEntidad;
		
	}
	
	public static int getNumFromRoma(String numRomano)
	{
		int num=0;
		if(numRomano.equals("I"))
			num=1;
		else if(numRomano.equals("II"))
			num=2;
		else if(numRomano.equals("III"))
			num=3;
		else if(numRomano.equals("IV"))
			num=4;
		else if(numRomano.equals("V"))
			num=5;
		else if(numRomano.equals("VI"))
			num=6;
		else if(numRomano.equals("VII"))
			num=7;
		else if(numRomano.equals("VIII"))
			num=8;
		else if(numRomano.equals("IX"))
			num=9;
		else if(numRomano.equals("X"))
			num=10;
		else if(numRomano.equals("XI"))
			num=11;
		else if(numRomano.equals("XII"))
			num=12;
		else if(numRomano.equals("XIII"))
			num=13;
		else if(numRomano.equals("XIV"))
			num=14;
		else if(numRomano.equals("XV"))
			num=15;
		else if(numRomano.equals("XVI"))
			num=16;
		return num;
	}
	

}
