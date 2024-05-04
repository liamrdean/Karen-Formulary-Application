package com.example.karenformulary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {






    private Context mContext;
    private List<String> expandableListTitle_EN;
    private HashMap<String, List<String>> expandableListDetail_EN;
    private List<String> expandableListTitle_KA;
    private HashMap<String, List<String>> expandableListDetail_KA;


    public CustomExpandableListAdapter(Context mContext, List<String> expandableListTitle_EN,
                                       HashMap<String, List<String>> expandableListDetail_EN,
                                       List<String> expandableListTitle_KA,
                                       HashMap<String, List<String>> expandableListDetail_KA) {

        this.mContext = mContext;
        this.expandableListTitle_EN = expandableListTitle_EN;
        this.expandableListDetail_EN = expandableListDetail_EN;

        this.expandableListTitle_KA = expandableListTitle_KA;
        this.expandableListDetail_KA = expandableListDetail_KA;
    }


    @Override
    public int getGroupCount() {
        return this.expandableListTitle_EN.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.expandableListDetail_EN.get(this.expandableListTitle_EN.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition, ActivityMain.isKaren);
    }
    public Object getGroup(int groupPosition, boolean isKaren) {
        Log.i("DRUGJK", expandableListTitle_KA.toString() + " " + expandableListTitle_EN.toString());
        if (isKaren) {
            return this.expandableListTitle_KA.get(groupPosition);
        } else {
            return this.expandableListTitle_EN.get(groupPosition);
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // return this.expandableListDetail_EN.get(this.expandableListTitle_EN.get(groupPosition)).get(childPosition);
        return getChild(groupPosition, childPosition, ActivityMain.isKaren);
    }

    public Object getChild(int groupPosition, int childPosition, boolean isKaren) {
        if (isKaren) {
            return this.expandableListDetail_KA.get(this.expandableListTitle_KA.get(groupPosition)).get(childPosition);
        } else {
            return this.expandableListDetail_EN.get(this.expandableListTitle_EN.get(groupPosition)).get(childPosition);
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group,null);
        }

        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Log.i("DEMOC", String.format("Child %d %d", groupPosition, childPosition));
        String expandedListText_EN = (String) getChild(groupPosition,childPosition, false);
        String expandedListText_KA = (String) getChild(groupPosition,childPosition, true);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater)
                    this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item,null);

        }

        ImageTextView expandedListTextView = convertView.findViewById(R.id.expandedListItem);
        //expandedListTextView.add();

        expandedListTextView.setData(expandedListText_EN, expandedListText_KA);

        // Sets data, handles setting if this is an image
        //expandedListTextView.setBackgroundColor(Color.parseColor("#0000FF"));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}