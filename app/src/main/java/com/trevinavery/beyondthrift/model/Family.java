package com.trevinavery.beyondthrift.model;

public class Family {
    private Person spouse;
    private Person father;
    private Person mother;
    private Person child;

    public Family(Person spouse, Person father, Person mother, Person child) {
        this.spouse = spouse;
        this.father = father;
        this.mother = mother;
        this.child = child;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    public Person getFather() {
        return father;
    }

    public void setFather(Person father) {
        this.father = father;
    }

    public Person getMother() {
        return mother;
    }

    public void setMother(Person mother) {
        this.mother = mother;
    }

    public Person getChild() {
        return child;
    }

    public void setChild(Person child) {
        this.child = child;
    }
}