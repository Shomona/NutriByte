//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application{
	static Model model = new Model();  	//made static to make accessible in the controller
	static View view = new View();		//made static to make accessible in the controller
	static Person person;				//made static to make accessible in the controller


	Controller controller = new Controller();	//all event handlers 


	/**Uncomment the following three lines if you want to try out the full-size data files */
	//static final String PRODUCT_FILE = "data/Products.csv";
	//static final String NUTRIENT_FILE = "data/Nutrients.csv";
	//static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

	/**The following constants refer to the data files to be used for this application */
	static final String PRODUCT_FILE = "data/Nutri2Products.csv";
	static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
	static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

	static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; //Refers to the file holding NutriByte logo image 

	static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

	static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
	static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

	//Dynamic Binding
	ObjectBinding<Person> profileBinding = new ObjectBinding<>() {
		{
			super.bind(view.genderComboBox.valueProperty(), view.ageTextField.textProperty(), view.heightTextField.textProperty(), view.weightTextField.textProperty(), view.physicalActivityComboBox.valueProperty());
		}
		protected Person computeValue() {
			TextField textfield = view.ageTextField;
			String gender, ingredientsToWatch="";
			float age, weight, height, physicalActivityLevel=1f;
			try {
				Person newperson = null;
				gender = NutriByte.view.genderComboBox.getValue();
				if(gender != null) {

					age = Float.parseFloat(textfield.getText().trim());

					textfield = view.weightTextField;

					weight = Float.parseFloat(textfield.getText().trim());

					textfield = view.heightTextField;

					height = Float.parseFloat(textfield.getText().trim());

					String physicalActivityLevelString = NutriByte.view.physicalActivityComboBox.getValue();
					for (NutriProfiler.PhysicalActivityEnum activity : NutriProfiler.PhysicalActivityEnum.values()) {
						if (activity.getName().equals(physicalActivityLevelString))
							physicalActivityLevel = activity.getPhysicalActivityLevel();
					}

					ingredientsToWatch = NutriByte.view.ingredientsToWatchTextArea.getText();

					if(gender.equalsIgnoreCase("Female")) {
						newperson = new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					}
					if(gender.equalsIgnoreCase("Male")) {
						newperson = new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch);
					}


					return newperson;
				}
				return null;
			} catch(NumberFormatException e) {

				return null;
			}
		}
	};




	@Override
	public void start(Stage stage) throws Exception {
		model.readProducts(PRODUCT_FILE);
		model.readNutrients(NUTRIENT_FILE);
		model.readServingSizes(SERVING_SIZE_FILE );
		view.setupMenus();
		view.setupNutriTrackerGrid();
		view.root.setCenter(view.setupWelcomeScene());
		Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
		view.root.setBackground(b);
		Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
		view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
		setupBindings();
		stage.setTitle("NutriByte 2.0");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	void setupBindings() {

		//Menu Controllers
		view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
		view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
		view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
		view.closeNutriProfileMenuItem.setOnAction(event -> view.root.setCenter(view.setupWelcomeScene()));
		view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
		view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());

		//Recommended Nutrients Table Binding
		view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
		view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
		view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

		//Products Table Binding
		view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
		view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
		view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);


		//Button Handlers
		view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
		view.searchButton.setOnAction(controller.new SearchButtonHandler());
		view.clearButton.setOnAction(controller.new ClearButtonHandler());
		view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
		view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());

		//Product ComboBox Binding 
		view.productsComboBox.setOnAction(controller.new ProductsComboBoxListener());


		//Listener for profile binding
		profileBinding.addListener((observable, oldValue, newValue) -> {

			if (newValue != null) {
				if ((newValue.age>0) && (newValue.weight > 0) && (newValue.height > 0) && !controller.lock) {

					person = newValue;
					NutriProfiler.createNutriProfile(person);
					NutriByte.view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
				} 
			} 
		});


		//Event Handlers for colors	
		view.ageTextField.textProperty().addListener((observable, oldValue, newValue) -> {


			if(newValue.matches("-*[0-9]+") || newValue.matches("-*[0-9]+.[0-9]+")) {
				view.ageTextField.setStyle("-fx-text-inner-color: black;");
			}
			else {
				view.ageTextField.setStyle("-fx-text-inner-color: red;");
			}
		});

		view.heightTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			if(newValue.matches("-*[0-9]+")|| newValue.matches("-*[0-9]+.[0-9]+")) view.heightTextField.setStyle("-fx-text-inner-color: black;");
			else view.heightTextField.setStyle("-fx-text-inner-color: red;");
		});

		view.weightTextField.textProperty().addListener((observable, oldValue, newValue) -> {

			if(newValue.matches("-*[0-9]+")|| newValue.matches("-*[0-9]+.[0-9]+")) view.weightTextField.setStyle("-fx-text-inner-color: black;");
			else view.weightTextField.setStyle("-fx-text-inner-color: red;");
		});

	}

	// Callbacks for Recommended Nutrients Table 
	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};


	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
			//write your code here

			float value = arg0.getValue().getNutrientQuantity();
			String numberAsString = String.format ("%.2f", value);
			return new SimpleStringProperty(numberAsString);

		}
	};

	Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {

			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();

		}
	};


	// Callbacks for Products Table
	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientNameProperty();
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {

			float value = arg0.getValue().getNutrientQuantity();
			String numberAsString = String.format ("%.2f", value);
			return new SimpleStringProperty(numberAsString);
		}
	};

	Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
		@Override
		public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {

			Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
			return nutrient.nutrientUomProperty();
		}
	};


}
