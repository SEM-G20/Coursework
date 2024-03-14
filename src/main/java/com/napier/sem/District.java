package com.napier.sem;

import java.util.HashMap;

/**
 * Represents a district.
 */
public class District {

    private String name;
    private int population = 0;
    private Country country;
    private HashMap<Integer, City> cities = new HashMap<>();

    public District(String name)
    {
        this.name = name;
    }

    public String getName(){ return name; }
    public int getPopulation(){ return population; }

    /**
     * Calculate population for whole district.
     */
    public void setPopulation(){
        if(!cities.isEmpty()){
            // Sum the populations for all cities contained within district
            cities.forEach((id, city) -> {
                this.population += city.getPopulation();
            });
        }
    }
    public Country getCountry(){ return country; }
    public void setCountry(Country country) { this.country = country; }
    public HashMap<Integer, City> getCities(){ return cities; }
    public void addCity(int cityID, City city){ cities.put(cityID, city); }
}
