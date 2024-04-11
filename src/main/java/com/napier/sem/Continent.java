package com.napier.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a continent.
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
