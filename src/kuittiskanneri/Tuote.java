package kuittiskanneri;

import android.util.Log;

public class Tuote {
	
	// Hinta kokonaislukuna, jotta tarkkuus säilyy
	private int _hinta;
	private String _nimi;
	private boolean _validiTuote;
	private boolean _tallennetaanko;

	// Rakentaja
	// Poistaa hinnasta pisteen ja muuttaa sen kokonaisluvuksi
	public Tuote(String nimi, String hinta)
	{
		// Oletuksena tuotetta ei tallenneta, jotta tyhjiä tai
		// virheellisiä tuotteita ei tallenneta
		_tallennetaanko = false;
		
		// Säilytetään hinnan tarkkuus
		// Otetaan hinnasta kaikki muut kuin numerot pois
		String lopullinenHinta = "";

		for(int i = 0; i < hinta.length(); ++i)
		{
			char merkki = hinta.charAt(i);
			if(Character.isDigit(merkki))
			{
				lopullinenHinta += merkki;
			}
		}
		
		// Asetetaan nimi ja hinta sen mukaan onnistutaanko asettamisessa
		if(nimi.length() == 0)
		{
			_nimi = "virhe";
			Log.e("Tuotelisäys", "Tuotteen nimi ei kelpaa");
		} else {
			_nimi = nimi;
		}
		
		if (lopullinenHinta.length() < 3){
			_hinta = -1;
			Log.e("Tuotelisäys", "Tuotteen hinta ei kelpaa");
		} else {
			int hintaKokonaislukuna = Integer.parseInt(lopullinenHinta);
			
			// Asetetaan jäsenmuuttujat
			_hinta = hintaKokonaislukuna;
		}
		
		if(_nimi != "virhe" && _hinta != -1)
		{
			_validiTuote = true;
		} else {
			_validiTuote = false;
		}
		
	}
	
	void asetaTallennus(boolean tallennetaanko)
	{
		_tallennetaanko = tallennetaanko;
	}
	
	// Palautusfunktiot:
	boolean tallennetaanko()
	{
		return _tallennetaanko;
	}
	
	boolean validiTuote()
	{
		return _validiTuote;
	}
	
	String palautaNimi()
	{
		return _nimi;
	}
	
	int palautaHinta()
	{
		return _hinta;
	}
	
	String palautaHintaStr()
	{
		String hinta = Integer.toString(_hinta);
		// Piste paikalleen hintaan
		// Jos hinta on alle euron lisätään nolla eteen
		if(hinta.length() < 3)
		{
			hinta = "0" + hinta;
		}
		
		hinta = hinta.substring(0, hinta.length() - 2) + "." + hinta.substring(hinta.length() - 2, hinta.length());
		return hinta;
	}
}


