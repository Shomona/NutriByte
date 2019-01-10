//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class RecommendedNutrient {

	StringProperty nutrientCode = new SimpleStringProperty(); ;
	FloatProperty nutrientQuantity = new SimpleFloatProperty();

	public RecommendedNutrient() {
		this.nutrientCode.setValue("");
	}

	public RecommendedNutrient(String nutrientCode, float nutrientQuantity) {
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

	public FloatProperty nutrientQuantityProperty() {
		return nutrientQuantity;
	}

	/**
	 * @param nutrientQuantity the nutrientQuantity to set
	 */
	public void setNutrientQuantity(float nutrientQuantity) {
		this.nutrientQuantity.set(nutrientQuantity);
	}

}
