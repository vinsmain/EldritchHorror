package ru.mgusev.eldritchhorror.model;

import java.util.HashMap;

public class Specialization {

    private static Specialization instance;
    private static HashMap<String, Integer> specMap;
    private static final String ALL_ROUNDER = "All-Rounder";
    private static final String COMBAT = "Combat";
    private static final String EXPEDITION = "Expedition";
    private static final String GATE_CLOSER = "Gate Closer";
    private static final String MAGIC = "Magic";
    private static final String RESEARCH = "Research";
    private static final String SUPPORT = "Support";

    private Specialization() {
        specMap = new HashMap<>();
        specMap.put(ALL_ROUNDER, 1);
        specMap.put(COMBAT, 2);
        specMap.put(EXPEDITION, 3);
        specMap.put(GATE_CLOSER, 4);
        specMap.put(MAGIC, 5);
        specMap.put(RESEARCH, 6);
        specMap.put(SUPPORT, 7);
    }

    public static Specialization getInstance(){
        if (null == instance){
            instance = new Specialization();
        }
        return instance;
    }
}
