package com.napier.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * District object is an object created by taking data from the world.sql database and processing it
 * Contains different variable to represent specific data in regards to different districts
 * Holds district name, district population, distrcit country
 * Makes use of getters and setters to get and set all of these values
 * Used in multiple method within dataHolder, for example citiesByPop, citiesInContinentByPop
 */
public class District {

    private String name;
    private long population = 0;
    private Country country;
    private HashMap<Integer, City> cities = new HashMap<>();

    public District(String name)
    {
        this.name = name;
    }

    public String getName(){ return name; }
    public long getPopulation(){ return population; }

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

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder("name=" + name + ", population=" + population +
                ", country=" + country.getName());

        if(cities != null){
            str.append(", districts=");
            str.append("(");
            for(Map.Entry<Integer, City> entry : cities.entrySet()){
                str.append(entry.getValue().getName());
                str.append(", ");
            }
            str.deleteCharAt(str.length() - 1);
            str.deleteCharAt(str.length() - 1);
            str.append(")");
        }

        return str.toString();
    }
}
