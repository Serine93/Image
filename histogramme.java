import java.io.IOException;
import java.util.ArrayList;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class histogramme {
	//LUI POSER LA QUESTION LE PLUS PRATIQUE A FAIRE
	public static ArrayList<String> ROUGE = new ArrayList<>();
	public static ArrayList<String> BLEU = new ArrayList<>();
	public static ArrayList<String> VERT = new ArrayList<>();
	public static void main(String[] args) throws IOException{
		ROUGE.add("C:\\Users\\bettse2008\\Downloads\\motos\\motos\\000.jpg"); //0
		Image img = ImageLoader.exec("");
		Viewer2D.exec(img);
		Viewer2D.exec(median(img));
		
		//faire directement dans le main les appels (R,G,B)
		
		
		
	}
	public static double[] histogramme(Image img) {
        int largeur = img.getXDim();
        int hauteur = img.getYDim();

        double[] histo = new double[256];

        for(int i = 0; i < histo.length; i++) {
            histo[i] = 0;
        }

        for(int x = 0; x < largeur; x++) {
            for(int y = 0; y < hauteur; y++) {
                int valeur = img.getPixelXYBByte(x, y, 0);
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
		int xMax = img.getXDim();
		int yMax = img.getXDim();
		int bMax = img.getXDim();
		
		
		for(int x =1; x)
		
	}
	 
	
	
	
}
