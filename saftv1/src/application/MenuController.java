package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class MenuController implements Initializable{
		ObservableList<Food> data = FXCollections.observableArrayList();
		ObservableList<Menu> menuList = FXCollections.observableArrayList();
		ObservableList<Menu> launch = FXCollections.observableArrayList();
		ObservableList<Menu> dinner = FXCollections.observableArrayList();
		XMLHandler handler = new XMLHandler();

		  @FXML
		    private ListView<Menu> menulist;

		    @FXML
		    private Button createmenuBtn;

		    @FXML
		    private Button removemenuBtn;

		    @FXML
			private TableColumn<Food, String> yemekcol;
			@FXML
			private TableColumn<Food,Double> fiyatcol;
		    
		    @FXML
		    private Label menupricelbl,totalpricelbl,foodnamelbl,foodpricelbl,foodcountlbl,datelbl,statuslbl;
		    
		    @FXML
		    private ListView<Food> menufoodlist;

		    @FXML
		    private TableView<Food> foodlist;

		    @FXML
		    private Button addbutton,removebutton,removefoodfrommenuBtn,addfoodtomenuBtn,kisiEkleBtn,haftalýkmenuBtn;

		    @FXML
		    private TextField foodtxt,pricetxt,datetxt,kisitxt;
		    
		    @FXML
		    private ComboBox<String> combobox;
		
	   

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//foodList click Event
    	pricetxt.addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation());
    	getList();
    	aylýkHesapla();      
    	setList();
    	checkMenus();
    	for(int i=0;i<menuList.size();i++){
    		System.out.println(menuList.get(i).getPrice());
    	}
   
	}
	
	 public void add(ActionEvent e) throws ParserConfigurationException, SAXException, IOException, TransformerException{
	    	if(!(pricetxt.getText().isEmpty() || foodtxt.getText().isEmpty())){
	    		Food toAdd = new Food(foodtxt.getText(),Double.parseDouble(pricetxt.getText()));
	    		if(doesListContain(toAdd.getName())){
	    			statuslbl.setText("Yemek zaten mevcut");
	    			return;
	    		}
	    		data.add(toAdd);
	    		foodlist.refresh();
	    		statuslbl.setText("Yemek baþarýyla eklendi!");
	    		handler.addToXML(toAdd,"lib/saft.xml");
	    	}else{
	    		statuslbl.setText("Lütfen tüm alanlarý doldurunuz");
	    	}
	    }
	    
	    public void remove(ActionEvent e) throws ParserConfigurationException, SAXException, IOException, TransformerException{
	    	if(foodlist.getSelectionModel().getSelectedItem()==null){
	    		statuslbl.setText("Lütfen çýkarýlacak liste elemanýný seçiniz.");
	    		return;
	    	}else{
	    		handler.removefromXML(foodlist.getSelectionModel().getSelectedItem(),"lib/saft.xml");
	    		data.remove(foodlist.getSelectionModel().getSelectedItem());
	    		foodlist.refresh();
	    		statuslbl.setText("Yemek baþarýyla çýkarýldý.");
	    		checkMenus();
	    	}
	    }
	    
		  public boolean doesListContain(String name){
			  for(Food x : data){
				  if(x.getName().equalsIgnoreCase(name))
					  return true;
			  }
			  return false;
		  }
		  
	
	public void getList(){
		try {
			handler.readXML(data,"lib/saft.xml");
			handler.readXML(menuList);
			for(Menu m:menuList){
				menuFiyatHesapla(m);
				if(m.getTime().equalsIgnoreCase("ogle"))
					launch.add(m);
				else
					dinner.add(m);
			}
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
		
		 combobox.getItems().add("Öðlen Menüleri");
	 	 combobox.getItems().add("Akþam Menüleri");
	}
	
	public void setList(){
		
	 	foodlist.setRowFactory( tv -> {
    	    TableRow<Food> row = new TableRow<Food>();
    	    row.setOnMouseClicked(event -> {
    	        if ((! row.isEmpty()) ) {
    	        	Food rowData = row.getItem();
    	        	pricetxt.setText(String.valueOf(rowData.getPrice()));
    	        	foodtxt.setText(rowData.getName());
    	        }
    	    });
    	    return row ;
    	});
	 	
		 
		 yemekcol.setCellValueFactory(new PropertyValueFactory<>("name"));
		 fiyatcol.setCellValueFactory(new PropertyValueFactory<>("price"));
		 foodlist.setItems(data);
	}
	
	
	 public EventHandler<KeyEvent> numeric_Validation() {
		    return new EventHandler<KeyEvent>() {
		        @Override
		        public void handle(KeyEvent e) {
		            TextField txt_TextField = (TextField) e.getSource();                
		            if(e.getCharacter().matches("[0-9.]")){ 
		                if(txt_TextField.getText().contains(".") && e.getCharacter().matches("[.]")){
		                    e.consume();
		                }else if(txt_TextField.getText().length() == 0 && e.getCharacter().matches("[.]")){
		                    e.consume(); 
		                }
		            }else{
		                e.consume();
		            }
		        }
		    };
		}
	 
	 public void showInfo(MouseEvent e){
		 Food food = menufoodlist.getSelectionModel().getSelectedItem();
		 if(food!=null){
			 foodnamelbl.setText(food.getName());
			 foodpricelbl.setText(String.valueOf(food.getPrice()+" TL"));
			
		 }
	 }
	 
	 public void showMenu(ActionEvent e){
		 if(combobox.getSelectionModel().getSelectedItem()!=null){
			 if(combobox.getSelectionModel().getSelectedItem().equals("Öðlen Menüleri")){
				 menulist.setItems(launch);
			 }else{
				 menulist.setItems(dinner);
			 }
		 }
	 }
	
	public void showFoods(MouseEvent e){
		if(menulist.getSelectionModel().getSelectedItem()!=null){
			menuFiyatHesapla(menulist.getSelectionModel().getSelectedItem());
			menufoodlist.setItems(menulist.getSelectionModel().getSelectedItem().getFoods());
			datelbl.setText(menulist.getSelectionModel().getSelectedItem().getDate());
			menupricelbl.setText(String.valueOf(menulist.getSelectionModel().getSelectedItem().getPrice() + " TL"));
			foodcountlbl.setText(String.valueOf(menulist.getSelectionModel().getSelectedItem().getFoods().size()));
			kisitxt.setText(String.valueOf(menulist.getSelectionModel().getSelectedItem().getKisiSayisi()));
		}else{
			statuslbl.setText("lütfen bir menu seçiniz.");
		}
	}
	
	public void addFoodToMenu(ActionEvent e){
		Food foodToAdd = foodlist.getSelectionModel().getSelectedItem();
		if(foodlist.getSelectionModel().getSelectedItem()!=null && menulist.getSelectionModel().getSelectedItem()!=null && doesItContain(menulist.getSelectionModel().getSelectedItem(),foodToAdd)==false){
			menulist.getSelectionModel().getSelectedItem().getFoods().add(foodToAdd);
			statuslbl.setText("Yemek menüye baþarýyla eklendi !");
			try {
				handler.addToXML(menulist.getSelectionModel().getSelectedItem(), foodToAdd);
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			menuFiyatHesapla(menulist.getSelectionModel().getSelectedItem());
			aylýkHesapla();
			menulist.refresh();
			menufoodlist.refresh();
			menupricelbl.setText(String.valueOf(menulist.getSelectionModel().getSelectedItem().getPrice() + " TL"));
			foodcountlbl.setText(String.valueOf(menulist.getSelectionModel().getSelectedItem().getFoods().size()));
		}else{
			if(doesItContain(menulist.getSelectionModel().getSelectedItem(),foodToAdd)){
				statuslbl.setText("Yemek zaten menüde var.");
			}else{
				statuslbl.setText("Lütfen eklenecek yemeði ve menüyü seçiniz.");
			}
		}
	}
	
	public boolean doesItContain(Menu menu ,Food food){
			for(int i=0;i<menu.getFoods().size();i++){
				if(food.getName().equalsIgnoreCase(menu.getFoods().get(i).getName())){
					return true;
				}
			}
			return false;
	}
	
	public boolean doesItContain(ObservableList<Food> foodlist,Food food){
		for(int i=0;i<foodlist.size();i++){
			if(foodlist.get(i).getName().equalsIgnoreCase(food.getName())){
				return true;
			}
		}
		return false;
	}
	
	public void checkMenus(){
		for(int i=0;i<menuList.size();i++){
			for(int j=0;j<menuList.get(i).getFoods().size();j++){
				if(doesItContain(data,menuList.get(i).getFoods().get(j))==false){
					Food foodToRemove = menuList.get(i).getFoods().get(j);
					menuList.get(i).getFoods().remove(foodToRemove);
					menufoodlist.refresh();
					aylýkHesapla();
					menuFiyatHesapla(menuList.get(i));
					try {
						handler.removefromXML(menuList.get(i),foodToRemove);
					} catch (ParserConfigurationException | SAXException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	public boolean doesItContain(Menu menu){
		System.out.println();
		for(int i=0;i<menuList.size();i++){
			if(menuList.get(i).getDate().equalsIgnoreCase(menu.getDate())&&menuList.get(i).getTime().equalsIgnoreCase(menu.getTime())){
				statuslbl.setText("menu zaten bulunuyor");
				return true;
			}
		}
		return false;
	}
	

	
	public void menuAdd(ActionEvent e){
		Menu menuToAdd = new Menu();
		if(!datetxt.getText().isEmpty() && !doesItContain(menuToAdd) && combobox.getSelectionModel().getSelectedItem()!=null){
			menuToAdd.setDate(datetxt.getText());
			menuToAdd.setPrice(0);
			menuList.add(menuToAdd);
			if(combobox.getSelectionModel().getSelectedItem().equals("Öðlen Menüleri")){
				menuToAdd.setTime("ogle");
				if(doesItContain(menuToAdd)==false){
				menuToAdd.setMenuName("Menu "+(launch.size()+1));
				launch.add(menuToAdd);
				}
			}else{
				menuToAdd.setTime("aksam");
				if(doesItContain(menuToAdd)==false){
				menuToAdd.setMenuName("Menu "+(dinner.size()+1));
				dinner.add(menuToAdd);
				}
			}
			menulist.refresh();
			if(doesItContain(menuToAdd)==false){
			try {
				handler.addToXML(menuToAdd);
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			statuslbl.setText("Menü baþarýyla eklendi!");
			}
			
		}else{
			if(doesItContain(menuToAdd)){
				statuslbl.setText("ayný tarihli menu zaten mevcut!");
			}
			statuslbl.setText("Tarih kýsmý boþ býrakýlmamalý !");
		}
			aylýkHesapla();
	}
	
	public void menuRemove(ActionEvent e){
		Menu menuToRemove = menulist.getSelectionModel().getSelectedItem();
		menuList.remove(menuToRemove);
		try {
			handler.removefromXML(menuToRemove);
		} catch (SAXException | IOException | ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String time = menuToRemove.getTime();
		if(time.equals("ogle")){
			launch.remove(menuToRemove);
		}else{
			dinner.remove(menuToRemove);
		}
		menulist.refresh();
		aylýkHesapla();
		menupricelbl.setText("");
		foodcountlbl.setText("");
		statuslbl.setText("Menü kaldýrýldý.");
	}
	
	public void menuFiyatHesapla(Menu menu){
		double fiyat=0;
		for(int i=0;i<menu.getFoods().size();i++){
			fiyat+=menu.getFoods().get(i).getPrice();
		}
		menu.setPrice(fiyat);
		aylýkHesapla();
	}
	
	public void addWeekMenu(ActionEvent e){
		if(combobox.getSelectionModel().getSelectedItem()==null){
			statuslbl.setText("Menü zamanýný seçiniz");
			return;
		}else{
			ObservableList<Menu> menulistToAdd = combobox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Öðlen Menüleri") ?launch : dinner ;
				int k=menulistToAdd.size()%7;
				if(menulistToAdd.size()==0){
					statuslbl.setText("lütfen ayýn ilk menüsü için tarih giriniz ve oluþturunuz.");
					return;
				}
				for(int i=0;i<7-k;i++){	
					Menu mlast = menulistToAdd.get(menulistToAdd.size()-1);
					String[] lastdate = mlast.getDate().split("/");
					int day = Integer.parseInt(lastdate[0]);
					day++;
					int month = Integer.parseInt(lastdate[1]);
					int year = Integer.parseInt(lastdate[2]);
					Menu newMenu = new Menu();
					newMenu.setDate(String.format("%02d",day)+"/"+String.format("%02d", month)+"/"+String.valueOf(year));
					newMenu.setTime(combobox.getSelectionModel().getSelectedItem().equalsIgnoreCase("Öðlen Menüleri") ? "ogle" : "aksam");
					newMenu.setMenuName("Menu "+(menulistToAdd.size()+1));
					menulistToAdd.add(newMenu);
					menulist.refresh();
					try {
						handler.addToXML(newMenu);
					} catch (ParserConfigurationException | SAXException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					aylýkHesapla();
					statuslbl.setText("Haftalýk menüler eklendi!");
				}
		}
		
	}
	
	public void aylýkHesapla(){
		double fiyat=0;
		for(int i=0;i<menuList.size();i++){
			fiyat += menuList.get(i).getPrice()*menuList.get(i).getKisiSayisi();
		}
		totalpricelbl.setText(String.valueOf(fiyat + " TL"));
	}
	
	public void removeFromMenu(ActionEvent e){
		Food foodToRemove = menufoodlist.getSelectionModel().getSelectedItem();
		Menu menuToRemoveFrom = menulist.getSelectionModel().getSelectedItem();
		if(foodToRemove!=null && menuToRemoveFrom!=null){
			menuToRemoveFrom.getFoods().remove(foodToRemove);
			try {
				handler.removefromXML(menuToRemoveFrom, foodToRemove);
			} catch (ParserConfigurationException | SAXException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			menuFiyatHesapla(menuToRemoveFrom);
			statuslbl.setText("Yemek çýkarýldý.");
		}
		
	}
	
	public void kisiEkle(ActionEvent e) throws ParserConfigurationException, SAXException, IOException{
		Menu menu = menulist.getSelectionModel().getSelectedItem();
		menu.setKisiSayisi(Integer.parseInt(kisitxt.getText()));
		statuslbl.setText("Kiþi sayýsý atandý");
		try {
			handler.editXML(menu);
		} catch (XPathExpressionException | TransformerFactoryConfigurationError | TransformerException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		menuFiyatHesapla(menu);
		aylýkHesapla();
	}
	
}
