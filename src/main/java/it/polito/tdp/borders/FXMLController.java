/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController
{
	private Model model;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;
	
    @FXML
    private Button btnSimula;
    
    @FXML
    private Button btnGrafo;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="txtAnno"
	private TextField txtAnno; // Value injected by FXMLLoader

	@FXML // fx:id="boxNazione"
	private ComboBox<Country> boxNazione; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader
	
//	private final String ERRORE = "\n\nERRORE! controllare che i dati inseriti siano corretti";
	
	@FXML void doCalcolaConfini(ActionEvent event)
	{ 
		Integer year;
		try
		{
			year = Integer.parseInt(this.txtAnno.getText());
			if(year < 1816 || year > 2006)
				throw new NumberFormatException();
		}
		catch(NumberFormatException nfe)
		{
			this.txtResult.appendText("\n\nErrore, inserire un numero corretto, tra 1816 e 2006");
			return;
		}  
		
		//resetto testo
		this.txtResult.clear();
    	this.txtResult.appendText("Crea grafo...\n");

    	//creo grafo
    	this.model.creaGrafo(year);
    	txtResult.appendText(String.format("\nGRAFO CREATO CON:\n#Vertici: %d\n#Archi: %d",
				this.model.getNumVertici(),
				this.model.getNumArchi()));
    	
    	
    	txtResult.appendText("\n" + this.model.stampaGrafo());

		//cliccabili
		this.btnGrafo.setDisable(false);
		this.btnSimula.setDisable(false);
		this.boxNazione.setDisable(false);

		//aggiungo risultati alla combobox 
		this.boxNazione.getItems().clear();
		this.boxNazione.getItems().addAll(this.model.getVertici()); 
	}

	@FXML void doSimula(ActionEvent event)
	{
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize()
	{
		assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
		assert boxNazione != null : "fx:id=\"boxNazione\" was not injected: check your FXML file 'Scene.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
	}

	public void setModel(Model model)
	{
		this.model = model;
	}
}
