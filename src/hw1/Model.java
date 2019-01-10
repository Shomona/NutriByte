//Name: Shomona Mukherjee
//Andrew Id: shomonam
package hw1;

import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileNotFoundException;

public class Model {
	Product products[];
	Nutrient nutrients[];
	ProductNutrient productNutrients[];

	public void readProducts(String filename){
		Scanner in;
		try {
			in = new Scanner(new File(filename));
			StringBuilder productBuilder = new StringBuilder();

			in.nextLine();

			while(in.hasNextLine()) {
				productBuilder.append(in.nextLine()+"\n");
			}
			in.close();
			String productLines[] = productBuilder.toString().split("\n");
			products = new Product[productLines.length];


			int i = 0;
			/* Iterate over each line
			 * Split the line based on only external comma
			 * Create a product object and store it in products array */
			for(String productRow : productLines) {
				String productDetails[] = productRow.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				//Removing the outside quotes from each value 
				productDetails[0] = productDetails[0].substring(1,productDetails[0].length()-1);
				productDetails[1] = productDetails[1].substring(1,productDetails[1].length()-1);
				productDetails[4] = productDetails[4].substring(1,productDetails[4].length()-1);
				productDetails[7] = productDetails[7].substring(1,productDetails[7].length()-1);


				products[i] = new Product(productDetails[0], productDetails[1], productDetails[4], productDetails[7]);
				i++;

			}



		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		

	}

	public void readNutrients(String filename) {
		Scanner in;
		try {
			in = new Scanner(new File(filename));
			StringBuilder nutrientBuilder = new StringBuilder();
			StringBuilder uniqueNutrients = new StringBuilder();

			in.nextLine();

			while(in.hasNextLine()) {
				nutrientBuilder.append(in.nextLine()+"\n");
			}
			in.close();
			String nutrientLines[] = nutrientBuilder.toString().split("\n");
			productNutrients = new ProductNutrient[nutrientLines.length];

			int i = 0;
			/* Iterate over each line
			 * Split the line based on only external comma
			 * Create a ProductNutrient object and store it in productNutrients array */
			for(String nutrientRow : nutrientLines) {
				String nutrientDetails[] = nutrientRow.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				//Removing the outside quotes from each value 
				nutrientDetails[0] = nutrientDetails[0].substring(1,nutrientDetails[0].length()-1);
				nutrientDetails[1] = nutrientDetails[1].substring(1,nutrientDetails[1].length()-1);
				nutrientDetails[2] = nutrientDetails[2].substring(1,nutrientDetails[2].length()-1);
				nutrientDetails[4] = nutrientDetails[4].substring(1,nutrientDetails[4].length()-1);
				nutrientDetails[5] = nutrientDetails[5].substring(1,nutrientDetails[5].length()-1);


				productNutrients[i] = new ProductNutrient(nutrientDetails[0], nutrientDetails[1], nutrientDetails[2], nutrientDetails[4].equals("")?0.0f:Float.parseFloat(nutrientDetails[4]), nutrientDetails[5]);
				i++;
				if(uniqueNutrients.toString().contains(nutrientDetails[1])) continue;
				uniqueNutrients.append(nutrientDetails[1]+";"+nutrientDetails[2]+";"+nutrientDetails[5]+"\n");

			}
			// Populate the nutrients array for unique nutrients
			String unique_nutrients[] = uniqueNutrients.toString().split("\n");
			nutrients = new Nutrient[unique_nutrients.length];
			int j = 0;
			for(String nutrient : unique_nutrients) {
				String udetails[] = nutrient.split(";");
				nutrients[j] = new Nutrient(udetails[0], udetails[1], udetails[2]);
				j++;

			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readServingSizes(String filename) {
		Scanner in;
		try {
			in = new Scanner(new File(filename));
			StringBuilder servingBuilder = new StringBuilder();

			in.nextLine();

			while(in.hasNextLine()) {
				servingBuilder.append(in.nextLine()+"\n");
			}
			in.close();
			String servingLines[] = servingBuilder.toString().split("\n");

			/* Iterate over each line
			 * Split the line based on only external comma
			 * Assign the remaining member variables of ProductNutrient class */
			for(String servingRow : servingLines) {
				String servingDetails[] = servingRow.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

				//Removing the outside quotes from each value 
				servingDetails[0] = servingDetails[0].substring(1,servingDetails[0].length()-1);
				servingDetails[1] = servingDetails[1].substring(1,servingDetails[1].length()-1);
				servingDetails[2] = servingDetails[2].substring(1,servingDetails[2].length()-1);
				servingDetails[3] = servingDetails[3].substring(1,servingDetails[3].length()-1);
				servingDetails[4] = servingDetails[4].substring(1,servingDetails[4].length()-1);

				for(Product product: products) {

					if(product.ndbNumber.equals(servingDetails[0])) {
						product.servingSize = servingDetails[1].equals("")?0.0f:Float.parseFloat(servingDetails[1]);
						product.servingUom = servingDetails[2];
						product.householdSize = servingDetails[3].equals("")?0.0f:Float.parseFloat(servingDetails[3]);
						product.householdUom = servingDetails[4];
					}
				}

			}


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




}
