package kuittiskanneri;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import android.content.Context;
import android.util.Log;

public class Tiedostonkasittelija {

	public Tiedostonkasittelija() {
		
	}
	
	public void tallennaKuitti(Kuitti kuitti, Context context)
	{
		// Luodaan tiedosto vain jos sit‰ ei ole olemassa
		File file = new File(context.getFilesDir().getAbsolutePath()+"/kuitit.dat");
		if(file.exists() == false)
		{
			new File(context.getFilesDir(), "kuitit.dat");
		} else {
			Log.i("Tiedosto", "Tiedosto oli jo olemassa.");
		}

		// Tehd‰‰n kuitista tekstimuotoinen seuraavaan tapaan
		// KUITTI 
		// *timestamp*
		// *tuote 1 nimi*
		// *tuote 1 hinta*
		// *tuote 2*
		// *tuote 2 hinta*
		// ...
		// *tuote n*
		// *tuote n hinta*
		// jne..
		String kuittiStr = "";
		
		kuittiStr += "KUITTI" + "\n";
		kuittiStr += kuitti.palautaPvm() + "\n";
		for(int i = 0; i < kuitti.palautaTuotelista().size(); ++i)
		{
			kuittiStr += kuitti.palautaTuotelista().elementAt(i).palautaNimi() + "\n";
			kuittiStr += kuitti.palautaTuotelista().elementAt(i).palautaHintaStr() + "\n";
		}
		
		// Filestream
		FileOutputStream outputStream;
		
		try {
			// mode append -> ei ylikirjoiteta
			outputStream = context.openFileOutput("kuitit.dat", Context.MODE_APPEND);
			outputStream.write(kuittiStr.getBytes());
			outputStream.close();
		} catch (Exception e) {
			//e.printStackTrace;
		}
	}

	public void tallennaKaikkiKuitit(Vector<Kuitti> kuitit, Context context)
	{
		// Tyhjennet‰‰n ensin kuitit.dat
		// Filestream
		FileOutputStream outputStream;
		
		String tyhjaStr = "";
		try {
			// mode private -> ylikirjoitetaan tyhj‰ll‰ merkkijonolla kuitit.dat
			outputStream = context.openFileOutput("kuitit.dat", Context.MODE_PRIVATE);
			outputStream.write(tyhjaStr.getBytes());
			outputStream.close();
		} catch (Exception e) {
			//e.printStackTrace;
		}
		
		// Tallennetaan kuitit uudestaan
		for(int i = 0; i < kuitit.size(); ++i)
		{
			tallennaKuitti(kuitit.elementAt(i), context);
		}
	}
	
	// Lataa kuitit yksityisest‰ muistista
	public Vector<Kuitti> lataaKuitit(Context context)
	{
		Vector<Kuitti> kuitit = new Vector<Kuitti>();
		Log.d("Kuittilataus", "alkoi");
		File file = new File(context.getFilesDir().getAbsolutePath()+"/kuitit.dat");
		if(file.exists())
		{
			Log.d("Kuittilataus", "tiedosto oli olemassa");
			
			String kuittiStr = "";

			
			// Filestream
			FileInputStream inputStream;
			try {
				inputStream = context.openFileInput("kuitit.dat");
				int c;
				while( (c = inputStream.read()) != -1){
					   kuittiStr += Character.toString((char)c);
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			
			// Luetaan kuittia rivi kerrallaan 
			String[] rivit = kuittiStr.split(System.getProperty("line.separator"));
			
			Log.d("Kuitin lataus muistista", kuittiStr);
			
			for(int i = 0; i < rivit.length; ++i)
			{
				if(rivit[i].equals("KUITTI"))
				{
					// Kuitti, joka alkaa "KUITTI"-tagia seuraavalta rivilt‰
					kuitit.add(lueKuitti(i + 1, rivit));
					Log.d("KUITTILATAUS L÷YTYI", "KUITTI" + i);
				}
					
			}
		}
			
		return kuitit;
		
	}
	
	public void poistaKuitti(int kuittiID)
	{
		// TODO kuitin poistaminen
	}
	
	public void muokkaaKuittia(Kuitti muokattu)
	{
		// TODO kuitin muokkaaminen
	}
	
	private Kuitti lueKuitti(int alkuRivi, String[] rivit)
	{
		Kuitti uusiKuitti = new Kuitti(); 
		// Alkurivi on timestamp-rivi
		uusiKuitti.asetaPaivamaara(rivit[alkuRivi]);
		
		// Aloitetaan ensimm‰isest‰ tuotteesta
		int i = alkuRivi + 1; 

		// Luetaan rivej‰ niin kauan ett‰ tulee vastaan seuraava "KUITTI" tai ett‰ rivit loppuu
		while(!rivit[i].equals("KUITTI"))
		{
			Log.d("RIVI KƒSITTELYSSƒ", rivit[i]);
			// Jos rivej‰ on j‰ljell‰, luetaan tuote ja hinta
			if(i < rivit.length)
			{   
				Log.d("KUITTILATAUS L÷YTYI", "NIMI JA HINTA");
				// Tuotteen nimi
				String nimi = rivit[i];
				++i;
				
				// Tuotteen hinta		
				String hinta = rivit[i];
				
				// Ei yli-indeksoida
				if((rivit.length - 1) == i)
				{
					Log.d("VIIMEINEN RIVI", "OLIS TESTISSƒ");
					uusiKuitti.lisaaTuote(nimi, hinta);
					break;
				}
				++i;
				uusiKuitti.lisaaTuote(nimi, hinta);
			}
		}
		return uusiKuitti;
	}
}
