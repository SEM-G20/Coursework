package com.napier.sem;


/**
 * Languages object is an object created by taking data from the world.sql database and processing it
 * Contains different variable to represent specific data in regards to different languages
 * Holds population, percentageOfPopulation and languageName values
 * Makes use of getters and setters to get population, percentageOfPopulation and languageName values
 * Used in languagesByPop method in dataHolder
 */

public class Languages {

    private long population;
    private String percentageOfPopulation;
    private String languageName;
    public Languages(){};

    public long getPopulation(){return population;}
    public String getPercentageOfPopulation(){
        return percentageOfPopulation;
    }
    public String getLanguageName(){return languageName;}

    public void setLanguageName(String LanguageName){languageName=LanguageName;}
    public void setPopulation(long Population){
        population = Population;
    }

    public void setPercentageOfPopulation(String PercentageOfPopulation){
        percentageOfPopulation = PercentageOfPopulation;
    }



}
