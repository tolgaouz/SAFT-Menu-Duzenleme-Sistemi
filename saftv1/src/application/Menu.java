package application;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Menu {
	
	private SimpleStringProperty date = new SimpleStringProperty();
	private SimpleDoubleProperty price = new SimpleDoubleProperty();
	private ObservableList<Food> foods;
	private SimpleStringProperty menuName = new SimpleStringProperty();
	private SimpleStringProperty time = new SimpleStringProperty();
	private SimpleIntegerProperty kisiSayisi = new SimpleIntegerProperty();
	
	public String getTime() {
		return this.time.get();
	}
	
	public int getKisiSayisi() {
		return kisiSayisi.get();
	}

	public void setKisiSayisi(int kisiSayisi) {
		this.kisiSayisi.set(kisiSayisi);
	}

	public void setTime(String time) {
		this.time.set(time);
	}
	public String getMenuName() {
		return menuName.get();
	}
	public void setMenuName(String menuName) {
		this.menuName.set(menuName);
	}
	
	public Menu() {
		foods = FXCollections.observableArrayList();
	}
	public String getDate() {
		return date.get();
	}
	
	public void setDate(String date) {
		this.date.set(date);
	}
	public double getPrice() {
		return  price.get();
	}
	public void setPrice(double price) {
		this.price.set(price);
	}
	public ObservableList<Food> getFoods() {
		return foods;
	}
	public void setFoods(ObservableList<Food> foods) {
		this.foods = foods;
	}
	
	public Menu(String date, double price, ObservableList<Food> Foods) {
		this.date.set(date);
		this.price.set(price);
		this.foods = FXCollections.observableArrayList();
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getMenuName();
	}
	
}
