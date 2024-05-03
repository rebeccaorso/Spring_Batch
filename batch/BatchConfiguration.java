package com.orsog.batch;

import javax.sql.DataSource;

import com.orsog.batch.Person;
import com.orsog.batch.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfiguration {

    // Definisce il lettore di file per leggere i dati da un file CSV
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("sample-data.csv")) // Specifica la risorsa (file CSV)
                .delimited() // Configura il reader per leggere i dati da un file delimitato
                .names("firstName", "lastName") // Specifica i nomi delle colonne nel file CSV
                .targetType(Person.class) // Specifica il tipo di oggetto in cui verranno mappati i dati letti
                .build();
    }

    // Definisce il processor per elaborare i dati dei Person
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    // Definisce il writer per scrivere i dati nel database
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)") // Query SQL per l'inserimento dei dati
                .dataSource(dataSource) // Specifica il dataSource
                .beanMapped() // Configura il writer per mappare gli oggetti Person alle colonne della tabella
                .build();
    }

    // Definisce il job di importazione degli utenti
    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener) // Aggiunge un listener per notificare il completamento del job
                .start(step1) // Specifica il primo passo del job
                .build();
    }

    // Definisce il passo 1 del job
    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("step1", jobRepository)
                .<Person, Person>chunk(3, transactionManager) // Configura il passo per processare i dati a chunk di 3 elementi
                .reader(reader) // Specifica il lettore di file
                .processor(processor) // Specifica il processor per elaborare i dati
                .writer(writer) // Specifica il writer per scrivere i dati nel database
                .build();
    }
}
