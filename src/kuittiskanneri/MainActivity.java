package kuittiskanneri;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int PIC_CROP = 2;
	
	private int _imgCapture;

	private Kontrolleri _kontrolleri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Luodaan kontrolleri
		_imgCapture = 0;
		_kontrolleri = new Kontrolleri(this, this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// ****************************** Tapahtumank‰sittelij‰t ****************************** 
	public void onSkannaaKuitti(View view) {

	    ++_imgCapture;
		startActivityForResult(_kontrolleri.skannaaKuitti(view), _imgCapture);
		
	}
	
	public void onHyvaksyKuva(View view) {
		_kontrolleri.hyvaksyKuva();
	}

	public void onSelaaKuitteja(View view) {
		// Siirryt‰‰n kuittilistan‰kym‰‰n
		_kontrolleri.selaaKuitteja(view);
	}
	
	public void onSelaaTuotteita(View view) {
		_kontrolleri.selaaTuotteita(view);
	}
	
	public void onTallennaKuitti(View view) {
		_kontrolleri.tallennaKuitti(view);
	}
	
	public void onPeruutaTallennus(View view) {
		
	}
	
	public void onLisaaUusiTuote(View view) {
		_kontrolleri.lisaaUusiTuote();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == _imgCapture && resultCode == RESULT_OK) {
	    	super.onActivityResult(requestCode, resultCode, data);
			_kontrolleri.activityResult();

	    }
	}

}

