package com.napier.sem;

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
