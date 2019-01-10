//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import hw3.Product.ProductNutrient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


public class Controller {

	boolean lock = false;
	class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {	
			//write your code here

			// Entered values
			String gender = NutriByte.view.genderComboBox.getValue();
			String age = NutriByte.view.ageTextField.getText();
			String weight = NutriByte.view.weightTextField.getText();
			String height = NutriByte.view.heightTextField.getText();
			String physicalActivityLevel = NutriByte.view.physicalActivityComboBox.getValue();
			String ingredientsToAvoid = NutriByte.view.ingredientsToWatchTextArea.getText();
			Person person = null;

			float paLevel = 0.0f;


			try{
				//Checking for valid data 
				if(gender == null ||gender.equals("")) throw new InvalidProfileException("Missing gender information");


				if(age.equals("")) throw new InvalidProfileException("Missing age information");
				else if(!(age.matches("-*[0-9]+") || age.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect age input. Must be a number");
				else if(age.contains("-")) throw new InvalidProfileException("Age must be a positive number");


				if(weight.equals("")) throw new InvalidProfileException("Missing weight information");
				else if(!(weight.matches("-*[0-9]+") || weight.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect weight input. Must be a number");
				else if(weight.contains("-")) throw new InvalidProfileException("Weight must be a positive number");

				if(height.equals("")) throw new InvalidProfileException("Missing height information");
				else if(!(height.matches("-*[0-9]+") || height.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect height input. Must be a number");
				else if(height.contains("-")) throw new InvalidProfileException("Height must be a positive number");

				if(physicalActivityLevel == null || physicalActivityLevel.equals("")) paLevel = 1;
				else {

					//Map phsyicalActivityLevel to enum		
					for( NutriProfiler.PhysicalActivityEnum paEnum : NutriProfiler.PhysicalActivityEnum.values()) {

						if(paEnum.getName().equalsIgnoreCase(physicalActivityLevel)) {
							paLevel = paEnum.getPhysicalActivityLevel();
							break;
						}
					}
				}




				if(gender.equalsIgnoreCase("Male")) person = new Male(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), paLevel, ingredientsToAvoid);		
				else person = new Female(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), paLevel, ingredientsToAvoid);

				NutriProfiler.createNutriProfile(person);
				NutriByte.view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
				NutriByte.person = person;

			}
			catch(InvalidProfileException e) {
				System.out.println(e.getMessage());
			}
		}			
	}

	class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			//write your code here

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select File");
			File file = fileChooser.showOpenDialog(NutriByte.view.root.getScene().getWindow());

			//File selected so clear previous data
			if(file !=null) {
				NutriByte.view.initializePrompts();
				NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList());
				NutriByte.view.dietProductsTableView.setItems(FXCollections.observableArrayList());
				NutriByte.view.recommendedNutrientsTableView.setItems(FXCollections.observableArrayList());
				NutriByte.view.nutriChart.clearChart();
				NutriByte.view.productsComboBox.setItems(FXCollections.observableArrayList());
				NutriByte.view.productIngredientsTextArea.clear();

				//Clear Labels
				NutriByte.view.searchResultSizeLabel.setText("");
				NutriByte.view.servingSizeLabel.setText("0.00");
				NutriByte.view.householdSizeLabel.setText("0.0");
				NutriByte.view.dietServingUomLabel.setText("");
				NutriByte.view.dietHouseholdUomLabel.setText("");
				NutriByte.view.dietHouseholdSizeTextField.setText("");
				NutriByte.view.dietServingSizeTextField.setText("");
				//Used to store boolean value returned by readProfiles 
				boolean readData = false;

				try {
					readData = NutriByte.model.readProfiles(file.getAbsolutePath());

					if(readData);
					{	//Disable the handler
						lock = true;
						//sessionOpen = true;

						//Populating profile
						NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
						NutriProfiler.createNutriProfile(NutriByte.person); 
						NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
						NutriByte.view.genderComboBox.setValue(NutriByte.person instanceof Male ? "Male" : "Female");
						NutriByte.view.ageTextField.setText(String.valueOf(NutriByte.person.age));
						NutriByte.view.weightTextField.setText(String.valueOf(NutriByte.person.weight));
						NutriByte.view.heightTextField.setText(String.valueOf(NutriByte.person.height));

						for( NutriProfiler.PhysicalActivityEnum paEnum : NutriProfiler.PhysicalActivityEnum.values()) {

							if(paEnum.getPhysicalActivityLevel() == NutriByte.person.physicalActivityLevel) {
								NutriByte.view.physicalActivityComboBox.setValue(paEnum.getName());
								break;
							}
						}

						NutriByte.view.ingredientsToWatchTextArea.setText(String.valueOf(NutriByte.person.ingredientsToWatch));


						//Set the combobox
						NutriByte.view.productsComboBox.setItems(NutriByte.person.dietProductsList);

						ObservableList<Product.ProductNutrient> ol = FXCollections.observableArrayList();
						//Populate existing products only if products exist
						if(NutriByte.person.dietProductsList.size() > 0) {
							ol.addAll(NutriByte.person.dietProductsList.get(0).getProductNutrients().values());
							NutriByte.view.productNutrientsTableView.setItems(ol);
							NutriByte.view.productIngredientsTextArea.setText(NutriByte.person.dietProductsList.get(0).getIngredients());

						}

						//Show the products in diet product list
						NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);

						//Trigger Map
						NutriByte.view.nutriChart.updateChart();
						NutriByte.view.productsComboBox.getSelectionModel().selectFirst();
					}
				}
				catch(InvalidProfileException e) {
					try {
						throw new InvalidProfileException("Could not read profile data");
					}
					catch(InvalidProfileException elo) {
						System.out.println(e.getMessage());
					}
				}
			}
			lock = false;
		}

	}

	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here

			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList());
			NutriByte.view.dietProductsTableView.setItems(FXCollections.observableArrayList());
			NutriByte.view.recommendedNutrientsTableView.setItems(FXCollections.observableArrayList());
			NutriByte.view.nutriChart.clearChart();
			NutriByte.view.productsComboBox.setItems(FXCollections.observableArrayList());
			NutriByte.view.productIngredientsTextArea.clear();


			//Clear Labels
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.0");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");
			NutriByte.view.dietHouseholdSizeTextField.setText("");
			NutriByte.view.dietServingSizeTextField.setText("");
			
			
			//Additional textfield
			NutriByte.view.productSearchTextField.setText("");
			NutriByte.view.nutrientSearchTextField.setText("");
			NutriByte.view.ingredientSearchTextField.setText("");

		}
	}


	class AboutMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("About");
			alert.setHeaderText("NutriByte");
			alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
			Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
			ImageView imageView = new ImageView();
			imageView.setImage(image);
			imageView.setFitWidth(300);
			imageView.setPreserveRatio(true);
			imageView.setSmooth(true);
			alert.setGraphic(imageView);
			alert.showAndWait();
		}
	}

	/**
	 * New event handlers
	 */

	class SaveMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here
			//Throw errors and dont allow saving if values aren't consistent 
			String gender = NutriByte.view.genderComboBox.getValue();
			String age = NutriByte.view.ageTextField.getText();
			String weight = NutriByte.view.weightTextField.getText();
			String height = NutriByte.view.heightTextField.getText();


			//Checking for valid data 
			try { 


				if(gender == null ||gender.equals("")) throw new InvalidProfileException("Missing gender information");

				if(age.equals("")) throw new InvalidProfileException("Missing age information");
				else if(!(age.matches("-*[0-9]+") || age.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect age input. Must be a number");
				else if(age.contains("-")) throw new InvalidProfileException("Age must be a positive number");


				if(weight.equals("")) throw new InvalidProfileException("Missing weight information");
				else if(!(weight.matches("-*[0-9]+") || weight.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect weight input. Must be a number");
				else if(weight.contains("-")) throw new InvalidProfileException("Weight must be a positive number");

				if(height.equals("")) throw new InvalidProfileException("Missing height information");
				else if(!(height.matches("-*[0-9]+") || height.matches("-*[0-9]+.[0-9]+") )) throw new InvalidProfileException("Incorrect height input. Must be a number");
				else if(height.contains("-")) throw new InvalidProfileException("Height must be a positive number");



				//Open the file chooser and save
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save File");
				File file = fileChooser.showSaveDialog(NutriByte.view.root.getScene().getWindow());
				NutriByte.model.writeProfiles(file.getAbsolutePath());
			}
			catch(InvalidProfileException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	class SearchButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here

			String productName = NutriByte.view.productSearchTextField.getText();
			String nutrientName = NutriByte.view.nutrientSearchTextField.getText();
			String ingredientName = NutriByte.view.ingredientSearchTextField.getText();
			boolean productNameExists = productName.equals("")?false: true;
			boolean nutrientNameExists = nutrientName.equals("")?false: true;
			boolean ingredientNameExists = ingredientName.equals("")?false: true;
			boolean moreProcessing = false;

			ObservableList<Product> productOptions = FXCollections.observableArrayList();


			Set<String> keys = Model.productsMap.keySet();
			Iterator<String> it = keys.iterator();

			for (int i=0 ;i<keys.size(); i++) {

				String proId = it.next();

				Product current = Model.productsMap.get(proId);


				//Stage 1: Processing Based on product
				// Check if product name present or if none of the fields are present
				if(productNameExists || !(productNameExists || nutrientNameExists || ingredientNameExists)) {
					if(current.getProductName().toUpperCase().contains(productName.toUpperCase())) {

						//Add and continue only if there is no nutrient and ingredient Name 
						if(nutrientNameExists == false && ingredientNameExists == false ) {
							productOptions.add(current);
							moreProcessing = false;
							continue;
						}
						//In case of more specification set moreProcessing to true to signify further processing
						else {
							moreProcessing = true;
						}


					}
					else {
						moreProcessing = false;
						continue;
					}

				}
				else {
					moreProcessing = true;
				}


				//Stage 2: Processing Based on nutrient
				if(moreProcessing) {

					//Check if nutrients present or if none of the fields are present
					if(nutrientNameExists || !(productNameExists || nutrientNameExists || ingredientNameExists)) {
						ObservableMap<String, Product.ProductNutrient> nutrientMap = current.getProductNutrients();
						for (String nutriId : nutrientMap.keySet()) {

							String nutrientMapName = Model.nutrientsMap.get(nutriId).getNutrientName();
							if(nutrientMapName.toUpperCase().contains(nutrientName.toUpperCase())){

								//Add and break from inner nutrient loop only if there is no ingredient Name
								//Set an internal flag toContune the outer product Loop
								if(ingredientNameExists == false) {
									productOptions.add(current);
									moreProcessing = false;
								}				
								//In case of more specification set moreProcessing to true to signify further processing
								else {
									moreProcessing = true;
								}



							}
						}
						//No more processing required for this product hence continue
						if(!moreProcessing) continue;
					}
					else {
						moreProcessing = true;
					}

					//Stage 3: Processing Based on nutrient
					if(moreProcessing) {
						//Check if ingredients present or if none of the fields are present
						if(ingredientNameExists || !(productNameExists || nutrientNameExists || ingredientNameExists)) {
							String ingredients = current.getIngredients();
							if(ingredients.contains(ingredientName.toUpperCase()+",")) {
								productOptions.add(current);
								moreProcessing = false;

							}
						}
					}
				}

				//Resetting moreProcessing to default before starting of next iteration
				moreProcessing = false;
			}

			NutriByte.view.productsComboBox.setItems(productOptions);


			NutriByte.view.productsComboBox.getSelectionModel().selectFirst();



		}
	}

	class ClearButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here

			NutriByte.view.productSearchTextField.clear();
			NutriByte.view.nutrientSearchTextField.clear();
			NutriByte.view.ingredientSearchTextField.clear();
			NutriByte.view.productsComboBox.setItems(FXCollections.observableArrayList());
			NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList());
			NutriByte.view.productIngredientsTextArea.clear();

			//Clear Labels
			NutriByte.view.searchResultSizeLabel.setText("");
			NutriByte.view.servingSizeLabel.setText("0.00");
			NutriByte.view.householdSizeLabel.setText("0.0");
			NutriByte.view.dietServingUomLabel.setText("");
			NutriByte.view.dietHouseholdUomLabel.setText("");

		}
	}

	class ProductsComboBoxListener implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here 

			Product current = NutriByte.view.productsComboBox.getValue();
			if(current == null) return;

			ObservableMap<String, ProductNutrient> productNutrients = current.getProductNutrients();

			//Convert map to array list
			ObservableList<Product.ProductNutrient> onutrientList =FXCollections.observableArrayList(productNutrients.values());


			NutriByte.view.productNutrientsTableView.setItems(onutrientList);
			NutriByte.view.productIngredientsTextArea.setText(current.getIngredients());

			//Labels
			String childrenSize = Integer.toString(NutriByte.view.productsComboBox.getItems().size());
			NutriByte.view.searchResultSizeLabel.setText(childrenSize + " product(s) found");
			NutriByte.view.servingSizeLabel.setText(Float.toString(current.getServingSize())+" "+ current.getServingUom());
			NutriByte.view.householdSizeLabel.setText(Float.toString(current.getHouseholdSize()) +" "+ current.getHouseholdUom());
			NutriByte.view.dietServingUomLabel.setText(current.getServingUom());
			NutriByte.view.dietHouseholdUomLabel.setText(current.getHouseholdUom());




		}
	}


	class AddDietButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {

			//write your code here

			if(NutriByte.person ==null) return;
			Product currentCombo = NutriByte.view.productsComboBox.getValue();

			String multiplierHousehold = NutriByte.view.dietHouseholdSizeTextField.getText();
			String multiplierServing = NutriByte.view.dietServingSizeTextField.getText();

			float factor = 1.0f;

			if(multiplierServing.equals("")) {

				//Check if house hold size is a non zero vale then proceed with the getting the factpr
				if(!multiplierHousehold.equals("") && currentCombo.getHouseholdSize()>0) {
					factor = Float.parseFloat(multiplierHousehold)/ currentCombo.getHouseholdSize();
				}
				//Else the factor should be the text input itself
				else if(currentCombo.getHouseholdSize() == 0) factor = Float.parseFloat(multiplierHousehold);
			}
			else {
				factor = Float.parseFloat(multiplierServing)/ currentCombo.getServingSize();
			}


			float newServingSize = currentCombo.getServingSize() * factor;
			float newHouseholdSize = (currentCombo.getHouseholdSize() == 0)? factor :currentCombo.getHouseholdSize() * factor;

			Product current = new Product(currentCombo.getNdbNumber(), currentCombo.getProductName(), currentCombo.getManufacturer(), currentCombo.getIngredients());
			current.setServingSize(newServingSize);
			current.setHouseholdSize(newHouseholdSize);
			current.setServingUom(currentCombo.getServingUom());
			current.setHouseholdUom(currentCombo.getHouseholdUom());
			current.setProductNutrients(currentCombo.getProductNutrients());


			//To add to the diet table
			NutriByte.person.dietProductsList.add(current);
			ObservableList<Product> prod = FXCollections.observableArrayList();
			prod.addAll(NutriByte.person.dietProductsList);
			NutriByte.view.dietProductsTableView.setItems(prod);

			NutriByte.person.populateDietNutrientMap();

			NutriByte.view.nutriChart.updateChart();





		}
	}

	class RemoveDietButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here


			Product selected = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedItem();
			List<Product> productTableView = NutriByte.view.dietProductsTableView.getItems();
			int index = productTableView.indexOf(selected);

			//Remove from diet table view
			if(NutriByte.person.dietProductsList.size() >0 && selected!=null) {


				//productTableView.remove(selected);

				//Remove from diet List as well
				NutriByte.person.dietProductsList.remove(index);
				ObservableList<Product> prod = FXCollections.observableArrayList();
				prod.addAll(NutriByte.person.dietProductsList);
				NutriByte.view.dietProductsTableView.setItems(prod);
				
				if(NutriByte.person.dietProductsList.size() == 0) NutriByte.view.nutriChart.clearChart();
				else{
					//Re calculate the nutrient map

					NutriByte.person.populateDietNutrientMap();

					//Update chart
					NutriByte.view.nutriChart.updateChart();
				}
			}
		}
	}

}
