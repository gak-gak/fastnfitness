package com.easyfitness.bodymeasures;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.easyfitness.BtnClickListener;
import com.easyfitness.DAO.Profile;
import com.easyfitness.DAO.bodymeasures.BodyMeasure;
import com.easyfitness.DAO.bodymeasures.BodyPart;
import com.easyfitness.DAO.bodymeasures.DAOBodyMeasure;
import com.easyfitness.DatePickerDialogFragment;
import com.easyfitness.MainActivity;
import com.easyfitness.R;
import com.easyfitness.graph.DateGraph;
import com.easyfitness.utils.DateConverter;
import com.easyfitness.utils.ExpandedListView;
import com.easyfitness.utils.Keyboard;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.onurkaganaldemir.ktoastlib.KToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Weight Track, Body Track 두 카테고리에서 나타나는 새로운 측정값 입력이나 클릭반응 ,Delete(Record옆에 X로 나타남),
 * 그래프 생성, Record Table 생성(그래프 밑에 기록들 나타나는 거) 등등
**/
public class BodyPartDetailsFragment extends Fragment {
    Button addButton = null;
    EditText measureEdit = null;
    EditText dateEdit = null;
    ExpandedListView measureList = null;
    Toolbar bodyToolbar = null;
    DatePickerDialogFragment mDateFrag = null;
    private String name;
    private int mBodyPartID;
    private LineChart mChart = null;
    private DateGraph mDateGraph = null;
    private DAOBodyMeasure mBodyMeasureDb = null;
    private DatePickerDialog.OnDateSetListener dateSet = (view, year, month, day) -> dateEdit.setText(DateConverter.dateToString(year, month + 1, day));
    private OnClickListener clickDateEdit = v -> showDatePickerFragment();
    private OnFocusChangeListener focusDateEdit = (v, hasFocus) -> {
        if (hasFocus) {
            showDatePickerFragment();
        }
    };
    private BtnClickListener itemClickDeleteRecord = this::showDeleteDialog;
    private OnClickListener onClickAddMeasure = new OnClickListener() {  //새로운 데이터 입력하기 위해 해당칸 클릭한 후 반응
        @Override
        public void onClick(View v) {
            if (!measureEdit.getText().toString().isEmpty()) {  //입력한 데이터가 있으면

                Date date = DateConverter.editToDate(dateEdit.getText().toString());
                //새 Edit Value 입력
                mBodyMeasureDb.addBodyMeasure(date, mBodyPartID, Float.valueOf(measureEdit.getText().toString()), getProfile().getId());
                refreshData();  //데이터 업데이트
                measureEdit.setText("");  //Body Track 새 데이터 입력 칸에 새로운 값을 Add하면 칸에 남아있는 글자 없어짐

                Keyboard.hide(getContext(), v);
            } else {  //입력 데이터가 없으면 경고창 출력
                KToast.errorToast(getActivity(), "Please enter a measure", Gravity.BOTTOM, KToast.LENGTH_SHORT);

            }
        }
    };

    private OnItemLongClickListener itemlongclickDeleteRecord = (listView, view, position, id) -> {

        // Get the cursor, positioned to the corresponding row in the result set
        //Cursor cursor = (Cursor) listView.getItemAtPosition(position);

        final long selectedID = id;

        String[] profilListArray = new String[1]; // un seul choix
        profilListArray[0] = getActivity().getResources().getString(R.string.DeleteLabel);

        AlertDialog.Builder itemActionBuilder = new AlertDialog.Builder(getActivity());
        itemActionBuilder.setTitle("").setItems(profilListArray, (dialog, which) -> {

            switch (which) {
                // Delete
                case 0:
                    mBodyMeasureDb.deleteMeasure(selectedID);
                    refreshData();
                    KToast.infoToast(getActivity(), getActivity().getResources().getText(R.string.removedid).toString() + " " + selectedID, Gravity.BOTTOM, KToast.LENGTH_SHORT);
                    break;
                default:
            }
        });
        itemActionBuilder.show();

        return true;
    };

    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static BodyPartDetailsFragment newInstance(int bodyPartID, boolean showInput) {
        BodyPartDetailsFragment f = new BodyPartDetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("bodyPartID", bodyPartID);
        args.putBoolean("showInput", showInput);
        f.setArguments(args);

        return f;
    }

    private void showDatePickerFragment() {
        if (mDateFrag == null) {
            mDateFrag = DatePickerDialogFragment.newInstance(dateSet);
        }

        FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
        mDateFrag.show(ft, "dialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bodytracking_details_fragment, container, false);

        addButton = view.findViewById(R.id.buttonAddWeight);
        measureEdit = view.findViewById(R.id.editWeight);
        dateEdit = view.findViewById(R.id.profilEditDate);
        measureList = view.findViewById(R.id.listWeightProfil);
        bodyToolbar = view.findViewById(R.id.bodyTrackingDetailsToolbar);
        CardView c = view.findViewById(R.id.addMeasureCardView);

        /* Initialisation BodyPart */
        mBodyPartID = getArguments().getInt("bodyPartID", 0);
        BodyPart mBodyPart = new BodyPart(mBodyPartID);

        // Hide Input if needed.
        if (!getArguments().getBoolean("showInput", true))
            c.setVisibility(View.GONE);

        /* Initialisation des boutons */
        addButton.setOnClickListener(onClickAddMeasure);
        dateEdit.setOnClickListener(clickDateEdit);
        dateEdit.setOnFocusChangeListener(focusDateEdit);
        measureList.setOnItemLongClickListener(itemlongclickDeleteRecord);

        /* Initialisation des evenements */

        // Add the other graph
        mChart = view.findViewById(R.id.weightChart);  //완성된 그래프 출력
        mChart.setDescription(null);  //그래프밑에 세부사항 나타내지 않음.
        mDateGraph = new DateGraph(getContext(), mChart, "");
        mBodyMeasureDb = new DAOBodyMeasure(view.getContext());

        // Set Initial text
        dateEdit.setText(DateConverter.currentDate());  //Date 선택 부분에 오늘날짜로 자동 설정

        ((MainActivity) getActivity()).getActivityToolbar().setVisibility(View.GONE);
        bodyToolbar.setTitle(getContext().getString(mBodyPart.getResourceNameID()));
        bodyToolbar.setNavigationIcon(R.drawable.ic_back);
        bodyToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshData();
    }
    /**DrawGraph(), FillRecordTable() 둘 다 void method 인데 후에 거울에 나타나게 하려면 graph랑 record table 따로 변수에 저장해서 가져올 수 있게 코드 수정해야 할듯**/
    private void DrawGraph(List<BodyMeasure> valueList) {  //저장된 값을 이용해 그래프 그리기

        // Recupere les enregistrements
        if (valueList.size() < 1) {
            mChart.clear();
            return;
        }

        ArrayList<Entry> yVals = new ArrayList<>();

        float minBodyMeasure = -1;

        for (int i = valueList.size() - 1; i >= 0; i--) {
            Entry value = new Entry((float) DateConverter.nbDays(valueList.get(i).getDate().getTime()), valueList.get(i).getBodyMeasure());
            yVals.add(value);
            if (minBodyMeasure == -1) minBodyMeasure = valueList.get(i).getBodyMeasure();
            else if (valueList.get(i).getBodyMeasure() < minBodyMeasure)
                minBodyMeasure = valueList.get(i).getBodyMeasure();
        }

        mDateGraph.draw(yVals);
    }


    private void FillRecordTable(List<BodyMeasure> valueList) {  //그래프 아래 나와있는 몸무게 기록 record table 만들기
        Cursor oldCursor = null;

        if (valueList.isEmpty()) {
            //Toast.makeText(getActivity(), "No records", Toast.LENGTH_SHORT).show();
            measureList.setAdapter(null);
        } else {
            // ...
            if (measureList.getAdapter() == null) {
                BodyMeasureCursorAdapter mTableAdapter = new BodyMeasureCursorAdapter(this.getView().getContext(), mBodyMeasureDb.getCursor(), 0, itemClickDeleteRecord);
                measureList.setAdapter(mTableAdapter);
            } else {
                oldCursor = ((BodyMeasureCursorAdapter) measureList.getAdapter()).swapCursor(mBodyMeasureDb.getCursor());
                if (oldCursor != null)
                    oldCursor.close();
            }
        }
    }

    public String getName() {
        return getArguments().getString("name");
    }

    private void refreshData() {  /**이 부분 중요**/
        View fragmentView = getView();
        if (fragmentView != null) {
            if (getProfile() != null) {
                List<BodyMeasure> valueList = mBodyMeasureDb.getBodyPartMeasuresList(mBodyPartID, getProfile());
                DrawGraph(valueList);  //여기서 그래프 생성
                // update table
                FillRecordTable(valueList);  //그래프 아래 Record 리스트 생성
            }
        }
    }

    private void showDeleteDialog(final long idToDelete) {  //그래프 밑에 나와있는 record table에 X표시 관련(data delete)

        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:  //버튼 클릭
                    mBodyMeasureDb.deleteMeasure(idToDelete);  //DB에서 데이터 삭제
                    refreshData();  //데이터를 지웠으니 다시 그래프 등등을 업데이트
                    Toast.makeText(getActivity(), getResources().getText(R.string.removedid) + " " + idToDelete, Toast.LENGTH_SHORT)
                        .show();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        };
        //한 번 더 물어보는 알림창
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getText(R.string.DeleteRecordDialog)).setPositiveButton(getResources().getText(R.string.global_yes), dialogClickListener)
            .setNegativeButton(getResources().getText(R.string.global_no), dialogClickListener).show();

    }

    private Profile getProfile() {
        return ((MainActivity) getActivity()).getCurrentProfil();
    }

    public Fragment getFragment() {
        return this;
    }

/*
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) refreshData();
    }
*/
}
