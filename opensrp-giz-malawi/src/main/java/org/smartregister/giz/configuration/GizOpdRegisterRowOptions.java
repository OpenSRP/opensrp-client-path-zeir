package org.smartregister.giz.configuration;

import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.giz.R;
import org.smartregister.opd.configuration.OpdRegisterRowOptions;
import org.smartregister.opd.holders.OpdRegisterViewHolder;
import org.smartregister.opd.utils.OpdConstants;
import org.smartregister.opd.utils.OpdDbConstants;
import org.smartregister.view.contract.SmartRegisterClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Ephraim Kigamba - ekigamba@ona.io on 2019-10-08
 */

public class GizOpdRegisterRowOptions implements OpdRegisterRowOptions {

    @Override
    public boolean isDefaultPopulatePatientColumn() {
        return false;
    }

    @Override
    public void populateClientRow(@NonNull Cursor cursor, @NonNull CommonPersonObjectClient commonPersonObjectClient
            , @NonNull SmartRegisterClient smartRegisterClient, @NonNull OpdRegisterViewHolder opdRegisterViewHolder) {
        Map<String, String> columnMaps = commonPersonObjectClient.getColumnmaps();

        String strVisitEndDate = columnMaps.get(OpdDbConstants.Column.OpdDetails.CURRENT_VISIT_END_DATE);

        if(strVisitEndDate != null){
            Date visitEndDate = null;
            try {
                visitEndDate = new SimpleDateFormat(OpdConstants.DateFormat.YYYY_MM_DD_HH_MM_SS, Locale.ENGLISH).parse(strVisitEndDate);

                // Get the midnight of that day when the visit happened
                Calendar date = Calendar.getInstance();
                date.setTime(visitEndDate);
                // reset hour, minutes, seconds and millis
                date.set(Calendar.HOUR_OF_DAY, 0);
                date.set(Calendar.MINUTE, 0);
                date.set(Calendar.SECOND, 0);
                date.set(Calendar.MILLISECOND, 0);

                // next day
                date.add(Calendar.DAY_OF_MONTH, 1);
                if (new Date().before(date.getTime())) {
                    opdRegisterViewHolder.dueButton.setText(R.string.treated);
                    opdRegisterViewHolder.dueButton.setTextColor(Color.parseColor("#219e05"));
                    opdRegisterViewHolder.dueButton.setAllCaps(false);
                    opdRegisterViewHolder.dueButton.setBackgroundResource(R.color.transparent);
                    return;
                }
            } catch (ParseException e) {
                Timber.e(e);
            }
        }

        String booleanString = columnMaps.get(OpdDbConstants.Column.OpdDetails.PENDING_DIAGNOSE_AND_TREAT);

        if (parseBoolean(booleanString)) {
            opdRegisterViewHolder.dueButton.setText(R.string.diagnose_and_treat);
            opdRegisterViewHolder.dueButton.setBackgroundResource(R.drawable.diagnose_treat_bg);
        } else {
            opdRegisterViewHolder.dueButton.setText(R.string.check_in);
            opdRegisterViewHolder.dueButton.setBackgroundResource(R.color.transparent);
        }

    }

    private boolean parseBoolean(@Nullable String booleanString) {
        return (!TextUtils.isEmpty(booleanString) && (
                (booleanString.length() == 1 && "1".equals(booleanString))
                        || (booleanString.length() > 1 && Boolean.parseBoolean(booleanString))));
    }

    @Override
    public boolean isCustomViewHolder() {
        return false;
    }

    @Nullable
    @Override
    public OpdRegisterViewHolder createCustomViewHolder(@NonNull View itemView) {
        return null;
    }

    @Override
    public boolean useCustomViewLayout() {
        return false;
    }

    @Override
    public int getCustomViewLayoutId() {
        return 0;
    }
}