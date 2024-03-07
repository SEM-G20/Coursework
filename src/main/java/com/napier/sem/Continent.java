package com.napier.sem;

import java.util.HashMap;

/**
 * Represents a continent.
 */
public class Continent {
    private String name;
    private int population = 0;
    private HashMap<String, Region> regions = new HashMap<>();

    public Continent(String name)
    {
        this.name = name;
    }

    public String getName(){ return name; }
    public int getPopulation(){ return population; }

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
}
