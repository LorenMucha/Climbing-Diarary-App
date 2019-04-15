package com.example.climbingdiary.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.climbingdiary.R;
import com.example.climbingdiary.RoutesFragment;
import com.example.climbingdiary.database.TaskRepository;
import com.example.climbingdiary.models.Area;
import com.example.climbingdiary.models.Levels;
import com.example.climbingdiary.models.Rating;
import com.example.climbingdiary.models.Route;
import com.example.climbingdiary.models.Sector;
import com.example.climbingdiary.models.Styles;
import com.example.climbingdiary.models.Ui.SetDate;

import java.util.regex.Pattern;

public class EditRouteDialog  extends DialogFragment {
    public EditRouteDialog() {}
    public static EditRouteDialog newInstance(String title, int _id){
        EditRouteDialog edit = new  EditRouteDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("id",_id);
        edit.setArguments(args);
        return edit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_route, container);
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final Context _context = view.getContext();

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Bearbeiten");
        //get the route value which will be edit
        final int route_id = getArguments().getInt("id",0);
        Route editRoute = Route.getRoute(route_id);
        Log.d("route set",editRoute.toString());

        // Initialize a new foreground color span instance
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);

        // Initialize a new spannable string builder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(title);

        // Apply the text color span
        ssBuilder.setSpan(foregroundColorSpan,0,title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        getDialog().setTitle(ssBuilder);

        // Set title divider color
        int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null)
            titleDivider.setBackgroundColor(getResources().getColor(android.R.color.black));

        //input elements
        Button closeDialog = (Button) view.findViewById(R.id.input_route_close);
        Button saveRoute = (Button) view.findViewById(R.id.input_route_save);
        saveRoute.setText("Update");
        final Spinner stil = (Spinner) view.findViewById(R.id.input_route_stil);
        final Spinner level = (Spinner) view.findViewById(R.id.input_route_level);
        final Spinner rating = (Spinner) view.findViewById(R.id.input_route_rating);
        final EditText date = (EditText) view.findViewById(R.id.input_route_date);
        final EditText name = (EditText) view.findViewById(R.id.input_route_name);
        final AutoCompleteTextView area = (AutoCompleteTextView) view.findViewById(R.id.input_route_area);
        final AutoCompleteTextView sector = (AutoCompleteTextView) view.findViewById(R.id.input_route_sektor);
        final EditText comment = (EditText) view.findViewById(R.id.input_route_comment);

        name.setText(editRoute.getName());

        // set Spinner for choosing the style
        ArrayAdapter<String> stilArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Styles.getStyle(true));
        stilArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        stil.setAdapter(stilArrayAdapter);
        stil.setSelection(stilArrayAdapter.getPosition(editRoute.getStyle().toUpperCase()));

        // set Spinner for choosing the level
        ArrayAdapter<String> levelArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Levels.getLevels());
        levelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        level.setAdapter(levelArrayAdapter);
        level.setSelection(levelArrayAdapter.getPosition(editRoute.getLevel()));

        //get the route List and set autocomplete
        ArrayAdapter<String> areaArrayAdapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, Area.getRouteNameList(_context));
        //will start working from first character
        area.setThreshold(1);
        area.setAdapter(areaArrayAdapter);
        area.setText(editRoute.getArea());
        sector.setText(editRoute.getSector());
        comment.setText(editRoute.getComment());

        //get the sector list and set the autocomplete if area is set
        area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                Log.d("sector list", TextUtils.join(",", Sector.getSectorList(_context,area.getText().toString().trim())));
                ArrayAdapter<String> sectorArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Sector.getSectorList(_context,area.getText().toString().trim()));
                //will start working from first character
                sector.setThreshold(1);
                sector.setAdapter(sectorArrayAdapter);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // set Spinner for choosing the level
        ArrayAdapter<String> ratingArrayAdapter = new ArrayAdapter<String>(_context,android.R.layout.simple_spinner_item, Rating.getRating());
        ratingArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        rating.setAdapter(ratingArrayAdapter);
        rating.setSelection(editRoute.getRating()-1);

        //set date listener
        SetDate setDate = new SetDate(date, _context);
        StringBuilder sBuilder = new StringBuilder();
        String[] dateSplit = (editRoute.getDate()).split(Pattern.quote("."));
        sBuilder.append(dateSplit[2]).append("-").append(dateSplit[1]).append("-").append(dateSplit[0]);
        date.setText(sBuilder.toString());

        //save the route
        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String route_name = name.getText().toString();
                String route_level = level.getSelectedItem().toString();
                String route_date = date.getText().toString();
                String route_area = area.getText().toString();
                String route_sector = sector.getText().toString();
                String route_comment = comment.getText().toString();
                int route_rating = rating.getSelectedItemPosition()+1;
                String route_style = stil.getSelectedItem().toString();
                Route new_route = new Route(0,route_name,route_level,route_area,route_sector,route_style,route_rating,route_comment,route_date);
                TaskRepository taskRepository = new TaskRepository();
                taskRepository.open();
                boolean taskState = taskRepository.deleteRoute(route_id);
                if(taskState) {
                    taskRepository.inserRoute(new_route);
                }
                taskRepository.close();
                //close the dialog
                getDialog().cancel();
                RoutesFragment.refreshData();
            }
        });

        //close the dialog
        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });

    }
}