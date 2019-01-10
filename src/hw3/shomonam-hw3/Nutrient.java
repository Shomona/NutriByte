//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Nutrient {

	StringProperty nutrientCode = new SimpleStringProperty(); ;
	StringProperty nutrientName = new SimpleStringProperty(); ;
	StringProperty nutrientUom = new SimpleStringProperty(); ;

	public Nutrient() {
		nutrientCode.setValue("");
		nutrientName.setValue("");
		nutrientUom.setValue("");
	}

	public Nutrient(String nutrientCode, String nutrientName, String nutrientUom) {
		this.nutrientCode.set(nutrientCode);
		this.nutrientName.set(nutrientName);
		this.nutrientUom.set(nutrientUom);
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
	 * @return the nutrientName
	 */
	public String getNutrientName() {
		return nutrientName.getValue();
	}

	public StringProperty nutrientNameProperty() {
		return nutrientName;
	}
	/**
	 * @param nutrientName the nutrientName to set
	 */
	public void setNutrientName(String nutrientName) {
		this.nutrientName.set(nutrientName);
	}

	/**
	 * @return the nutrientUom
	 */
	public String getNutrientUom() {
		return nutrientUom.getValue();
	}

	public StringProperty nutrientUomProperty() {
		return nutrientUom;
	}

	/**
	 * @param nutrientUom the nutrientUom to set
	 */
	public void setNutrientUom(String nutrientUom) {
		this.nutrientUom.set(nutrientUom);
	}


}
