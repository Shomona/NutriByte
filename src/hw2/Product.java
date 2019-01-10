//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw2;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product implements java.io.Serializable{


	private StringProperty ndbNumber = new SimpleStringProperty(); 
	private StringProperty productName = new SimpleStringProperty();
	private StringProperty manufacturer = new SimpleStringProperty();
	private StringProperty ingredients = new SimpleStringProperty();
	private FloatProperty servingSize = new SimpleFloatProperty();
	private StringProperty servingUom = new SimpleStringProperty();
	private FloatProperty householdSize = new SimpleFloatProperty();
	private StringProperty householdUom = new SimpleStringProperty();
	private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();

	public Product() {
		this.ndbNumber.set("");
		this.productName.set("");
		this.manufacturer.set("");
		this.ingredients.set("");
		this.servingUom.set("");
		this.householdUom.set("");
	}

	public Product(String ndbNumber, String productName, String manufacturer, String ingredients ) {
		this.ndbNumber.set(ndbNumber);
		this.productName.set(productName);
		this.manufacturer.set(manufacturer);
		this.ingredients.set(ingredients);

	}


	//TODO : Create Property getter and setter as well if needed. 

	/**
	 * @return the ndbNumber
	 */
	public String getNdbNumber() {
		return ndbNumber.getValue();
	}

	/**
	 * @param ndbNumber the ndbNumber to set
	 */
	public void setNdbNumber(String ndbNumber) {
		this.ndbNumber.set(ndbNumber);
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return productName.getValue();
	}

	/**
	 * @param productName the productName to set
	 */
	public void setProductName(String productName) {
		this.productName.set(productName);
	}

	/**
	 * @return the manufacturer
	 */
	public String getManufacturer() {
		return manufacturer.getValue();
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer.set(manufacturer);
	}

	/**
	 * @return the ingredients
	 */
	public String getIngredients() {
		return ingredients.getValue();
	}

	/**
	 * @param ingredients the ingredients to set
	 */
	public void setIngredients(String ingredients) {
		this.ingredients.set(ingredients);
	}

	/**
	 * @return the servingSize
	 */
	public float getServingSize() {
		return servingSize.getValue();
	}

	/**
	 * @param servingSize the servingSize to set
	 */
	public void setServingSize(float servingSize) {
		this.servingSize.set(servingSize);
	}

	/**
	 * @return the servingUom
	 */
	public String getServingUom() {
		return servingUom.getValue();
	}

	/**
	 * @param servingUom the servingUom to set
	 */
	public void setServingUom(String servingUom) {
		this.servingUom.set(servingUom);
	}

	/**
	 * @return the householdSize
	 */
	public float getHouseholdSize() {
		return householdSize.getValue();
	}

	/**
	 * @param householdSize the householdSize to set
	 */
	public void setHouseholdSize(float householdSize) {
		this.householdSize.set(householdSize);
	}

	/**
	 * @return the householdUom
	 */
	public String getHouseholdUom() {
		String fu = this.householdUom.getValue();
		return fu;
	}

	/**
	 * @param householdUom the householdUom to set
	 */
	public void setHouseholdUom(String householdUom) {
		this.householdUom.set(householdUom);
	}

	/**
	 * @return the productNutrients
	 */
	public ObservableMap<String, ProductNutrient> getProductNutrients() {
		return productNutrients;
	}

	/**
	 * @param productNutrients the productNutrients to set
	 */
	public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
		this.productNutrients = productNutrients;
	}

	class ProductNutrient {

		StringProperty nutrientCode = new SimpleStringProperty();
		FloatProperty nutrientQuantity = new SimpleFloatProperty();

		public ProductNutrient() {
			this.nutrientCode.setValue("");
		}

		public ProductNutrient(String nutrientCode, float nutrientQuantity) {
			this.nutrientCode.setValue(nutrientCode);
			this.nutrientQuantity.setValue(nutrientQuantity);
		}

		/**
		 * @return the nutrientCode
		 */
		public String getNutrientCode() {
			return nutrientCode.getValue();
		}

		/**
		 * @param nutrientCode the nutrientCode to set
		 */
		public void setNutrientCode(String nutrientCode) {
			this.nutrientCode.set(nutrientCode);
		}

		/**
		 * @return the nutrientQuantity
		 */
		public float getNutrientQuantity() {
			return nutrientQuantity.getValue();
		}

		/**
		 * @param nutrientQuantity the nutrientQuantity to set
		 */
		public void setNutrientQuantity(float nutrientQuantity) {
			this.nutrientQuantity.set(nutrientQuantity);
		}



	}


}
