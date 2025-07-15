package com.springbatch.spring_batch_bootcamp_001.controller;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class JobLaunchingController {

    //job launcher can be used to launch a job in its simplistic form,
    //However, to launch more complex jobs we need, JobOperator
    //Job operator can handle things like restarts and retries when launching a JOB

//    @Autowired
//    private JobLauncher jobLauncher;


    @Autowired
    private JobOperator jobOperator;

//    @Autowired
//    private Job job;

    @RequestMapping(value = "/test/launch", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void launch(@RequestParam("name") String name) throws Exception{

        JobParameters jobParameters =
                new JobParametersBuilder()
                .addString("name", name)
                .toJobParameters();

//        this.jobLauncher.run(job,jobParameters);

        //the JobOperator is able to look the job by its name when it comes to run
        jobOperator.start("uniqueJobName", String.format("name=%s", name));

    }

}
