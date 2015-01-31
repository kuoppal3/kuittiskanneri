package kuittiskanneri;


import java.util.Vector;
import android.util.Log;

public class Kuitti {
	// Lista t�m�n kuitin tuotteista
	private Vector<Tuote> _tuotteet;
	private String _paivamaara;
	
	// Rakentaja
	public Kuitti()
	{
		//_paivamaara = new Date();
		_tuotteet = new Vector<Tuote>();
	}
	
	// Lis�� kuitin tuotteet kuitille tekstin perusteella
	public void lisaaKuitti(String kuitinTeksti)
	{
		String[] rivit = kuitinTeksti.split(System.getProperty("line.separator"));
		for(int i = 0; i < rivit.length; ++i)
		{
			parsiTuote(rivit[i]);
		}
	}
	
	private void parsiTuote(String tuoteRivi)
	{
		// Hinta ja nimi
		String hinta = "";
		boolean hintaLoydetty = false;
		String nimi = "";
		
		// Parsitaan lopusta, jolloin saadaan hinta ensin
		for(int i = tuoteRivi.length() - 1; i >= 0; --i)
		{
			char merkki = tuoteRivi.charAt(i);
			// Vaaditaan numero, piste tai v�lily�nti ja hinta on enint��n 6 merkki�
			if((Character.isDigit(merkki) || merkki == '.' || merkki == ' ' ) && hintaLoydetty == false
				&& tuoteRivi.length() - 6 < i )
			{
				hinta += merkki;
				continue;
			} 
			
			if(Character.isLetter(merkki) || Character.isDigit(merkki) || merkki == ' ' || merkki == ',' || merkki == '%' ) {
				// Hintaa ei en�� parsita
				hintaLoydetty = true;
				// Aletaan parsia nime�
				nimi += merkki;
			}
		}
		
		// K��nnet��n nimi ja hinta oikeaan suuntaan
		String kaannettyNimi = new StringBuffer(nimi).reverse().toString();	
		String kaannettyHinta = new StringBuffer(hinta).reverse().toString();

		// Lis�t��n tuote
		lisaaTuote(kaannettyNimi, kaannettyHinta);
		
	}
	
	// Lis�� tuotteen kuitille
	public void lisaaTuote(String nimi, String hinta)
	{
		Log.i("Tuotelis�ys", nimi + "   " + hinta);
		Tuote uusiTuote = new Tuote(nimi, hinta);
		_tuotteet.addElement(uusiTuote);
	}
	
	// Asettaa kuitille p�iv�m��r�n
	public void asetaPaivamaara(String paivamaara)
	{
		_paivamaara = paivamaara;
	}
	
	// PALAUTUSFUNKTIOT:
	public Vector<Tuote> palautaTuotelista()
	{
		return _tuotteet;
	}
	
	public String palautaPvm()
	{
		return _paivamaara;
	}
	
	public String palautaSumma()
	{
		int summa = 0;
		// Summataan validien tuotteiden hinnat yhteen
		for(int i = 0; i < _tuotteet.size(); ++i)
		{
			if(_tuotteet.elementAt(i).validiTuote())
			{
				summa += _tuotteet.elementAt(i).palautaHinta();
			}
		}
		
		String loppusumma = Integer.toString(summa);
		// Muutetaan summa stringiksi, jossa piste paikallaan
		// Jos hinta on alle euron lis�t��n nolla eteen
		if(loppusumma.length() < 3)
		{
			loppusumma = "0" + loppusumma;
		}
		
		loppusumma = loppusumma.substring(0, loppusumma.length() - 2) + "." + loppusumma.substring(loppusumma.length() - 2, loppusumma.length());
		return loppusumma;
	}
	
}
