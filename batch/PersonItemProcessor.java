package com.orsog.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Classe che implementa l'interfaccia ItemProcessor per processare oggetti di tipo Person durante il lavoro batch.
 */
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    /**
     * Metodo che processa un oggetto Person, trasformando il nome e il cognome in maiuscolo.
     * @param person L'oggetto Person da processare.
     * @return L'oggetto Person con nome e cognome in maiuscolo.
     * @throws Exception Eccezione in caso di errori durante il processo.
     */
    @Override
    public Person process(final Person person) throws Exception {
        final String firstName = person.getFirstName().toUpperCase(); // Converti il nome in maiuscolo
        final String lastName = person.getLastName().toUpperCase(); // Converti il cognome in maiuscolo

        final Person transformedPerson = new Person(firstName, lastName); // Crea un nuovo oggetto Person trasformato

        log.info("Converting (" + person + ") into (" + transformedPerson + ")"); // Logga la trasformazione

        return transformedPerson; // Restituisce l'oggetto Person trasformato
    }
}
