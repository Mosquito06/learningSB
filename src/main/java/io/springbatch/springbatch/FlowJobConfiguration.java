package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
//@Configuration
public class FlowJobConfiguration
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob()
    {
        return jobBuilderFactory
                .get("helloJob")
                .start(Step1())
                .on("COMPLETED").to(Step3())
                .from(Step1())
                .on("FAILED").to(Step2())
                .end()
                .build();
    }

    @Bean
    public Step Step1()
    {
        return stepBuilderFactory
                .get("Step1")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" step1 has executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
    public Step Step2()
    {
        return stepBuilderFactory
                .get("Step2")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step2 was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step Step3()
    {
        return stepBuilderFactory
                .get("Step3")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step3 was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
