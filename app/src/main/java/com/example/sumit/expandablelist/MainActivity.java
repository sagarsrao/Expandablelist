package com.example.sumit.expandablelist;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.example.sumit.expandablelist.DetailInfo;
import com.example.sumit.expandablelist.HeaderInfo;
import com.example.sumit.expandablelist.MyListAdapter;
import com.example.sumit.expandablelist.R;

public class MainActivity extends Activity implements OnClickListener{

    private LinkedHashMap<String, HeaderInfo> myDepartments = new LinkedHashMap<String, HeaderInfo>();
    private ArrayList<HeaderInfo> deptList = new ArrayList<HeaderInfo>();

   // private MyListAdapter listAdapter;
    private MyListAdapter listAdapter;
    private ExpandableListView myList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner = (Spinner) findViewById(R.id.department);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //Just add some data to start with
        loadData();

        //get reference to the ExpandableListView
        myList = (ExpandableListView) findViewById(R.id.myList);
        //create the adapter by passing your ArrayList data
        listAdapter = new MyListAdapter(MainActivity.this, deptList);
        //attach the adapter to the list
        myList.setAdapter(listAdapter);

        //expand all Groups
        expandAll();

        //add new item to the List
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);

        //listener for child row click
        myList.setOnChildClickListener(myListItemClicked);
        //listener for group heading click
        myList.setOnGroupClickListener(myListGroupClicked);


    }

    public void onClick(View v) {

        switch (v.getId()) {

            //add entry to the List
            case R.id.add:

                Spinner spinner = (Spinner) findViewById(R.id.department);
                String department = spinner.getSelectedItem().toString();
                EditText editText = (EditText) findViewById(R.id.product);
                String product = editText.getText().toString();
                editText.setText("");

                //add a new item to the list
                int groupPosition = addProduct(department,product);
                //notify the list so that changes can take effect
                listAdapter.notifyDataSetChanged();

                //collapse all groups
                collapseAll();
                //expand the group where item was just added
                myList.expandGroup(groupPosition);
                //set the current group to be selected so that it becomes visible
                myList.setSelectedGroup(groupPosition);

                break;

            // More buttons go here (if any) ...

        }
    }


    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            myList.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            myList.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){

        addProduct("Apparel","Activewear");
        addProduct("Apparel","Jackets");
        addProduct("Apparel","Shorts");

        addProduct("Beauty","Fragrances");
        addProduct("Beauty","Makeup");

    }

    //our child listener
    private OnChildClickListener myListItemClicked =  new OnChildClickListener() {

        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = deptList.get(groupPosition);
            //get the child info
            DetailInfo detailInfo =  headerInfo.getProductList().get(childPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Clicked on Detail " + headerInfo.getName()
                    + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
            return false;
        }

    };

    //our group listener
    private OnGroupClickListener myListGroupClicked =  new OnGroupClickListener() {

        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {

            //get the group header
            HeaderInfo headerInfo = deptList.get(groupPosition);
            //display it or do something with it
            Toast.makeText(getBaseContext(), "Child on Header " + headerInfo.getName(),
                    Toast.LENGTH_LONG).show();

            return false;
        }

    };

    //here we maintain our products in various departments
    private int addProduct(String department, String product){

        int groupPosition = 0;

        //check the hash map if the group already exists
        HeaderInfo headerInfo = myDepartments.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new HeaderInfo();
            headerInfo.setName(department);
            myDepartments.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<DetailInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        DetailInfo detailInfo = new DetailInfo();
        detailInfo.setSequence(String.valueOf(listSize));
        detailInfo.setName(product);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
