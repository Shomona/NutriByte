//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw2;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class CSVFiler extends DataFiler {


	@Override
	public boolean readFile(String filename) {

		CSVFormat csvFormat = CSVFormat.DEFAULT;
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			String gender = "", age = "", weight = "", height = "", physicalActivityLevel = "";
			StringBuilder ingredientsToWatch = new StringBuilder();
			for (CSVRecord csvRecord : csvParser) {

				gender = csvRecord.get(0);
				age = csvRecord.get(1);
				weight = csvRecord.get(2);
				height	= csvRecord.get(3);
				physicalActivityLevel = csvRecord.get(4);

				for(int i = 5 ; i<csvRecord.size(); i++) {
					ingredientsToWatch.append(csvRecord.get(i) + ", " );
				}

			}

			//Rempve the extra comma 
			if (ingredientsToWatch.length() > 0) ingredientsToWatch.replace(ingredientsToWatch.toString().length()-2, ingredientsToWatch.toString().length()-1, "");

			//If either is empty with no zero value to avoid Number format exception return false
			if(gender.equals("") || age.equals("") || weight.equals("") || height.equals("") || physicalActivityLevel.equals("")) return false;

			switch (gender) {
			case "Female": NutriByte.person = 
					new Female(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), Float.parseFloat(physicalActivityLevel), ingredientsToWatch.toString());
			break;
			case "Male": NutriByte.person = 
					new Male(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), Float.parseFloat(physicalActivityLevel), ingredientsToWatch.toString());
			break;
			default: break;
			}

		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

		return true;

	}

	@Override
	public void writeFile(String filename) {

	}


}
