package com.orsog.batch;

/**
 * Rappresenta un oggetto persona con i campi per il nome e il cognome.
 */
public class Person {
    private String firstName; // Nome della persona
    private String lastName; // Cognome della persona

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
