package kuittiskanneri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

public class Nakymankasittelija {

	private Activity _act;
	private Kontrolleri _kontrolleri;
	
	private Vector<TuoteKomponentti> _tuoteKomponentit = new Vector<TuoteKomponentti>();
	
	public Nakymankasittelija(Activity activity, Kontrolleri kontrolleri) {
		_act = activity;
		_kontrolleri = kontrolleri;
	}
	
	public void asetaKuva(Bitmap kuvatiedosto)
	{
		ImageView imgView = (ImageView) _act.findViewById(R.id.imageViewKuvanHyvaksyminen);
		imgView.setImageBitmap(kuvatiedosto);
	}
	
	// Luo tuotelistan kuitin perusteella
	public void luoTuotelista(Kuitti kuitti)
	{
		Vector<Tuote> tuotteet = kuitti.palautaTuotelista();
		
		// Tulostetaan tuotteet ja hinnat
		// Haetaan ensin layout
		LinearLayout linLayout = (LinearLayout) _act.findViewById(R.id.linLayoutTuotelista);
		
		for(int i = 0; i < tuotteet.size(); ++i)
		{
			if(tuotteet.elementAt(i).validiTuote())
			{
				// Luodaan uusi komponentti jokaisesta tuotteesta
				TuoteKomponentti uusiKomp = new TuoteKomponentti(_act, null);
				
				// Komponentti j‰tet‰‰n tyhj‰ksi jos kyseess‰ uusi tuote
				if(!tuotteet.elementAt(i).palautaNimi().equals("Uusi tuote"))
				{
					uusiKomp.asetaTuote(tuotteet.elementAt(i));
				}
				// Otetaan komponentti talteen
				_tuoteKomponentit.add(uusiKomp);
				
				// Laitetaan komponentti layoutiin
				linLayout.addView(uusiKomp);
			}
		}
		
		// Tulostetaan koko kuitin summa
		TextView summaTextView = (TextView) _act.findViewById(R.id.yhteensaNumeroTextView1);
		summaTextView.setText(kuitti.palautaSumma());
	}

	public void lisaaTyhjaKomponentti()
	{
		// Luodaan uusi komponentti jokaisesta tuotteesta
		TuoteKomponentti uusiKomp = new TuoteKomponentti(_act, null);
		
		// Otetaan komponentti talteen
		_tuoteKomponentit.add(uusiKomp);
		
		// Laitetaan komponentti layoutiin
		LinearLayout linLayout = (LinearLayout) _act.findViewById(R.id.linLayoutTuotelista);
		linLayout.addView(uusiKomp);
	}
	
	// Palauttaa tuotekomponentit
	public Vector<TuoteKomponentti> palautaTuoteKomponentit()
	{
		return _tuoteKomponentit;
	}
	
	public void tyhjennaTuoteKomponentit()
	{
		_tuoteKomponentit.clear();
	}
	
	public void luoKuittilista(Vector<Kuitti> kuitit, final Context context)
	{
		final ExpandableListView expList = (ExpandableListView) _act.findViewById(R.id.kuittilistaExpListView1);
		List<String> listDataHeader = new ArrayList<String>();
		HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
		
		// Otsikkorivit l‰pi ja laitetaan ne listaan
        for(int i = 0; i < kuitit.size(); ++i) 
		{
        	String header = kuitit.elementAt(i).palautaPvm() + " SUMMA: " + kuitit.elementAt(i).palautaSumma();
        	listDataHeader.add(header);
        	
        	// Tuotteet k‰yd‰‰n l‰pi ja laitellaan ne listaan
        	Vector<Tuote> tuotteet = kuitit.elementAt(i).palautaTuotelista();
        	List<String> childs = new ArrayList<String>();
        	
        	for(int a = 0; a < tuotteet.size(); ++a)
        	{
        		String tuote = tuotteet.elementAt(a).palautaNimi() + "    " + tuotteet.elementAt(a).palautaHintaStr();
        		childs.add(tuote);
        	}
        	
        	listDataChild.put(header, childs);
		}
		
        // Asetetaan laajennettavalle listalle adapteri
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(context, listDataHeader, listDataChild);
		expList.setAdapter(listAdapter);
		
		// Voidaan klikata pitk‰‰n, jolloin saadaan poisto tai muokkaus aikaiseksi
		expList.setLongClickable(true);
		// Tapahtumankuuntelija pitk‰lle klikkaukselle
		expList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View childView,
					int position, long id){
				
				// Otsikkorivin ja lapsialkioiden paikat
				long packedPosition = ((ExpandableListView) parent).getExpandableListPosition(position);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
				
                // Tarkastelu kun painetaan pitk‰‰n otsikkoalkiota
				if (ExpandableListView.getPackedPositionType(packedPosition) == 
	                    ExpandableListView.PACKED_POSITION_TYPE_GROUP) 
				{
	                Log.d("LONG CLICK ON GROUP", groupPosition + " ja " + childPosition);
	                // Avataan valikko, jossa on vaihtoehdot "Poista", "Muokkaa" (TODO "Lis‰‰ tuote")
	                avaaPopupMenu(context, childView, groupPosition);

	                // Tapahtuma k‰sitelty
	                return true;
				}
				
				// Tarkastelu kun painetaan pitk‰‰n lapsialkiota
				if (ExpandableListView.getPackedPositionType(packedPosition) == 
	                    ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
				{

	                Log.d("LONG CLICK ON CHILD", groupPosition + " ja " + childPosition);
	                // TODO Avaa tekstilaatikko jossa t‰t‰ tuotetta voi muokata
	                // Tapahtuma k‰sitelty
	                return true;
				}
				
				return false;
			}
			
		});
	}

	// Avaa popupmenun
	void avaaPopupMenu(Context context, View view, final int kuitinID)
	{
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu_kuittilista, popup.getMenu());
        
        // Tapahtumank‰sittelij‰ popupmenulle
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  	   
		   @Override
		   public boolean onMenuItemClick(MenuItem item) {
			   Log.d("ITEM", item.toString());
			   String itemStr = item.toString();
			   
			   // Kuitin muokkausn‰kym‰‰n siirtyminen
			   if(itemStr.equals("Muokkaa"))
			   {
				   _kontrolleri.muokkaaKuittia(kuitinID);
			   }
			   
			   // Kuitti poistetaan
			   if(itemStr.equals("Poista"))
			   {
				   _kontrolleri.poistaKuitti(kuitinID);
			   }
			   
			   return true;
		   }
        });

        popup.show();
	}
	
	
	
}
