import java.io.IOException;
import java.util.ArrayList;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class histogramme {
	
	public static void main(String[] args) throws IOException{
		Image img = ImageLoader.exec("C:\\Users\\bettse2008\\Downloads\\motos\\motos\\000.jpg");
		Viewer2D.exec(img);
		Viewer2D.exec(median(img));
		int seuile = 240;
		Image msk = buildBackgroundMask(img, seuile);
		
		int R = 0;
		double[] r= histogramme(img, R, msk);
		int G = 1;
		double[] g= histogramme(img, G, msk);
		int B = 0;
		double[] b= histogramme(img, B, msk);
				
		
	}
	//HISTOGRAMME
	public static double[] histogramme(Image img, int canal, Image mask) {
        int largeur = img.getXDim();
        int hauteur = img.getYDim();

        double[] histo = new double[256];

        for(int i = 0; i < histo.length; i++) {
            histo[i] = 0;
        }
        //0 c'est l'objet 

        for(int x = 0; x < largeur; x++) {
            for(int y = 0; y < hauteur; y++) {
                int valeur = img.getPixelXYBByte(x, y, 0);
                int m = img.getPixelXYBByte(x, y, 0);
                		if(m==255)
                		histo[valeur] += 1;
            }
        }
        try {
            HistogramTools.plotHistogram(histo);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'affichage : " + e);
        }

        return histo;
    }
	//MEDIAN 
	public static Image median(Image img) {
		Image newImg = new ByteImage(img);
	    newImg.setColor(true);
	    for (int x = 1; x < img.getXDim() -1; x++) {
	        for (int y = 1; y < img.getYDim()-1; y++) {
	            for (int b = 0; b < 2; b++) {
	                newImg.setPixelXYBByte(x,y,b,medianeVoisinage(img,x,y,b));
	            }
	        }
	    }return newImg;
	    
	}
	
	//MOYENNE VOISINAGE
	
	   private static int medianeVoisinage(Image img, int x, int y, int canal) {
	        ArrayList<Integer> tab = new ArrayList<>();
	        for (int X = x-1; X < x+2; X++) {
	            for (int Y = y-1; Y < y+2; Y++) {
	                tab.add(img.getPixelXYBByte(X,Y,canal));
	            }
	        }
	        tab.sort(null);
	        return tab.get(4);
	    }
	
	//DIVISER PAR DEUX 
	public void DiviserParDeux(Image img) {
		int largeur = img.getXDim();
        int hauteur = img.getYDim();		
		  for(int x =1; x<largeur; x++) {
			  for(int y = 1; y<hauteur; y++) {
				  
			  }
		  }
	}
	
	// NORMALISATION
	
	
	//CALCUL AUTOMATIQUE DU SEUIL 

	//
	private static Image buildBackgroundMask(Image i, int seuil) {
        //objet => 255
        //fond => 0
        int largeur = i.getXDim();
        int hauteur = i.getYDim();

        ByteImage mask = new ByteImage(largeur, hauteur, 1, 1, 1);

        for(int x = 0; x < i.getXDim(); x++) {
            for(int y = 0; y < i.getYDim(); y++ ) {

                int r = i.getPixelXYBByte(x, y, 0);
                int g = i.getPixelXYBByte(x, y, 1);
                int b = i.getPixelXYBByte(x, y, 2);

                if (r <= seuil && g <= seuil && b <= seuil) {
                    mask.setPixelXYTByte(x, y, 0, 255);
                }else {
                    mask.setPixelXYTByte(x, y, 0, 0);
                }

            }
        }

        return mask;
}
}
	
	

