//Name: Shomona Mukherjee
//Andrew Id: shomonam
package hw1;

import java.util.Arrays;
import java.util.Scanner;

public class NutriByte {
	Model model = new Model();  				//will handle all data read from the data files
	Scanner input = new Scanner(System.in);  	//to be used for all console i/o in this class

	static final String PRODUCT_FILE = "data/SampleProducts.csv";
	static final String NUTRIENT_FILE = "data/SampleNutrients.csv";
	static final String SERVING_SIZE_FILE = "data/SampleServingSize.csv";

	//do not change this method
	public static void main(String[] args) {
		NutriByte nutriByte = new NutriByte();
		nutriByte.model.readProducts(PRODUCT_FILE);
		nutriByte.model.readNutrients(NUTRIENT_FILE);
		nutriByte.model.readServingSizes(SERVING_SIZE_FILE );
		switch (nutriByte.getMenuChoice()) {
		case 1: {
			nutriByte.printSearchResults(nutriByte.searchProductsWithSelectedIngredients(nutriByte.getIngredientChoice()));
			break;
		}
		case 2: {
			int nutrientChoice = nutriByte.getNutrientChoice();
			nutriByte.printSearchResults(nutriByte.searchProductsWithSelectedNutrient(nutrientChoice), nutrientChoice);
			break;
		}
		case 3:  
		default: System.out.println("Good Bye!"); break;
		}
	}

	//do not change this method
	int getMenuChoice() {
		System.out.println("*** Welcome to NutriByte ***");
		System.out.println("--------------------------------------------------");
		System.out.println("1. Find ingredient(s)");
		System.out.println("2. Find a nutrient");
		System.out.println("3. Exit");
		input = new Scanner(System.in);
		return input.nextInt();
	}

	//do not change this method
	String getIngredientChoice() {
		input.nextLine();
		System.out.println("Type the keywords to search for the ingredients");
		System.out.println("--------------------------------------------------");
		return input.nextLine();
	}

	int getNutrientChoice() {
		//write your code here
		input.nextLine();
		System.out.println("Select the nutrient you are looking for");
		System.out.println("--------------------------------------------------");
		for(int i =0; i<this.model.nutrients.length ;i++) {
			System.out.printf("%-40s"+((i+1)%3==0?"%n":""), i+1 + "." + model.nutrients[i].nutrientName);
		}
		System.out.println();
		System.out.println("--------------------------------------------------");
		return Integer.parseInt(input.nextLine());

	}


	Product[] searchProductsWithSelectedIngredients(String searchString) {
		//write your code here
		int i =0;
		StringBuilder sb = new StringBuilder();
		String search[] = searchString.split(" ");
		//Go over every search ingredient entered 
		for(String searchValue : search) {
			//Search all the products for the selected ingredient 
			for(Product product: model.products) {
				//Select and add product ndbNumber to String Builder if the product ingredient contains the serached ingredient 
				if(product.ingredients.toUpperCase().contains(searchValue.toUpperCase())) {
					if(sb.toString().contains(product.ndbNumber)) continue;
					sb.append(product.ndbNumber+"\n");
				}
			}		
		}
		if(sb.toString().equals("")) return new Product[0];
		String selected[] = sb.toString().split("\n");
		Product ingridientProd[] = new Product[selected.length];
		//Add the products for the selected product ndbNumbers 		
		for(String selectedId : selected) {
			for(Product product: model.products) {
				if(product.ndbNumber.equalsIgnoreCase(selectedId)) {
					ingridientProd[i] = product;
					i++;
				}
			}
		}


		return ingridientProd;
	}

	Product[] searchProductsWithSelectedNutrient(int menuChoice) {
		//write your code here
		StringBuilder sb = new StringBuilder();

		for(ProductNutrient productnutrient: model.productNutrients) {
			//Select and add product ndbNumber to String Builder if the nutrient matches and its quantity is greater than 0.0
			if((productnutrient.nutrientName.toUpperCase().equalsIgnoreCase(model.nutrients[menuChoice-1].nutrientName)) && productnutrient.quantity>0.0f) {
				if(sb.toString().contains(productnutrient.ndbNumber)) continue;
				sb.append(productnutrient.ndbNumber+"\n");
			}
		}

		int i =0;
		if(sb.toString().equals("")) return new Product[0];
		String selected[] = sb.toString().split("\n");
		Product nutrientProd[] = new Product[selected.length];
		//Add the products for the selected product ndbNumbers 
		for(String selectedId : selected) {
			for(Product product: model.products) {
				if(product.ndbNumber.equalsIgnoreCase(selectedId)) {
					nutrientProd[i] = product;
					i++;
				}
			}
		}
		return nutrientProd;
	}

	void printSearchResults(Product[] searchResults) {
		//write your code here
		int i =1;
		if(searchResults.length == 0) System.out.println("*** 0 products found ***");
		else {
			System.out.println("*** "+ searchResults.length +" products found ***");
			for(Product product: searchResults) {
				System.out.println(i+". "+product.productName+" from "+product.manufarurer);
				i++;
			}
		}
		System.out.println("--------------------------------------------------");
	}

	void printSearchResults(Product[] searchResults, int nutrientChoice) {
		//write your code here
		int i =1;
		System.out.println("*** "+ searchResults.length +" products found ***");
		for(Product product: searchResults) {
			for(ProductNutrient pnutrient : model.productNutrients) {
				if(product.ndbNumber.equals(pnutrient.ndbNumber) && pnutrient.nutrientName.equalsIgnoreCase(model.nutrients[nutrientChoice-1].nutrientName)) {
					float value = (pnutrient.quantity * product.servingSize)/100;
					System.out.printf(" %2d. %.2f %s of %s: %s has %.2f%s of %s %n",i ,product.householdSize, product.householdUom, product.ndbNumber, product.productName, value, pnutrient.nutrientUom, model.nutrients[nutrientChoice-1].nutrientName );
					i++;
				}
			}
		}
	}
}
