package com.napier.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * Region object is an object created by taking data from the world.sql database and processing it
 * Contains different variable to represent specific data in regards to the region
 * Holds name, population continent values
 * Makes use of getters and setters to get name, population and continent values
 * Includes toString method to print region
 * Used in multiple method within dataHolder, for example countryiesByPop, countriesByPopInContinent
 */
public class Region {
    private String name;
    private long population = 0;
    private Continent continent;
    private HashMap<String, Country> countries = new HashMap<>();

    public Region(String name)
    {
        this.name = name;
    }

    public String getName(){ return name; }
    public long getPopulation(){ return population; }

    /**
     * Calculate population for whole region.
     */
    public void setPopulation(){
        if(!countries.isEmpty()){
            // Sum the populations for all countries contained within region
            countries.forEach((name, country) -> {
                this.population += country.getPopulation();
            });
        }
    }
    public Continent getContinent(){ return continent; }
    public void setContinent(Continent continent){ this.continent = continent; }
    public HashMap<String, Country> getCountries(){ return countries; }
    public void addCountry(String name, Country country){
        countries.put(name, country);
    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder("name=" + name + ", population=" + population +
                ", continent=" + continent.getName());

        if(countries != null){
            str.append(", districts=");
            str.append("(");
            for(Map.Entry<String, Country> entry : countries.entrySet()){
                str.append(entry.getKey());
                str.append(", ");
            }
            str.deleteCharAt(str.length() - 1);
            str.deleteCharAt(str.length() - 1);
            str.append(")");
        }

        return str.toString();
    }
}
