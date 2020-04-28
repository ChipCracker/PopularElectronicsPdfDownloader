package de.chris.popularelectronics.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Downloader {

	private String url = "https://www.americanradiohistory.com/Archive-Poptronics/";
	 private  Logger logger = Logger.getLogger("DownloadLogger");  
	
	public Downloader() {
		

	   
		
	}
	public void download() {
		FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("./download.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("My first log");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
		
		String magazineName = "Pop";
		String[] centuries= {"1950","1960","1970","1980","1990","2000"};
		String[] years = generateYearArray();
		String[] months = {"01", "02","03","04","05", "06", "07","08","09","10","11","12"};
		
		File download = new File("./downloads");
		download.mkdir();
		
		for(String century : centuries) {
			for(String year : years) {
				for(String month: months) {
					int centuryInt= Integer.valueOf(century);
					int yearInt = Integer.valueOf(year);
					if(centuryInt == 1950 && yearInt < 4) {
						continue;
					} 
					downloadEdition(century, century.substring(2,3)+year, month, magazineName);
				}
			}
		}
	}
	public void downloadAsync() {
		

	    FileHandler fh;  

	    try {  

	        // This block configure the logger with handler and formatter  
	        fh = new FileHandler("./download.log");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	        // the following statement is used to log any messages  
	        logger.info("My first log");  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
		
		String magazineName = "Pop";
		String[] centuries= {"1950","1960","1970","1980","1990","2000"};
		String[] years = generateYearArray();
		String[] months = {"01", "02","03","04","05", "06", "07","08","09","10","11","12"};
		
		File download = new File("./downloads");
		download.mkdir();
		
		ExecutorService pool = Executors.newFixedThreadPool(5);
		for(String century : centuries) {
			for(String year : years) {
				for(String month: months) {
					int centuryInt= Integer.valueOf(century);
					int yearInt = Integer.valueOf(year);
					if(centuryInt == 1950 && yearInt < 4) {
						continue;
					} 
					doAsyncDownloadEdition(pool, century, century.substring(2,3)+year, month, magazineName);
				}
			}
		}
	}
	// 1,2,3,4,...
	private String[] generateYearArray() {
		List<String> yearList = new ArrayList<String>();
		for(int i=0; i<10; i++) {
			yearList.add(Integer.toString(i));
		}
		String[] yearArray = new String[yearList.size()];
		int tmp = 0;
		for(String year : yearList) {
				yearArray[tmp] = year;
			tmp++;
		}
		return yearArray;
	}
	
	private void doAsyncDownloadEdition(ExecutorService pool, final String century, final String year, final String month, final String magazineName) {
		// Irgendwas Asynchrones machen
		Runnable r= new Runnable() {
		    public void run() {
		        downloadEdition(century, year, month, magazineName);
		    }
		};
		
		pool.execute(r);
		
	}
	

	
	/**
	 * 
	 * @param century z.b 1950
	 * @param year	z.b 55
	 * @param month	z.b 4
	 * @return
	 */
	private boolean downloadEdition(String century, String year, String month, String magazineName) {
		String hyphen = "-";
		String yearPrefix = "";
		if(Integer.valueOf(year) < 50 && Integer.valueOf(year) >= 0) {
			magazineName = "PP";
		}else if(Integer.valueOf(year) == 64 && Integer.valueOf(month) == 12) {
			magazineName =  "Poptronics";
		} else if(Integer.valueOf(year) == 68) {
			magazineName = "Poptronics";
		}else if(Integer.valueOf(year)>69) {
			yearPrefix = "19";
			if(Integer.valueOf(year) == 72) {
				magazineName ="Pop";
			}else {
				magazineName = "Poptronics";
			}
		}
		if(Integer.valueOf(year)>82 && Integer.valueOf(year) <86) {
			magazineName = "CE";
		}else if(Integer.valueOf(year)> 88){
			magazineName= "PE";
		}
		if(Integer.valueOf(year) > 89) {
			yearPrefix = "";
		}
		if(Integer.valueOf(year) == 99) {
			hyphen = ".";
		}
		String episodeUrl = this.url + century.substring(2,4) + "s/"+yearPrefix+year+"/"+magazineName+hyphen+century.substring(0,3)+year.substring(1,2)+"-"+month+".pdf";
		System.out.println(episodeUrl);
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(episodeUrl).openStream());
			@SuppressWarnings("resource")
			FileOutputStream fos = new FileOutputStream("./downloads/Popular-Electronics Ausgabe_"+year+"-"+month+".pdf");
			byte dataBuffer[] = new byte[1024];
		    int bytesRead;
		    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
		        fos.write(dataBuffer, 0, bytesRead);
		    }
		    
		} catch (Exception e) {
			logger.log(Level.INFO , e.toString());
		}
		return true;
	}
}
