import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import fr.unistra.pelican.ByteImage;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;

public class histogramme {
	
	public static void main(String[] args) throws IOException{
		String path="C:\\Users\\bettse2008\\Downloads\\motos\\motos\\000.jpg";
		Image img1 = ImageLoader.exec(path);
		img1.setName(path);
		
		//Image img2 = ImageLoader.exec("C:\\Users\\bettse2008\\Downloads\\motos\\motos\\025.jpg");
		
		TreeMap<Double, String> tm = new TreeMap<>();
		
		int seuile = 240;
		Image msk1 = buildBackgroundMask(img1, seuile);
		median(img1);
		//Viewer2D.exec(msk);
		//Viewer2D.exec(img1);
		//Viewer2D.exec(median(img1));
		int R = 0,G=1,B=2;
		double[] r1= normalisation(discrétisationPar2(histogramme(img1, R, msk1)),img1);
		double[] g1=  normalisation(discrétisationPar2(histogramme(img1, G, msk1)),img1);
		double[] b1= normalisation(discrétisationPar2(histogramme(img1, B, msk1)),img1);
		
		File dossier = new File("C:\\Users\\bettse2008\\Downloads\\motos\\motos");
		File[] ToutesLesMotos = dossier.listFiles();
		for(int i=0; i< ToutesLesMotos.length; i++) {
			Image im = ImageLoader.exec(ToutesLesMotos[i].getAbsolutePath());
			im.setName(ToutesLesMotos[i].getAbsolutePath());
		if(im.getName().equals(img1.getName())) {
			System.err.println("error");
		}else {
			median(im);
			Image msk2 = buildBackgroundMask(im, seuile);
			double[] r2= normalisation(discrétisationPar2(histogramme(im, R, msk2)),im);
			double[] g2=  normalisation(discrétisationPar2(histogramme(im, G, msk2)),im);
			double[] b2= normalisation(discrétisationPar2(histogramme(im, B, msk2)),im);
			
			double distance = distanceEuclidienne(g1, g2) + 
					distanceEuclidienne(r1, r2) + distanceEuclidienne(b1, b2);
			
			tm.put( distance, im.getName());
		}	
		}
		int k=0;
        for(Entry<Double, String> e:tm.entrySet()) {
            System.out.println(e.getValue() + " => "+e.getKey());
            Viewer2D.exec(ImageLoader.exec(e.getValue()));

            k++;

            if(k>10) break;
        }

        System.out.println(tm);
	}
	



	//SIMILARITE
	public static double distanceEuclidienne(double[] histo1, double[] histo2) {
		
		double distance = 0;
		for (int j = 0; j < histo1.length; ++j) {
			distance += Math.sqrt(Math.pow(histo1[j] - histo2[j], 2));
		}


		return distance;
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
	
	

