package com.example.dto;

public class PowerCoupleDTO {
    private String person;
    private String organization;
    private int strength;

    public PowerCoupleDTO() {
    }

    public PowerCoupleDTO(String person, String organization, int strength) {
        this.person = person;
        this.organization = organization;
        this.strength = strength;
    }

    public String getPerson() {
        return this.person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "{" +
            " person='" + getPerson() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", strength='" + getStrength() + "'" +
            "}";
    }

}
