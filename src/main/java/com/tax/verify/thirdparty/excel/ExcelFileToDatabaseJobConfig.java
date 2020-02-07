package com.tax.verify.thirdparty.excel;
/*
import com.tax.verify.dto.Data;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ExcelFileToDatabaseJobConfig {
    @Bean
    public FlatFileItemReader<Data> reader(String filepath) {
        return new FlatFileItemReaderBuilder<Data>()
                .name("personItemReader")
                .resource(new ClassPathResource(filepath))
                .delimited()
                .names(new String[]{"vd_sorulan", "tc_sorulan", "plaka"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Data>() {{
                    setTargetType(Data.class);
                }})
                .build();
    }
}
*/