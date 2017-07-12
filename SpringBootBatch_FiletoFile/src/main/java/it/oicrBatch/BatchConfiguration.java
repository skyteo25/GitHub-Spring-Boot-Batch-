package it.oicrBatch;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import it.oicrBatch.items.CustomItemProcessor;
import it.oicrBatch.model.Persona;
import it.oicrBatch.model.UserDate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	@Autowired
	public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	public DataSource dataSource;
	
	@Bean
	public JdbcCursorItemReader<Persona> reader(){
		
		final DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUsername("sa");
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // ex. com.mysql.jdbc.Driver
		ds.setUrl("jdbc:sqlserver://localhost:1433;databaseName=TestSpring;integratedSecurity=true"); // ex. jdbc:mysql://localhost/springbatch
		ds.setPassword("S2ESprint");
		
		JdbcCursorItemReader<Persona> reader = new JdbcCursorItemReader<Persona>();
		reader.setDataSource(ds);
		reader.setSql("select id , lastName , firstName, age from Persona");
	    reader.setRowMapper(new PersonaRowMapper());
		return reader;
	}
	
	public class PersonaRowMapper implements RowMapper<Persona>{
		
		@Override
		public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
			Persona persona = new Persona();
			persona.setLastName(rs.getString("lastName"));
			persona.setFirstName(rs.getString("firstName"));
			persona.setAge(rs.getInt("age"));
			System.out.println(persona);
			return persona;
		}
	}
	
	/*@SuppressWarnings("rawtypes")
	@Bean
	public StaxEventItemReader<Persona> reader(){
		StaxEventItemReader<Persona> reader = new StaxEventItemReader<Persona>();
		Resource resource = resourceLoader.getResource("Data.xml");
		reader.setResource(resource);
		reader.setFragmentRootElementName("persona");
		
		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(new HashMap<String, Class>(){
			private static final long serialVersionUID = 1L;

		{
			put("persona", Persona.class);
		}});
		
		reader.setUnmarshaller(xStreamMarshaller);
		
		return reader;
		
	}*/
	
	@Bean
	public ItemProcessor<Persona,UserDate> processor(){
		return new CustomItemProcessor();
	}
	
	/*@SuppressWarnings("rawtypes")
	@Bean
	public StaxEventItemWriter<Persona> writer(){
		
		
		StaxEventItemWriter<Persona> writer = new StaxEventItemWriter<Persona>();
		Resource resource = resourceLoader.getResource("persona.xml");
		writer.setResource(resource);
		
		XStreamMarshaller marshaller = new XStreamMarshaller();
		marshaller.setAliases(new HashMap<String, Class>(){
			private static final long serialVersionUID = 1L;

		{
			put("persona", Persona.class);
		}});
		
		writer.setMarshaller(marshaller);
		writer.setRootTagName("persone");
		writer.setOverwriteOutput(true);
		
		return writer;
	}*/
	
	@Bean
	public JdbcBatchItemWriter<UserDate> writer(){
		JdbcBatchItemWriter<UserDate> writer = new JdbcBatchItemWriter<UserDate>();
		
		final DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUsername("sa");
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // ex. com.mysql.jdbc.Driver
		ds.setUrl("jdbc:sqlserver://localhost:1433;databaseName=TestSpring;integratedSecurity=true"); // ex. jdbc:mysql://localhost/springbatch
		ds.setPassword("S2ESprint");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<UserDate>());
		writer.setSql("insert into UserDate (lastName,firstName,age,dataNascita) values (:lastName,:firstName,:age,:dataNascita)");
		writer.setDataSource(ds);
		return writer;
	}
	
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1")
				.<Persona,UserDate> chunk(10)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();
	}
	
	@Bean
	public Job importUserJob(){
		return jobBuilderFactory.get("importUserJob")
				.incrementer(new RunIdIncrementer())
				.flow(step1())
				.end()
				.build();
				
	}
}