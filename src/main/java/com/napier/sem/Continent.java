package com.napier.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * Continent object is an object created by taking data from the world.sql database and processing it
 * Contains different variable to represent specific data in regards to different continents
 * Holds name and population
 * Makes use of getters and setters to get and set these values
 * Used in multiple method within dataHolder, for example countryiesByPop, countriesByPopInContinent
 */
public class Continent {
    private String name;
    private long population = 0;
    private HashMap<String, Region> regions = new HashMap<>();

    public Continent(String name)
    {
        this.name = name;
    }

    public String getName(){ return name; }
    public long getPopulation(){ return population; }

    /**
     * Calculate population for whole continent.
     */
    public void setPopulation(){
        if(!regions.isEmpty()){
            // Sum the populations for all regions contained within continent
            regions.forEach((name, region) -> {
                if(region.getPopulation() == 0){
                    System.out.printf("Region %s population is empty.\n", name);
                }
                else{
                    this.population += region.getPopulation();
                }
            });
        }
    }
    public HashMap<String, Region> getRegions(){ return regions; }
    public void addRegion(String name, Region region){ regions.put(name, region); }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder("name=" + name + ", population=" + population);
        if(regions != null){
            str.append(", regions=");
            str.append("(");
            for(Map.Entry<String, Region> entry : regions.entrySet()){
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
