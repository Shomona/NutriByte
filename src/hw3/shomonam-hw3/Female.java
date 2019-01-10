//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw3;

import hw3.NutriProfiler.AgeGroupEnum;

public class Female extends Person {

	float[][] nutriConstantsTableFemale = new float[][]{
		//AgeGroups: 3M, 6M, 1Y, 3Y, 8Y, 13Y, 18Y, 30Y, 50Y, ABOVE 
		{1.52f, 1.52f, 1.2f, 1.05f, 0.95f, 0.95f, 0.71f, 0.8f, 0.8f, 0.8f}, //0: Protein constants
		{60, 60, 95, 130, 130, 130, 130, 130, 130, 130}, //1: Carbohydrate
		{19, 19, 19, 19, 25, 26, 26, 25, 25, 21},  //2: Fiber constants
		{36, 36, 32, 21, 16, 15, 14, 14, 14, 14}, 	//3: Histidine
		{88, 88, 43, 28, 22, 21, 19, 19, 19, 19}, 	//4: isoleucine
		{156, 156, 93, 63, 49, 47, 44 , 42, 42, 42},//5: leucine
		{107, 107, 89, 58, 46, 43, 40, 38, 38, 38}, //6: lysine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//7: methionine
		{59, 59, 43, 28, 22, 21, 19, 19, 19, 19}, 	//8: cysteine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //9: phenylalanine
		{135, 135, 84, 54, 41, 38, 35, 33, 33, 33}, //10: phenylalanine
		{73, 73, 49, 32, 24, 22, 21, 20, 20, 20}, 	//11: threonine
		{28, 28, 13, 8, 6, 6, 5, 5, 5, 5}, 			//12: tryptophan
		{87, 87, 58, 37, 28, 27, 24, 24, 24, 24	}  	//13: valine
	};

	Female(float age, float weight, float height, float physicalActivityLevel, String ingredientsToAvoid) {
		//write your code here
		super(age, weight, height, physicalActivityLevel, ingredientsToAvoid);
		initializeNutriConstantsTable();
	}

	@Override
	float calculateEnergyRequirement() {
		//write your code here

		int ageIndex = 0;
		float energy = 0;
		float X = 0, A=0, B=0, C=0, D=0;

		for( AgeGroupEnum ageEnum : AgeGroupEnum.values()) {

			if(ageEnum.getAge() > age) {
				ageIndex = ageEnum.getAgeGroupIndex();
				break;
			}
		}

		//Constants associated with Energy
		switch(ageIndex) {
		case 0:
			X = -75;
			break;
		case 1:
			X = 44;
			break;
		case 2:
			X = 78;
			break;
		case 3:
			X = 80;
			break;
		case 4:
		case 5:
		case 6:
			A = 135.3f;
			B = 30.8f;
			C = 10f;
			D = 934;
			break;
		default:
			A = 354;
			B = 6.91f;
			C = 9.36f;
			D = 726;
		}

		if(ageIndex>=0 && ageIndex<4) {
			energy = 89 * weight - X;
		}
		else if (ageIndex>=4 && ageIndex<7) {
			energy = A - (B*age) + physicalActivityLevel * ( C * weight + D * height/100) + 20;
		}
		else {
			energy = A - (B*age) + physicalActivityLevel * ( C * weight + D * height/100);
		}


		return energy;

	}

	@Override
	void initializeNutriConstantsTable() {
		//write your code here
		for(int i=0; i<nutriConstantsTableFemale.length; i++)
			for(int j=0; j<nutriConstantsTableFemale[i].length; j++)
				super.nutriConstantsTable[i][j]=nutriConstantsTableFemale[i][j];
	}
}
