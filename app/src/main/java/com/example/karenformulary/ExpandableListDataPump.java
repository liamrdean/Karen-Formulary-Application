package com.example.karenformulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {

    public static HashMap<String , List<String>> getData() {

        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> Note = new ArrayList<>();
        Note.add("India");
        Note.add("Pakistan");
        Note.add("Australia");
        Note.add("Viet Nam");
        Note.add("South Africa");

        List<String> Food = new ArrayList<>();
        Food.add("Brazil");
        Food.add("Spain");
        Food.add("Germany");
        Food.add("Netherlands");
        Food.add("Italy");

        List<String> BeCareful = new ArrayList<>();
        BeCareful.add("United States");
        BeCareful.add("Spain");
        BeCareful.add("Argentina");
        BeCareful.add("France");
        BeCareful.add("Russia");

        List<String> DoNotGive = new ArrayList<>();
        DoNotGive.add("United States");
        DoNotGive.add("Spain");
        DoNotGive.add("Argentina");
        DoNotGive.add("France");
        DoNotGive.add("Russia");

        List<String> SideEffects = new ArrayList<>();
        SideEffects.add("Spain");
        SideEffects.add("Argentina");
        SideEffects.add("France");
        SideEffects.add("Russia");

        List<String> Interactions = new ArrayList<>();
        Interactions.add("United States");
        Interactions.add("Spain");
        Interactions.add("Argentina");
        Interactions.add("France");
        Interactions.add("Russia");

        List<String> Pregnancy = new ArrayList<>();
        Pregnancy.add("United States");
        Pregnancy.add("Spain");
        Pregnancy.add("Argentina");
        Pregnancy.add("France");
        Pregnancy.add("Russia");

        List<String> BreastFeeding = new ArrayList<>();
        BreastFeeding.add("United States");
        BreastFeeding.add("Spain");
        BreastFeeding.add("Argentina");
        BreastFeeding.add("France");
        BreastFeeding.add("Russia");

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