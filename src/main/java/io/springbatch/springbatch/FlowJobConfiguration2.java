package io.springbatch.springbatch;

import lombok.RequiredArgsConstructor;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class FlowJobConfiguration2
{
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob()
    {
        return jobBuilderFactory
                .get("batchFlow")
                .start( flowA() )
                .next( Step3() )
                .next( flowB() )
                .next( Step6() )
                .end()
                .build();
    }

    @Bean
    public Flow flowA()
    {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowA");

        flowBuilder.start( Step1() )
                .next( Step2() )
                .end();

        return flowBuilder.build();

    };

    @Bean
    public Flow flowB()
    {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowA");

        flowBuilder.start( Step4() )
                .next( Step5() )
                .end();

        return flowBuilder.build();
    };

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

    public Step Step4()
    {
        return stepBuilderFactory
                .get("Step4")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step4 was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step Step5()
    {
        return stepBuilderFactory
                .get("Step5")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step5 was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    public Step Step6()
    {
        return stepBuilderFactory
                .get("Step6")
                .tasklet(new Tasklet()
                {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception
                    {
                        System.out.println(" >> step was executed ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }
}
