class Animal {
	String type; //this is the type of animal
	int number_of_legs; //its number of legs
	boolean carnivore; //if it eats meat, true/false
	boolean herbivore; //if it only eats veggies, true/false
	boolean omnivore; //it it eats both, true/false
	String animal_noise; //what sound does it make (String)

	//set up constructor method to create new objects
	Animal (String species, int legs, boolean carn, boolean herb, boolean omni, String sound) {
		//the next lines set values for the object's attributes defined on lines 2 - 7
		type = species;
		number_of_legs = legs;
		carnivore = carn;
		herbivore = herb;
		omnivore = omni;
		animal_noise = sound;
	}

	public void eats_meat() {
		if ((carnivore)||(omnivore)) { //if carnivore or omnivore, will take the steak
			System.out.println("The " + type + " accepts your offer of steak.");
		}
		else {
			System.out.println("The " + type + " doesn't eat meat. Try offering it kale.");		
		}
	}

	public void roar() {
		System.out.println(animal_noise + " says the " + type + ".");
	}

}

class AnimalLauncher {
	public static void main(String args[]) {
		String[] animals = {"pig","duck","cow","dog"}; //array of animals
		int legs[] = {4,2,4,4}; //they're mostly all quadrupeds
		boolean[] carn = {false,false,false,true}; //assign meat eating status, etc
		boolean[] herb = {false,false,true,false};
		boolean[] omni = {true,true,false,false};
		String[] noises = {"oink","quack","moo","ruff-ruff"}; //assign noises

		Animal[] menagerie = new Animal[4]; //put my animals in an array

		for (int i = 0; i<animals.length; i++) { 
			//the .length method is an automatic way to set the upper bound of the loop based on array size
			Animal animal = new Animal(animals[i],legs[i],carn[i],herb[i],omni[i],noises[i]); //new animal
			menagerie[i] = animal; //add my animal to the menagerie
			menagerie[i].roar(); //allow my animal to practice making noises
			menagerie[i].eats_meat(); //offer my animal a steak
		}
	
	}
}
