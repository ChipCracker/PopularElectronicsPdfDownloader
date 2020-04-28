package de.chris.popularelectronics.downloader;

public class Main {

	public static void main(String[] args) {
		

		Downloader downloader = new Downloader();
		if(runAsync(args)) {
			System.out.println("Running in AsyncMode!");
			downloader.downloadAsync();
		}else {
			downloader.download();	
		}
	}
	
	public static boolean runAsync(String[] args) {
		 for(String s : args) {
			 if(s.equals("--async"));{
				 return true;
			 }
		 }
		 return false;
	}
	
}
