package com.example.karenformulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String , List<String>> getData() {

        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> Note = new ArrayList<>();
        Note.add("note 1");
        Note.add("$note 2");
        Note.add("note 3");
        Note.add("note 4");
        Note.add("note 5");

        List<String> Food = new ArrayList<>();
        Food.add("food 1");
        Food.add("food 2");
        Food.add("food 3");
        Food.add("food 4");
        Food.add("food 5");

        List<String> BeCareful = new ArrayList<>();
        BeCareful.add("BeCareful 1");
        BeCareful.add("BeCareful 2");
        BeCareful.add("BeCareful 3");
        BeCareful.add("BeCareful 4");
        BeCareful.add("BeCareful 5");

        List<String> DoNotGive = new ArrayList<>();
        DoNotGive.add("DoNotGive 1");
        DoNotGive.add("DoNotGive 2");
        DoNotGive.add("DoNotGive 3");
        DoNotGive.add("DoNotGive 4");
        DoNotGive.add("DoNotGive 5");

        List<String> SideEffects = new ArrayList<>();
        SideEffects.add("SideEffects 1");
        SideEffects.add("SideEffects 2");
        SideEffects.add("SideEffects 3");
        SideEffects.add("SideEffects 4");
        SideEffects.add("SideEffects 5");

        List<String> Interactions = new ArrayList<>();
        Interactions.add("Interactions 1");
        Interactions.add("Interactions 2");
        Interactions.add("Interactions 3");
        Interactions.add("Interactions 4");
        Interactions.add("Interactions 5");

        List<String> Pregnancy = new ArrayList<>();
        Pregnancy.add("Pregnancy 1");
        Pregnancy.add("Pregnancy 2");
        Pregnancy.add("Pregnancy 3");
        Pregnancy.add("Pregnancy 4");
        Pregnancy.add("Pregnancy 5");

        List<String> BreastFeeding = new ArrayList<>();
        BreastFeeding.add("BreastFeeding 1");
        BreastFeeding.add("BreastFeeding 2");
        BreastFeeding.add("BreastFeeding 3");
        BreastFeeding.add("BreastFeeding 4");
        BreastFeeding.add("BreastFeeding 5");

        expandableListDetail.put("Note",Note);
        expandableListDetail.put("Food",Food);
        expandableListDetail.put("Be Careful",BeCareful);
        expandableListDetail.put("Do Not Give",DoNotGive);
        expandableListDetail.put("Side-Effects",SideEffects);
        expandableListDetail.put("Interactions",Interactions);
        expandableListDetail.put("Pregnancy",Pregnancy);
        expandableListDetail.put("Breast Feeding",BreastFeeding);

        return expandableListDetail;
    }
}