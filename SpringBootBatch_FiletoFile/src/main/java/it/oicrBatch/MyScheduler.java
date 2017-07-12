package it.oicrBatch;

import javax.batch.operations.JobRestartException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class MyScheduler {
	
	@Autowired
	public JobLauncher launcher;
	
	@Autowired
	public Job job;
	/*Below you can find example pattern found on spring forum:

	* "0 0 * * * *" = the top of every hour of every day.
	* "*_/10 * * * * *" = every ten seconds.
	* "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
	* "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
	* "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
	* "0 0 0 25 12 ?" = every Christmas Day at midnight

	Cron expression is represented by six fields:
	second, minute, hour, day of month, month, day(s) of week
	* means any 
	* *_/X means every X 
	*/
	
	@Scheduled(cron="*/10 * * * * *")
	public void myScheduler() throws JobExecutionAlreadyRunningException, org.springframework.batch.core.repository.JobRestartException{
		
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		
		try {
			org.springframework.batch.core.JobExecution execution = launcher.run(job, jobParameters);
			
			System.out.println("############ status: "+ execution.getExitStatus());
		} catch (JobRestartException e) {
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
		} catch (JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
}