//Name: Shomona Mukherjee
//Andrew-id: shomonam

package hw2;

import hw2.NutriProfiler.AgeGroupEnum;

public abstract class Person {

	float age, weight, height, physicalActivityLevel; //age in years, weight in kg, height in cm
	String ingredientsToWatch;
	float[][] nutriConstantsTable = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT][NutriProfiler.AGE_GROUP_COUNT];

	NutriProfiler.AgeGroupEnum ageGroup;

	abstract void initializeNutriConstantsTable();
	abstract float calculateEnergyRequirement();

	//remove this default constructor once you have defined the child's constructor


	Person(float age, float weight, float height, float physicalActivityLevel, String ingredientsToWatch) {
		//write your code here

		this.age = age;
		this.weight = weight;
		this.height = height;
		this.physicalActivityLevel = physicalActivityLevel;
		this.ingredientsToWatch = ingredientsToWatch;

		for( AgeGroupEnum ageEnum : AgeGroupEnum.values()) {

			if(ageEnum.getAge() > age) {
				ageGroup = ageEnum;
				break;
			}
		}

	}

	//returns an array of nutrient values of size NutriProfiler.RECOMMENDED_NUTRI_COUNT. 
	//Each value is calculated as follows:
	//For Protein, it multiples the constant with the person's weight.
	//For Carb and Fiber, it simply takes the constant from the 
	//nutriConstantsTable based on NutriEnums' nutriIndex and the person's ageGroup
	//For others, it multiples the constant with the person's weight and divides by 1000.
	//Try not to use any literals or hard-coded values for age group, nutrient name, array-index, etc. 

	float[] calculateNutriRequirement() {
		//write your code here
		int ageIndex = ageGroup.getAgeGroupIndex();
		float[] nutrireq = new float[NutriProfiler.RECOMMENDED_NUTRI_COUNT];


		for (NutriProfiler.NutriEnum nutriEnum : NutriProfiler.NutriEnum.values()) {

			if (nutriEnum.equals(NutriProfiler.NutriEnum.PROTEIN)) {

				nutrireq[nutriEnum.getNutriIndex()] = weight * nutriConstantsTable[nutriEnum.getNutriIndex()][ageIndex];
			}
			else if (nutriEnum.equals(NutriProfiler.NutriEnum.CARBOHYDRATE) ||nutriEnum.equals(NutriProfiler.NutriEnum.FIBER)) {

				nutrireq[nutriEnum.getNutriIndex()] = nutriConstantsTable[nutriEnum.getNutriIndex()][ageIndex];
			}
			else {

				nutrireq[nutriEnum.getNutriIndex()] = weight * nutriConstantsTable[nutriEnum.getNutriIndex()][ageIndex]/ 1000;
			}

		}


		return nutrireq;
	}
}
