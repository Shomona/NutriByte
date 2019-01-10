//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw2;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import hw2.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Model {

	public static ObservableMap<String, Product> productsMap = FXCollections.observableHashMap();
	public static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableHashMap();

	public void readProducts(String filename) {

		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				Product product = new Product(csvRecord.get(0), csvRecord.get(1),
						csvRecord.get(4), csvRecord.get(7));
				productsMap.put(csvRecord.get(0), product);
			}



		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

	}

	public void readNutrients(String filename) {


		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {

				Product currentProduct = productsMap.get(csvRecord.get(0));

				//Create a Nutrient object if the nutrient is unique and add it to the map
				Nutrient nutrient = new Nutrient(csvRecord.get(1), csvRecord.get(2), csvRecord.get(5));
				nutrientsMap.put(csvRecord.get(1), nutrient);

				//Create a ProductNutrient object and append to this currentProduct map if the qty>0
				ObservableMap<String, ProductNutrient> returnedMap = currentProduct.getProductNutrients();
				if(csvRecord.get(4).equals("") ||csvRecord.get(4).trim().equals("0.0")) continue;
				Product.ProductNutrient pnutrient = currentProduct.new ProductNutrient(csvRecord.get(1), Float.parseFloat(csvRecord.get(4)));
				returnedMap.put(csvRecord.get(1), pnutrient);
				currentProduct.setProductNutrients(returnedMap);


			}



		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

	}

	public void readServingSizes(String filename) {


		CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
		try {
			CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
			for (CSVRecord csvRecord : csvParser) {


				Product currentProduct = productsMap.get(csvRecord.get(0));
				currentProduct.setServingSize(csvRecord.get(1).equals("")?0.0f:Float.parseFloat(csvRecord.get(1)));
				currentProduct.setServingUom(csvRecord.get(2));
				currentProduct.setHouseholdSize(csvRecord.get(3).equals("")?0.0f:Float.parseFloat(csvRecord.get(3)));
				currentProduct.setHouseholdUom(csvRecord.get(4));


			}



		}

		catch (FileNotFoundException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }

	}

	public boolean readProfiles(String filename) {

		String extension = "";
		int index = filename.lastIndexOf('.');

		boolean readComplete = false;

		if (index > 0) {
			extension = filename.substring(index+1);
		}

		if(extension.equalsIgnoreCase("xml")) {
			DataFiler d = new XMLFiler();
			readComplete = d.readFile(filename);
		}
		else {
			DataFiler d = new CSVFiler();
			readComplete = d.readFile(filename);
		}

		return readComplete;
	}

	public static void main(String args[]) {
		Model m = new Model();

	}


}
