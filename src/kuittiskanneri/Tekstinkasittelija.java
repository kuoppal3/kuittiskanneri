package kuittiskanneri;

import android.app.Activity;
import android.util.Log;

public class Tekstinkasittelija extends Activity {

	public Tekstinkasittelija() {

	}
	
	// TODO ei toimi
	public String parsiLoppu(String tunnistettuTeksti)
	{
		// Tarkastellaan jokaista tuotetta niin ett‰ se tulkitaan sanaksi "YHTEENSA"
		// Oletetaan ett‰ kaksi merkki‰ saa olla v‰‰rin jolloin tarkastelu tapahtuu
		// **TEENSA, *H*EENSA, *HT*ENSA, *HTE*NSA, ... , YHT*ENS*, YHTE*NS*, YHTEE*S*, YHTEEN**
		String[] rivit = tunnistettuTeksti.split(System.getProperty("line.separator"));	
	
		for(int i = 0; i < rivit.length; ++i)
		{
			boolean oliYhteensa = false;
			Log.i("TUOTERIVI", rivit[i]);
			
			int prefix = 0;
			// Etsit‰‰n prefiksin pituus
			// TODO t‰ss‰ lienee vikaa
			for(int a = 0; a < rivit[i].length(); ++a)
			{
				if((rivit[i].charAt(a) == 'Y' || rivit[i].charAt(a) == 'V'))
				{
					prefix = a;
					break;
				}
			}
			
			for(int indx = 0; indx < rivit[i].length() - prefix; ++indx)
			{
				int x = indx - prefix;
				Log.i("kirjain", Character.toString(rivit[i].charAt(x)));
				if((x == 0 && (rivit[i].charAt(x) == 'Y' || rivit[i].charAt(x) == 'V'))
				|| (x == 1 && (rivit[i].charAt(x) == 'H'))
				|| (x == 2 && (rivit[i].charAt(x) == 'T'))
				|| ((x == 3 || x == 4) && (rivit[i].charAt(x) == 'E'))
				|| (x == 5 && (rivit[i].charAt(x) == 'N'))
			 	|| (x == 6 && (rivit[i].charAt(x) == 'S'))
				|| (x == 7 && (rivit[i].charAt(x) == 'A')))
				{
					oliYhteensa = true;
				} else if (x < 8) {
					oliYhteensa = false;
					break;
				}
				
			}
			if(oliYhteensa)
			{
				Log.w("L÷YTYI", "YHTEENSƒ");
				tunnistettuTeksti = "";
				for(int a = 0; a < i; ++a)
				{
					tunnistettuTeksti += rivit[a] + "\n";
				}
			}
		}
		
		return tunnistettuTeksti;
	}
}
