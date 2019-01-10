//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw2;

import java.io.File;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Controller {

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
			boolean recommendationShow = true;
			float paLevel = 0.0f;

			//Checking for valid data 
			if(gender == null ||gender.equals("")) recommendationShow = false;
			if(age.equals("")) age = "0";
			if(weight.equals("")) weight = "0";
			if(height.equals("")) age = "0";
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

			if(recommendationShow) {

				if(gender.equalsIgnoreCase("Male")) person = new Male(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), paLevel, ingredientsToAvoid);		
				else person = new Female(Float.parseFloat(age), Float.parseFloat(weight), Float.parseFloat(height), paLevel, ingredientsToAvoid);

				NutriProfiler.createNutriProfile(person);
				NutriByte.view.recommendedNutrientsTableView.setItems(NutriProfiler.recommendedNutrientsList);
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
			if(NutriByte.model.readProfiles(file.getAbsolutePath()));
			{
				NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
				NutriProfiler.createNutriProfile(NutriByte.person); 
				NutriByte.view.recommendedNutrientsTableView.setItems(NutriProfiler.recommendedNutrientsList);
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


			}


		}
	}

	class NewMenuItemHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			//write your code here

			NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
			NutriByte.view.initializePrompts();
			NutriProfiler.recommendedNutrientsList.clear();

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
}
