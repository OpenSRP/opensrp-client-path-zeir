package org.smartregister.path.reporting.dropuout;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.apache.commons.lang3.tuple.Triple;
import org.smartregister.child.domain.NamedObject;
import org.smartregister.child.toolbar.LocationSwitcherToolbar;
import org.smartregister.immunization.db.VaccineRepo;
import org.smartregister.path.R;
import org.smartregister.path.reporting.BaseReportActivity;
import org.smartregister.path.reporting.dropuout.receiver.CoverageDropoutBroadcastReceiver;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keyman on 08/01/18.
 */
public class MeaslesCumulativeDropoutReportActivity extends BaseReportActivity {
    private ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle("");

        LocationSwitcherToolbar toolbar = (LocationSwitcherToolbar) getToolbar();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(org.smartregister.path.activity.MeaslesCumulativeDropoutReportActivity.this, DropoutReportsActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
            }
        });

        ((TextView) toolbar.findViewById(R.id.title)).setText(getString(R.string.measles_cumulative_dropout_report));

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
        expandableListView.setDivider(null);
        expandableListView.setDividerHeight(0);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_dropout_report_template;
    }

    @Override
    protected int getDrawerLayoutId() {
        return R.id.drawer_layout;
    }

    @Override
    protected int getToolbarId() {
        return LocationSwitcherToolbar.TOOLBAR_ID;
    }

    @Override
    protected Class onBackActivity() {
        return null;
    }

    @Override
    protected String getActionType() {
        return CoverageDropoutBroadcastReceiver.TYPE_GENERATE_CUMULATIVE_INDICATORS;
    }

    @Override
    protected int getParentNav() {
        return R.id.dropout_reports;
    }

    @SuppressWarnings("unchecked")
    private void updateExpandableList(final LinkedHashMap<Pair<String, String>, List<ExpandedListAdapter.ItemData<Triple<String, String, String>, Date>>> map) {
        ExpandedListAdapter<Pair<String, String>, Triple<String, String, String>, Date> expandableListAdapter = new ExpandedListAdapter(MeaslesCumulativeDropoutReportActivity.this, map, R.layout.dropout_report_cumulative_header, R.layout.dropout_report_item);
        expandableListAdapter.setChildSelectable(false);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListAdapter.notifyDataSetChanged();
    }

    @Override
    protected Map<String, NamedObject<?>> generateReportBackground() {
        LinkedHashMap<Pair<String, String>, List<ExpandedListAdapter.ItemData<Triple<String, String, String>, Date>>> linkedHashMap = generateCumulativeDropoutMap(VaccineRepo.Vaccine.measles1, VaccineRepo.Vaccine.measles2);
        NamedObject<LinkedHashMap<Pair<String, String>, List<ExpandedListAdapter.ItemData<Triple<String, String, String>, Date>>>> linkedHashMapNamedObject = new NamedObject<>(LinkedHashMap.class.getName(), linkedHashMap);

        Map<String, NamedObject<?>> map = new HashMap<>();
        map.put(linkedHashMapNamedObject.name, linkedHashMapNamedObject);
        return map;
    }

    @Override
    protected void generateReportUI(Map<String, NamedObject<?>> map, boolean userAction) {
        LinkedHashMap<Pair<String, String>, List<ExpandedListAdapter.ItemData<Triple<String, String, String>, Date>>> linkedHashMap = new LinkedHashMap<>();

        if (map.containsKey(LinkedHashMap.class.getName())) {
            NamedObject<?> namedObject = map.get(LinkedHashMap.class.getName());
            if (namedObject != null) {
                linkedHashMap = (LinkedHashMap<Pair<String, String>, List<ExpandedListAdapter.ItemData<Triple<String, String, String>, Date>>>) namedObject.object;
            }
        }

        updateExpandableList(linkedHashMap);
    }

    @Override
    public void onUniqueIdFetched(Triple<String, Map<String, String>, String> triple, String s) {
        // override to remove functionality
    }

    @Override
    public void onNoUniqueId() {
        // override to remove functionality
    }

    @Override
    public void onRegistrationSaved(boolean b) {
        // override to remove functionality
    }
}

