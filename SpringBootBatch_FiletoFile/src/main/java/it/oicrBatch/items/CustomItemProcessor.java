package it.oicrBatch.items;


import java.sql.Timestamp;
import java.util.Date;

import it.oicrBatch.model.Persona;
import it.oicrBatch.model.UserDate;

public class CustomItemProcessor implements org.springframework.batch.item.ItemProcessor<Persona, UserDate> {

	@Override
	public UserDate process(Persona persona) throws Exception {
		
		UserDate userDate = new UserDate();
		Timestamp timeStamp = new Timestamp(new Date().getTime());
		@SuppressWarnings("deprecation")
		Date dataNascita = new Date("1990/01/01");
		userDate.setLastName(persona.getLastName());
		userDate.setFirstName(persona.getFirstName());
		userDate.setAge(persona.getAge());
		userDate.setDataNascita(dataNascita);
		return userDate;
	}	
}