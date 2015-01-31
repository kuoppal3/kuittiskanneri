package kuittiskanneri;

import java.io.File;
import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.googlecode.tesseract.android.TessBaseAPI;

public class Kontrolleri {
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int PIC_CROP = 2;
	
	private int _imgCapture;
	private Vector<Kuitti> _kuitit = new Vector<Kuitti>();
	private Tekstinkasittelija _tekstinKasittelija = new Tekstinkasittelija();
	private Nakymankasittelija _nakymanKasittelija;
	private Tiedostonkasittelija _tiedostonKasittelija = new Tiedostonkasittelija();
	private Kamera _kamera;
	private TessBaseAPI _baseApi;
	
	private Context _context;
	private Activity _activity;
	
	public Kontrolleri(Context context, Activity activity)
	{
		// Otetaan talteen activity n‰kym‰n vaihtamista varten
		_activity = activity;
		_context = context;
		
		_nakymanKasittelija = new Nakymankasittelija(_activity, this);
		
		// Ladataan kuitit tiedostosta
		_kuitit = _tiedostonKasittelija.lataaKuitit(_context);
		
		_imgCapture = 0;
		
		// Luodaan TessBaseApi
		_baseApi = new TessBaseAPI();
		_baseApi.setVariable("classify_font_name" , "Lucida Console");
		File tessbase_path = new File(Environment.getExternalStorageDirectory().getPath());
		_baseApi.init(tessbase_path.toString() + "/tesseract/", "eng");
		_baseApi.setVariable("classify_font_name" , "Lucida Console");

		// Siirryt‰‰n alkun‰kym‰‰n
		_activity.setContentView(R.layout.activity_main);
	}

	// ****************************** Tapahtumank‰sittelij‰t ****************************** 
	public Intent skannaaKuitti(View view)
	{
		_kamera = null;
		_kamera = new Kamera(_activity.getApplicationContext(), _activity);
	    Intent kameraIntent = _kamera.dispatchTakePictureIntent();
	    return kameraIntent;
	}
	
	public void hyvaksyKuva()
	{

	}
	
	public void selaaKuitteja(View view) {
		// Siirryt‰‰n kuittilistan‰kym‰‰n
		_activity.setContentView(R.layout.kuittilista);
		_nakymanKasittelija.luoKuittilista(_kuitit, _context);
	}
	
	public void selaaTuotteita(View view) {
		
	}
	
	// KuitinID on kuitin j‰rjestysluku laajennettavassa listassa, ylin 0 alin n
	public void muokkaaKuittia(int kuitinID) 
	{
		// TODO Kuitin muokkaamisen peruuttaminen ja kuitin lis‰‰minen oikealle paikalle
		// Luodaan uusi kuitti tunnistetun tekstin perusteella
		Kuitti uusiKuitti = _kuitit.elementAt(kuitinID);

		_kuitit.remove(kuitinID);
		// Siirryt‰‰n tuotelistan‰kym‰‰n ja tulostetaan kuitti muokattavaksi	  
	    _activity.setContentView(R.layout.tuotelista);
	    _nakymanKasittelija.luoTuotelista(uusiKuitti);
	}
	
	// KuitinID on kuitin j‰rjestysluku laajennettavassa listassa, ylin 0 alin n
	public void poistaKuitti(int kuitinID)
	{
		_kuitit.remove(kuitinID);
		
		// Kaikki kuitit tallentaessa ylikirjoitetaan kuitit.dat,
		// jolloin poistettu kuitti lopullisesti
		_tiedostonKasittelija.tallennaKaikkiKuitit(_kuitit, _context);
		
		// Siirryt‰‰n kuittilistan‰kym‰‰n
		_activity.setContentView(R.layout.kuittilista);
		_nakymanKasittelija.luoKuittilista(_kuitit, _context);
	}
	
	public Kuitti luoKuittiNakymasta()
	{
		Vector<TuoteKomponentti> komponentit = _nakymanKasittelija.palautaTuoteKomponentit();

		Kuitti uusiKuitti = new Kuitti();
		uusiKuitti.asetaPaivamaara(new Date().toString());
		
		// Lis‰t‰‰n komponenttien tiedot kuittiin
		for(int i = 0; i < komponentit.size(); ++i)
		{
			Tuote lisattava = komponentit.elementAt(i).palautaKorjattuTuote();
			if(lisattava.tallennetaanko())
			{
				uusiKuitti.lisaaTuote(lisattava.palautaNimi(), lisattava.palautaHintaStr());
			}

		}
		
		return uusiKuitti;
	}
	
	// Tallentaa kuitin
	public void tallennaKuitti(View view) {
		// Luodaan uusi kuitti n‰kym‰st‰ 
		Kuitti uusiKuitti = luoKuittiNakymasta();
		
		// Tyhjennet‰‰n n‰kym‰n komponenttien tiedot
		_nakymanKasittelija.tyhjennaTuoteKomponentit();
		
		// lis‰t‰‰n kuitti sovellukselle
		_kuitit.add(uusiKuitti);
		
		// Siirryt‰‰n kuittilistan‰kym‰‰n
		_activity.setContentView(R.layout.kuittilista);
		_nakymanKasittelija.luoKuittilista(_kuitit, _context);
		
		// Tallennetaan kuitti viel‰ tiedostoon
		_tiedostonKasittelija.tallennaKuitti(uusiKuitti, _context);

	}

	public void lisaaUusiTuote()
	{
		// Luodaan uusi kuitti n‰kym‰st‰ ja lis‰t‰‰n siihen tyhj‰ tuote
		Kuitti uusiKuitti = luoKuittiNakymasta();
		uusiKuitti.lisaaTuote("Uusi tuote", "0.00");
		
		for(int i = 0; i < uusiKuitti.palautaTuotelista().size(); ++i)
		{
			Log.d("asd", uusiKuitti.palautaTuotelista().elementAt(i).palautaNimi());
		}
		
		// Tyhjennet‰‰n vanhat tuotekomponentit ja luodaan uusi tuotelista
		_nakymanKasittelija.tyhjennaTuoteKomponentit();	

		// Siirryt‰‰n kuittilistan‰kym‰‰n
		_activity.setContentView(R.layout.tuotelista);
		_nakymanKasittelija.luoTuotelista(uusiKuitti);
	}
	
	// TODO 
	public void onPeruutaTallennus(View view) {
		
	}

	public void activityResult()
	{
		File tessbase_path = new File(Environment.getExternalStorageDirectory().getPath());
		
        if(new File(Environment.getExternalStorageDirectory().getPath() + "/tesseract/tessdata/eng.traineddata").exists())
        {
        	Log.i("Tiedosto", "Oli olemassa kiva tiedosto. " + tessbase_path.toString() + "/tesseract/tessdata/eng.traineddata");
        } else {
        	Log.i("Tiedosto", "Ei lˆytynyt tiedostoa. " + tessbase_path.toString() + "/tesseract/tessdata/eng.traineddata");
        }
        
        // TODO kuvan croppaus
        //performCrop();
        
        // Haetaan kuva t‰m‰n sovelluksen yksityisest‰ tallennustilasta
        File kuvatiedosto = _kamera.palautaKuvatiedosto();
        BitmapFactory.Options options = new BitmapFactory.Options();
        
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap kuva = BitmapFactory.decodeFile(kuvatiedosto.getAbsolutePath(), options);

        // Optimoidaan kuva bin‰‰riseksi
        Kuvankasittelija kasittelija = new Kuvankasittelija();	        
        kuva = kasittelija.muutaBinaariseksi(kuva, 127);

        // Parsitaan kuvasta teksti
        _baseApi.clear();
		_baseApi.setImage(kuva);

		String recognizedText = _baseApi.getUTF8Text();
		Log.i("Tunnistettu teksti", recognizedText);

		// Otetaan viel‰ turhia merkkej‰ pois tunnistetun tekstin lopusta
		Tekstinkasittelija tekstinKasittelija = new Tekstinkasittelija();			
		// TODO vaatii korjauksia
		//tekstinKasittelija.parsiLoppu(recognizedText);
		String tuotteetStr = recognizedText;

		// Luodaan uusi kuitti tunnistetun tekstin perusteella
		Kuitti uusiKuitti = new Kuitti();
		Date pvm = new Date();
		String pvmStr = pvm.toString();
		uusiKuitti.asetaPaivamaara(pvmStr);
		uusiKuitti.lisaaKuitti(tuotteetStr);

		// Siirryt‰‰n kuvan hyv‰ksymisn‰kym‰‰n
		_activity.setContentView(R.layout.kuvan_hyvaksyminen);
		_nakymanKasittelija.asetaKuva(kuva);
		
		// Siirryt‰‰n tuotelistan‰kym‰‰n ja tulostetaan kuitti muokattavaksi	  
	    _activity.setContentView(R.layout.tuotelista);
	    _nakymanKasittelija.luoTuotelista(uusiKuitti);
	}
}
