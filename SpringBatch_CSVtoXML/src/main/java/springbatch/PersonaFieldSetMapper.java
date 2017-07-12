package springbatch;

import org.joda.time.LocalDate;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

import model.Persona;

public class PersonaFieldSetMapper implements FieldSetMapper<Persona> {
	
	@Override
	public Persona mapFieldSet(FieldSet fieldset){
		Persona persona = new Persona();
		persona.setPname(fieldset.readString(0));
		persona.setDtnascita(new LocalDate(fieldset.readDate(1,"dd/MM/yyyy")));
		return persona;
	}
}