package kuittiskanneri;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TuoteKomponentti extends LinearLayout{

	public TuoteKomponentti(Context context, AttributeSet attrs) {
		super(context);
		LayoutInflater layoutInflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.tuoterivi, this);
	}
	
	// Asettaa tuotteen nimen ja hinnan n‰kym‰‰n
	public void asetaTuote(Tuote tuote)
	{	
		TextView tuoteNimi = (TextView) findViewById(R.id.tuoteEdit);
		tuoteNimi.setText(tuote.palautaNimi());
		
		TextView tuoteHinta = (TextView) findViewById(R.id.hintaEdit);
		tuoteHinta.setText(tuote.palautaHintaStr());
	
	}
	
	public Tuote palautaKorjattuTuote()
	{	
		TextView tuoteNimi = (TextView) findViewById(R.id.tuoteEdit);
		String nimi = tuoteNimi.getText().toString();
		
		TextView tuoteHinta = (TextView) findViewById(R.id.hintaEdit);
		String hinta = tuoteHinta.getText().toString();
		
		Tuote korjattu = new Tuote(nimi, hinta);
		
		// Asetetaan tallennus, jos k‰ytt‰j‰ niin haluaa
		CheckBox tallennusCheckBox = (CheckBox) findViewById(R.id.valittu);
		if(tallennusCheckBox.isChecked())
		{
			korjattu.asetaTallennus(true);
		}
		
		return korjattu;
	}

}
