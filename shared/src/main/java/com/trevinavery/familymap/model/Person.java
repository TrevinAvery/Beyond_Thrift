package com.trevinavery.familymap.model;

/**
 * The Person class is a Java representation of a single row
 * in the Persons table. This is a convenience class to help
 * process the data in the database.
 */
public class Person implements Comparable<Person> {

    // Person ID: Unique identifier for this person (non-empty string)
    private String personID;

    // Descendant: User (Username) to which this person belongs
    private String descendant;

    // First Name: Person’s first name (non-empty string)
    private String firstName;

    // Last Name: Person’s last name (non-empty string)
    private String lastName;

    // Gender: Person’s gender (string: "f" or "m")
    private String gender;

    // Father: ID of person’s father (possibly null)
    private String father;

    // Mother: ID of person’s mother (possibly null)
    private String mother;

    // Spouse: ID of person’s spouse (possibly null)
    private String spouse;

    /**
     * Constructs a Person object with no data.
     */
    public Person() {
        // default constructor
    }

    /**
     * Constructs a Person object with pre-entered data.
     *
     * @param personID
     * @param descendant
     * @param firstName
     * @param lastName
     * @param gender
     * @param father
     * @param mother
     * @param spouse
     */
    public Person(String personID, String descendant, String firstName, String lastName,
                  String gender, String father, String mother, String spouse) {
        setPersonID(personID);
        setDescendant(descendant);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFather(father);
        setMother(mother);
        setSpouse(spouse);
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (personID != null ? !personID.equals(person.personID) : person.personID != null)
            return false;
        if (descendant != null ? !descendant.equals(person.descendant) : person.descendant != null)
            return false;
        if (firstName != null ? !firstName.equals(person.firstName) : person.firstName != null)
            return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null)
            return false;
        if (gender != null ? !gender.equals(person.gender) : person.gender != null) return false;
        if (father != null ? !father.equals(person.father) : person.father != null) return false;
        if (mother != null ? !mother.equals(person.mother) : person.mother != null) return false;
        return spouse != null ? spouse.equals(person.spouse) : person.spouse == null;

    }

    @Override
    public int hashCode() {
        int result = personID != null ? personID.hashCode() : 0;
        result = 31 * result + (descendant != null ? descendant.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (father != null ? father.hashCode() : 0);
        result = 31 * result + (mother != null ? mother.hashCode() : 0);
        result = 31 * result + (spouse != null ? spouse.hashCode() : 0);
        return result;
    }

    public String getName() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public int compareTo(Person person) {
        int nameCompare = getName().compareToIgnoreCase(person.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }

        return getPersonID().compareTo(person.getPersonID());
    }
}
