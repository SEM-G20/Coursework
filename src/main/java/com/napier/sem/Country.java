package com.napier.sem;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a country.
 */
public class Country {
    private String code;
    private String name;
    private long population;
    private Continent continent;
    private City city;
    private Region region;
    private HashMap<String, District> districts = new HashMap<>();
    private City capital;
    private HashMap<String, Float> officialLanguages = new HashMap<>();
    private String mainLanguage;

    private long popInCities;
    private String popInCitiesPer;
    private long popOutCities;
    private String popOutCitiesPer;

    public long getPopInCities(){return popInCities;}
    public void setPopInCities(long popInCities){ this.popInCities = popInCities; }
    public String getPopInCitiesPer(){return popInCitiesPer;}
    public void setPopInCitiesPer(String PopInCitiesPer){ this.popInCitiesPer = PopInCitiesPer; }
    public long getPopOutCities(){return popOutCities;}
    public void setPopOutCities(long PopOutCities){ this.popOutCities = PopOutCities; }
    public String getPopOutCitiesPer(){return popOutCitiesPer;}
    public void setPopOutCitiesPer(String PopOutCitiesPer){ this.popOutCitiesPer = PopOutCitiesPer; }


    public Country(){}

    public Country(String name)
    {
        this.name = name;
    }

    public Country(String name, int population)
    {
        this.name = name;
        this.population = population;
    }


    public String getCode(){return code;}
    public void setCode(String code){ this.code = code; }
    public String getName(){ return name; }
    public void setName(String name){ this.name = name;}
    public long getPopulation(){ return population; }
    public void setPopulation(long population) { this.population=population; }
    public Continent getContinent(){ return continent; }
    public void setContinent(Continent continent) { this.continent = continent; }
    public City getCity(){return city;}
    public void setCity(City city) {this.city = city;}

    public Region getRegion(){ return region; }
    public void setRegion(Region region){ this.region = region; }
    public HashMap<String, District> getDistricts(){ return districts; }
    public void addDistrict(String name, District district){
        districts.put(name, district);
    }
    public void setCapital(City capital){ this.capital = capital; }
    public City getCapital(){ return capital; }
    public HashMap<String, Float> getOfficialLanguages(){ return officialLanguages; }
    public void addLanguage(String language, float percentage){ officialLanguages.put(language, percentage); }
    public String getMainLanguage(){ return mainLanguage; }

    /**
     * Compare percentage of use of each official language, set the biggest as main language in the country.
     */
    public void setMainLanguage(){

        if(officialLanguages != null){

            String tempLanguage = "";
            float tempPercentage = 0.0f;

            // Get the biggest percentage of use
            for(HashMap.Entry<String, Float> entry : officialLanguages.entrySet()){
                if(entry.getValue() > tempPercentage){
                    tempLanguage = entry.getKey();
                    tempPercentage = entry.getValue();
                }
            }
            mainLanguage = tempLanguage;
        }
    }

    @Override
    public String toString(){

        StringBuilder str = new StringBuilder("code=" + code + "name=" + name + ", population=" + population +
                ", continent=" + continent.getName() + ", region=" + region.getName());

        if(districts != null){
            str.append(", districts=");
            str.append("(");
            for(Map.Entry<String, District> entry : districts.entrySet()){
                str.append(entry.getKey());
                str.append(", ");
            }
            str.deleteCharAt(str.length() - 1);
            str.deleteCharAt(str.length() - 1);
            str.append(")");
        }

        str.append(", capital=").append(capital.getName()).append(", language=").append(mainLanguage);

        return str.toString();
    }
}
