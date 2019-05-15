
/**
 * Write a description of CSVMax here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;

public class CSVMin {
    public CSVRecord coldestHourInFile(CSVParser parser){
        CSVRecord smallestSoFar=null;
        for (CSVRecord currentRow : parser) {
        	if (smallestSoFar == null) {
        		smallestSoFar = currentRow;
        	} else {
        		double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
        		double smallestTemp = Double.parseDouble(smallestSoFar.get("TemperatureF"));
        		if (currentTemp < smallestTemp && currentTemp != -9999){
        			smallestSoFar = currentRow;
        		}
        	}
        }
        return smallestSoFar;
    }

   public void testColdestHourInfile(){
   		FileResource fr = new FileResource();
   		CSVRecord smallest = coldestHourInFile(fr.getCSVParser());
   		System.out.println("coldest temperature was " + smallest.get("TemperatureF") + " at " + smallest.get("DateUTC"));
   } 

   public String fileWithColdestTemperature(){
   		CSVRecord smallestSoFar = null;
   		String coldestTempFile=null;
   		DirectoryResource dr = new DirectoryResource();
   		for (File f : dr.selectedFiles()) {
   			FileResource fr = new FileResource(f);
   			CSVRecord currentRow = coldestHourInFile(fr.getCSVParser()); 
   			if (smallestSoFar == null) {
   				smallestSoFar = currentRow;
   			} else {
   				double currentTemp = Double.parseDouble(currentRow.get("TemperatureF"));
        		double smallestTemp = Double.parseDouble(smallestSoFar.get("TemperatureF"));
        		if (currentTemp < smallestTemp && currentTemp != -9999){
        			smallestSoFar = currentRow;
        			coldestTempFile = f.getName();
        		}
   			}
   		}
   		return coldestTempFile;
   }

   public void testFileWithColdestTemperature(){
   		System.out.println("Coldest day was in file "+ fileWithColdestTemperature());
   		String fullPath = "nc_weather/2013/"+fileWithColdestTemperature();
   		FileResource fr = new FileResource(fullPath);
   		CSVRecord smallest = coldestHourInFile(fr.getCSVParser());
   		System.out.println("coldest temperature was " + smallest.get("TemperatureF") + " at " + smallest.get("DateUTC"));
   		System.out.println("All the Temperatures on the coldest day were: ");
   		for (CSVRecord record : fr.getCSVParser()){
   			System.out.println(record.get("DateUTC") + " : " + record.get("TemperatureF"));
   		}
   }
   
   public CSVRecord lowestHumidityInFile(CSVParser parser){
	   CSVRecord lowestSoFar = null;
	   for (CSVRecord currentRow : parser) {
		   if (lowestSoFar == null && !currentRow.get("Humidity").equals("N/A")){
			   lowestSoFar = currentRow;
		   } else if (!currentRow.get("Humidity").equals("N/A")){
			   double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
			   double lowestHumidity = Double.parseDouble(lowestSoFar.get("Humidity"));
			   
			   if (currentHumidity < lowestHumidity) {
				   lowestSoFar = currentRow;
			   }
		   }
	   }
	   return lowestSoFar;
   }
   
   public void testLowestHumidityInFile(){
	   FileResource fr = new FileResource();
	   CSVRecord csv = lowestHumidityInFile(fr.getCSVParser());
	   System.out.println("Lowest Humidity was " + csv.get("Humidity") + " at " + csv.get("DateUTC"));
   }
   
   public CSVRecord lowestHumidityInManyFiles(){
	   CSVRecord lowestSoFar = null;
		DirectoryResource dr = new DirectoryResource();
		for(File f : dr.selectedFiles()){
			FileResource fr = new FileResource(f);
			CSVRecord currentRow = lowestHumidityInFile(fr.getCSVParser());
			if (lowestSoFar == null && !currentRow.get("Humidity").equals("N/A")){
			   lowestSoFar = currentRow;
		   } else if (!currentRow.get("Humidity").equals("N/A")) {
			   double currentHumidity = Double.parseDouble(currentRow.get("Humidity"));
			   double lowestHumidity = Double.parseDouble(lowestSoFar.get("Humidity"));
			   
			   if (currentHumidity < lowestHumidity) {
				   lowestSoFar = currentRow;
			   }
		   }
		}
		return lowestSoFar;
   }
   
   public void testLowestHumidityInManyFiles(){
	   System.out.println("Lowest Humidity was " + lowestHumidityInManyFiles().get("Humidity") + " at " + lowestHumidityInManyFiles().get("DateUTC"));
   }
   
   public double averageTemperatureInFile(CSVParser parser){
	   double totalTemp=0;
	   double temp = 0;
	   int count=0;
	   for (CSVRecord currentRow : parser){
		   count = count + 1;
		   temp = Double.parseDouble(currentRow.get("TemperatureF"));
		   totalTemp = totalTemp + temp;
	   }
	   if (count == 0){
			return 0;
	   } else {
			return totalTemp/count;
	   }
   }
   
   public void testAverageTemperatureInFile(){
	   FileResource fr = new FileResource();
	   CSVParser parser = fr.getCSVParser();
	   System.out.println("Average temperature in file is " + averageTemperatureInFile(parser));
   }
   
   public double averageTemperatureWithHighHumidityInFile(CSVParser parser, int value){
	   double totalTemp = 0;
	   double temp = 0;
	   int count = 0;
	   for(CSVRecord currentRow : parser){
		   if (Integer.parseInt(currentRow.get("Humidity")) >= value){
				count = count + 1;
				temp = Double.parseDouble(currentRow.get("TemperatureF"));
				totalTemp = totalTemp + temp;
		   }
	   }
	   if (count == 0){
			return 0;
	   } else {
			return totalTemp/count;
	   }
   }
   
   public void testAverageTemperatureWithHighHumidityInFile(){
		FileResource fr = new FileResource();
		CSVParser parser = fr.getCSVParser();
		double averageTemp = averageTemperatureWithHighHumidityInFile(parser, 80);
		if (averageTemp != 0.0){
			System.out.println("Average Temp when high humidity is " + averageTemp);
		} else {
			System.out.println("No temperatures is with that humidity");
		}
		
   }
}
