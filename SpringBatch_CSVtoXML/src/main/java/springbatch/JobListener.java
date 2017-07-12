package springbatch;

import java.util.List;
import org.joda.time.DateTime;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecutionListener;

public class JobListener implements JobExecutionListener {
	
	private DateTime startTime, stopTime;

	@Override
	public void beforeJob(JobExecution arg0) {
		startTime = new DateTime();
		System.out.println("Persona job iniziato alle: " + startTime);
		
	}

	@Override
	public void afterJob(JobExecution arg0) {
		stopTime = new DateTime();
		System.out.println("Persona job finito alle: " + stopTime);
		
		if(arg0.getStatus() == BatchStatus.COMPLETED)
			System.out.println("Persona job eseguito correttamente");
		else if (arg0.getStatus() == BatchStatus.FAILED){
			System.out.println("Persona job fallito con le seguenti eccezzioni: ");
			List<Throwable> listException = arg0.getAllFailureExceptions();
			for (Throwable th : listException)
				System.out.println("Eccezzioni: " + th.getLocalizedMessage());
		}
	}
	

}
