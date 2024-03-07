package com.napier.sem;

/**
 * Represents a city.
 */
public class City {

    private int id;
    private String name;
    private int population;
    private District district;
    private Country country;

    public City(int id, String name, int population)
    {
        this.id = id;
        this.name = name;
        this.population = population;
    }
    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getPopulation(){ return population; }

    public District getDistrict(){ return district; }
    public void setDistrict(District district){ this.district = district; }
    public Country getCountry(){ return country; }
    public void setCountry(Country country){ this.country = country; }

}
