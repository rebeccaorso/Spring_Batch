package com.orsog.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

// Questa classe implementa l'interfaccia JobExecutionListener per gestire eventi relativi all'esecuzione di un job di Spring Batch
public class JobCompletionNotificationListener implements JobExecutionListener {

    // Logger per la registrazione di messaggi
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    // JdbcTemplate per eseguire le query sul database
    private final JdbcTemplate jdbcTemplate;

    // Costruttore che accetta un JdbcTemplate
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Metodo chiamato dopo il completamento del job
    @Override
    public void afterJob(JobExecution jobExecution) {
        // Verifica se lo stato del job è COMPLETED
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            // Logga un messaggio informativo
            log.info("!!! JOB FINISHED! Time to verify the results");

            // Esegue una query sul database per selezionare i dati inseriti
            jdbcTemplate
                    .query("SELECT first_name, last_name FROM people", new DataClassRowMapper<>(Person.class))
                    .forEach(person -> log.info("Found <{{}}> in the database.", person));
        }
    }
}
