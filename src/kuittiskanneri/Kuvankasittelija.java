package kuittiskanneri;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class Kuvankasittelija {
	public Bitmap muutaBinaariseksi(Bitmap bmpOriginal, int threshold)
	{
		Bitmap kuva = toGrayscale(bmpOriginal);
		kuva = toBinary(kuva, threshold);
		return kuva;
	}
	
	public Bitmap toBinary(Bitmap bmpOriginal, int threshold) {
	    int width, height;
	    height = bmpOriginal.getHeight();
	    width = bmpOriginal.getWidth();
	    Bitmap bmpBinary = Bitmap.createBitmap(bmpOriginal);

	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // Pikselin väri
	            int pixel = bmpOriginal.getPixel(x, y);
	            int red = Color.red(pixel);

	            // TODO Tähän tarkasteluja lähimmistä pikseleistä, jos on tummia, niin thresholdia lasketaan
	            // Kutsutaan funktiota, joka tarkastelee ollaanko kuvan reunalla
	           
	            if(red < threshold){
	                bmpBinary.setPixel(x, y, 0xFF000000);
	            } else{
	                bmpBinary.setPixel(x, y, 0xFFFFFFFF);
	            }

	        }
	    }
	    return bmpBinary;
	}
	
	public Bitmap toGrayscale(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
