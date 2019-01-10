//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;



public class CSVFiler extends DataFiler {


	@Override
	public boolean readFile(String filename) {

		CSVFormat csvFormat = CSVFormat.DEFAULT;
		try {

			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			;

			//For Profile
			List<CSVRecord> csvRecord = csvParser.getRecords();
			StringBuilder dataProf = new StringBuilder();
			for(int j=0; j<csvRecord.get(0).size(); j++)
				dataProf.append(csvRecord.get(0).get(j).trim() + ", ") ;
			if (dataProf.length() > 0) dataProf.replace(dataProf.toString().length()-2, dataProf.toString().length()-1, "");
			NutriByte.person = validatePersonData(dataProf.toString());


			//For Product
			for (int i =1; i<csvRecord.size(); i++) {

				Product current = null;
				StringBuilder dataAlt = new StringBuilder();

				for(int j=0; j<csvRecord.get(i).size(); j++)
					dataAlt.append(csvRecord.get(i).get(j).trim() + ", ") ;


				if (dataAlt.length() > 0) dataAlt.replace(dataAlt.toString().length()-2, dataAlt.toString().length()-1, "");



				try {
					current = validateProductData(dataAlt.toString());
				}
				catch(InvalidProfileException e) {
					continue;
				}

				NutriByte.person.dietProductsList.add(current);

			}

			NutriByte.person.populateDietNutrientMap();




		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

		return true;

	}

	@Override
	public void writeFile(String filename) {


		//Delimiter used in CSV file
		final String NEW_LINE_SEPARATOR = "\n";

		FileWriter fileWriter = null;

		CSVPrinter csvFilePrinter = null;

		//Create the CSVFormat object with "\n" as a record delimiter
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

		try {

			//initialize FileWriter object
			fileWriter = new FileWriter(filename);

			//initialize CSVPrinter object 
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);



			//Write the profile object list to the CSV file
			List<String> profileDataRecord = new ArrayList<String>();
			profileDataRecord.add(NutriByte.person instanceof Male ? "Male" : "Female");
			profileDataRecord.add(Float.toString(NutriByte.person.age));
			profileDataRecord.add(Float.toString(NutriByte.person.weight));
			profileDataRecord.add(Float.toString(NutriByte.person.height));
			profileDataRecord.add(Float.toString(NutriByte.person.physicalActivityLevel));
			String toWatch[] = NutriByte.person.ingredientsToWatch.split(",");
			for(String ingredient : toWatch) {
				profileDataRecord.add(ingredient);
			}

			csvFilePrinter.printRecord(profileDataRecord);

			//Write the diet products 
			for(Product current: NutriByte.person.dietProductsList) {
				List<String> productDataRecord = new ArrayList<String>();
				productDataRecord.add(current.getNdbNumber());
				productDataRecord.add(Float.toString(current.getServingSize()));
				productDataRecord.add(Float.toString(current.getHouseholdSize()));
				csvFilePrinter.printRecord(productDataRecord);
			}




		}
		catch (Exception e) {
			System.out.println("Error in CsvFileWriter!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter!");
				e.printStackTrace();
			}
		}


	}


	public Person validatePersonData(String data) {
		String gender = "", age = "", weight = "", height = "", physicalActivityLevel = "";
		StringBuilder ingredientsToWatch = new StringBuilder();
		String csvRecord[] = data.split(",");
		Person readPerson = null;

		gender = csvRecord[0].trim();

		//Exception Handling for gender

		if(!(gender.equalsIgnoreCase("Male") || gender.equalsIgnoreCase("Female"))) {
			throw new InvalidProfileException("The profile must have gender: Female or Male as first word");
		}

		age = csvRecord[1].trim();
		//Exception Handling for age
		if(!(age.matches("-*[0-9]+") || age.matches("-*[0-9]+.[0-9]+") )) {
			throw new InvalidProfileException("Invalid data for Age: "+age+"\n Age must be a number");
		}
		else if(age.contains("-")) {
			throw new InvalidProfileException("Age must be a positive number");
		}


		weight = csvRecord[2].trim();
		//Exception Handling for weight
		if(!(weight.matches("-*[0-9]+") || weight.matches("-*[0-9]+.[0-9]+") )) {
			throw new InvalidProfileException("Invalid data for Weight : "+weight+"\n Weight must be a number");
		}
		else if(age.contains("-")) {
			throw new InvalidProfileException("Weight  must be a positive number");
		}


		height	= csvRecord[3].trim();
		//Exception Handling for height
		if(!(height.matches("-*[0-9]+") || height.matches("-*[0-9]+.[0-9]+") )) {
			throw new InvalidProfileException("Invalid data for Weight : "+height+"\n Height must be a number");
		}
		else if(height.contains("-")) {
			throw new InvalidProfileException("Height  must be a positive number");
		}

		physicalActivityLevel = csvRecord[4].trim();
		//If either is empty with no zero value to avoid Number format exception return false
		if(gender.equals("") || age.equals("") || weight.equals("") || height.equals("") || physicalActivityLevel.equals("")) return null;

		boolean paLevel = false;
		//Exception Handling for physicalActivityLevel
		for( NutriProfiler.PhysicalActivityEnum paEnum : NutriProfiler.PhysicalActivityEnum.values()) {

			if(paEnum.getPhysicalActivityLevel() == Float.parseFloat(physicalActivityLevel)) {
				paLevel = true;
				break;
			}
		}


		if(!paLevel) {
			throw new InvalidProfileException("Invalid physical activity level: "+physicalActivityLevel+"\n Must be: 1.0, 1.1, 1.25, or 1.48");
		}


		for(int i = 5 ; i<csvRecord.length; i++) {
			ingredientsToWatch.append(csvRecord[i] + ", " );
		}

		//Create profile
		//Rempve the extra comma 
		if (ingredientsToWatch.length() > 0) ingredientsToWatch.replace(ingredientsToWatch.toString().length()-2, ingredientsToWatch.toString().length()-1, "");


		switch (gender) {
		case "Female": readPerson = 
				new Female(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), Float.parseFloat(physicalActivityLevel), ingredientsToWatch.toString());
		break;
		case "Male": readPerson = 
				new Male(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), Float.parseFloat(physicalActivityLevel), ingredientsToWatch.toString());
		break;
		default: break;
		}

		return readPerson;


	}

	public Product validateProductData(String data) {

		Product current = null;
		String pid= "";
		String serving = "";
		String household = "";

		String csvRecord[] = data.split(",");

		for(int i=0; i<csvRecord.length; i++) {
			//Expected to be product
			if(i==0) {
				pid = csvRecord[i].trim();
				if(!Model.productsMap.containsKey(pid)) throw new InvalidProfileException("No product with the code: "+pid);
			}
			else {
				//Ecpected to be either serving size or household size
				if(csvRecord[i].trim().equals("") || !(csvRecord[i].trim().matches("[0-9]+") || csvRecord[i].trim().matches("[0-9]+.[0-9]+"))) {
					throw new InvalidProfileException("Cannot read: "+data+"\nThe data must be - String, number, number - for ndb number, serving size, household size");
				}

			}
		}

		if(csvRecord.length !=3) throw new InvalidProfileException("Cannot read: "+data+"\nThe data must be - String, number, number - for ndb number, serving size, household size");
		serving = csvRecord[1].trim();
		household = csvRecord[2].trim();


		for(String key : Model.productsMap.keySet()) {
			Product product = Model.productsMap.get(key);
			if(product.getNdbNumber().equals(pid)) {
				current = new Product(product.getNdbNumber(), product.getProductName(), product.getManufacturer(), product.getIngredients());
				current.setServingSize(Float.parseFloat(serving)); 
				current.setHouseholdSize(Float.parseFloat(household)); 
				current.setServingUom(product.getServingUom());
				current.setHouseholdUom(product.getHouseholdUom());
				current.setProductNutrients(product.getProductNutrients());
			}
		}

		return current;
	}

}
