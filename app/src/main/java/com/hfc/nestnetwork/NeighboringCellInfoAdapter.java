package com.hfc.nestnetwork;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoWcdma;
import android.telephony.NeighboringCellInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NeighboringCellInfoAdapter extends BaseAdapter {

    private Context mContext;
    private List<CellInfo> mInfos;

    public NeighboringCellInfoAdapter(Context context, List<CellInfo> infos) {
        mContext = context;
        mInfos = infos;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_neighboring_cell_info, parent, false);
            holder = new ViewHolder();
            holder.lacLabelTextView = (TextView) convertView.findViewById(R.id.lacLabelTextView);
            holder.lacTextView = (TextView) convertView.findViewById(R.id.lacTextView);
            holder.cidLabelTextView = (TextView) convertView.findViewById(R.id.cidLabelTextView);
            holder.cidTextView = (TextView) convertView.findViewById(R.id.cidTextView);
            holder.bsssLabelTextView =(TextView)  convertView.findViewById(R.id.bsssLabelTextView);
            holder.bsssTextView = (TextView) convertView.findViewById(R.id.bsssTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CellInfo cellInfo = mInfos.get(position);
        holder.lacLabelTextView.setText("LAC:");
        if (cellInfo instanceof CellInfoLte) {
            CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            int tac = cellIdentity.getTac();
            holder.lacTextView.setText("lte:"+tac);
            int cid = cellIdentity.getCi();
            holder.cidTextView.setText("gsm  "+cid);
            Log.d("CellInfoUtils", "LAC (TAC) for LTE: " + tac);
        } else if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            int lac = cellIdentity.getLac();

            holder.lacTextView.setText("gsm:"+lac);
            int cid = cellIdentity.getCid();
            holder.cidTextView.setText("gsm  "+cid);
            Log.d("CellInfoUtils", "LAC for GSM: " + lac);
        } else if (cellInfo instanceof CellInfoWcdma) {
            // Handle WCDMA (UMTS) if needed
            CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
            holder.lacTextView.setText("Wcdma:"+cellIdentity.getLac());
            int cid = cellIdentity.getCid();
            holder.cidTextView.setText("Wcdma  "+cid);
        } else if (cellInfo instanceof CellInfoCdma) {
            CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
            int lac = cellIdentity.getNetworkId();
            holder.lacTextView.setText("Cdma:"+lac);
            int cid = cellIdentity.getBasestationId();
            holder.cidTextView.setText("gsm  "+cid);
            Log.d("CellInfoUtils", "LAC for CDMA: " + lac);
        } else if (cellInfo instanceof CellInfoNr) {
            CellIdentityNr cellIdentity = (CellIdentityNr) ((CellInfoNr) cellInfo).getCellIdentity();
            // Handle 5G NR if needed
            holder.lacTextView.setText("5g  "+cellIdentity.getTac());
            holder.cidTextView.setText("5g  "+cellIdentity.toString());
        }else {
            holder.lacTextView.setText(String.valueOf("could not get lac"));

        }

        holder.cidLabelTextView.setText("CID:");
//        holder.cidTextView.setText(String.valueOf(cellInfo.getCellIdentity()));
        holder.bsssLabelTextView.setText("BSSS:");
        holder.bsssTextView.setText(String.valueOf(cellInfo.toString()));

        return convertView;
    }

    static class ViewHolder {
        TextView lacLabelTextView;
        TextView lacTextView;
        TextView cidLabelTextView;
        TextView cidTextView;
        TextView bsssLabelTextView;
        TextView bsssTextView;
    }

}
