package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.LocalDate;

@XmlRootElement(name = "Data")
public class Persona {
	
	private String pname;
	private LocalDate dtnascita;
	
	//Annotation per mappare i tag nel file xml
	@XmlElement(name = "pname")
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	
	@XmlElement(name = "dtnascita")
	//Annotation per adattare la data 
	@XmlJavaTypeAdapter(type = LocalDate.class, value = adapter.LocalDateAdapter.class)
	public LocalDate getDtnascita() {
		return dtnascita;
	}
	public void setDtnascita(LocalDate dtnascita) {
		this.dtnascita = dtnascita;
	}
	
	@Override
	public String toString(){
		return "Persona: " + pname + "\n Data di nascita: " + dtnascita;
	}
}