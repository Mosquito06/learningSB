package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class JobExcutionDeciderConfiguration
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob()
    {
        return jobBuilderFactory.get("deciderJob")
                .start( Step() )
                .next( decider() )
                .from( decider() ).on("ODD").to( OddStep() )
                .from( decider() ).on("EVEN").to( EvenStep())
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider()
    {
        return new CustomDecider();
    }

    @Bean
    public Step Step()
    {
        return stepBuilderFactory
                .get("Step1")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println("this is the start tasklet");

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
    public Step EvenStep()
    {
        return stepBuilderFactory
                .get("EvenStep")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> EvenStep was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step OddStep()
    {
        return stepBuilderFactory
                .get("OddStep")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> OddStep was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

}
