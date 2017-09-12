package application;

import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleStringProperty;


public class Food {
	private SimpleStringProperty name = new SimpleStringProperty("");
	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	
	
	public String getName() {
		return this.name.get();
	}
	public Food() {
		this("boþ alan",0);
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public double getPrice() {
		return this.price.get();
	}
	public void setPrice(double price1) {
		this.price.set(price1);
	}

	public Food(String food,double price) {
		
		setPrice(price);
		setName(food);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	 
	
	
}
