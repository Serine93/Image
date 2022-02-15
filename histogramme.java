import java.io.IOException;
import java.util.ArrayList;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class histogramme {
	
	public static void main(String[] args) throws IOException{
		Image img1 = ImageLoader.exec("C:\\Users\\bettse2008\\Downloads\\motos\\motos\\000.jpg");
		Image img2 = ImageLoader.exec("C:\\Users\\bettse2008\\Downloads\\motos\\motos\\025.jpg");
		
		
		
		int seuile = 240;
		Image msk1 = buildBackgroundMask(img1, seuile);
		Image msk2 = buildBackgroundMask(img2, seuile);
		//Viewer2D.exec(msk);
		//Viewer2D.exec(img1);
		//Viewer2D.exec(median(img1));
		
		int R = 0,G=1,B=2;
		double[] r1= normalisation(discrétisationPar2(histogramme(img1, R, msk1)),img1);
		double[] g1=  normalisation(discrétisationPar2(histogramme(img1, G, msk1)),img1);
		double[] b1= normalisation(discrétisationPar2(histogramme(img1, B, msk1)),img1);

		double[] r2= normalisation(discrétisationPar2(histogramme(img2, R, msk2)),img2);
		double[] g2=  normalisation(discrétisationPar2(histogramme(img2, G, msk2)),img2);
		double[] b2= normalisation(discrétisationPar2(histogramme(img2, B, msk2)),img2);
		
		
	}
	//DESCRETISATION DE L'IMAGE 
	public static double[] discrétisationPar2(double [] histo) {
	     
	     double[]discrét = new double[histo.length/2];
	     for(int i=0,j=0;i<histo.length;i+=2,j+=1) {
	    	 discrét[j]=histo[i]+histo[i+1];
	    	 
	     }
		/*
		 * try { HistogramTools.plotHistogram(discrét); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		return discrét; 
	}
	
	//NORMALISATION DE L'IMAGE
	public static double[] normalisation(double [] discrét, Image img) {
		int largeur = img.getXDim();
	    int hauteur = img.getYDim();
		double [] normalis = new double[discrét.length];
		for(int i=0,j=0;i<discrét.length;i++,j++) {
			normalis[j]= (discrét[i]/(largeur*hauteur)) *100;
		}
		 try {
				HistogramTools.plotHistogram(normalis);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return normalis;
		
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
                int valeur = img.getPixelXYBByte(x, y, canal);
                int m = mask.getPixelXYBByte(x, y, 0);
                		if(m==255)
                		histo[valeur] += 1;
            }
        }
		/*
		 * try { HistogramTools.plotHistogram(histo); } catch (IOException e) {
		 * System.err.println("Erreur lors de l'affichage : " + e); }
		 */

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
	
	

