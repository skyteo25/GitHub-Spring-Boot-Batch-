package springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		ApplicationContext cxt = new ClassPathXmlApplicationContext("spring-batch-context.xml");
		JobLauncher jobLauncher = (JobLauncher)cxt.getBean("jobLauncher");
		Job job = (Job)cxt.getBean("PersonaJob");
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Exit Status : "+ execution.getStatus());
 
        } catch (JobExecutionException e) {
            System.out.println("Job Persona failed");
            e.printStackTrace();
        }
	}
}