package uk.co.dooapp.hae_launcher.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.co.dooapp.hae_launcher.R;
import uk.co.dooapp.hae_launcher.models.AppInfo;

public class AppListRecyclerViewAdapter extends RecyclerView.Adapter<AppListRecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<AppInfo> mApps = new ArrayList<>();

    public AppListRecyclerViewAdapter(Context context, ArrayList<AppInfo> apps) {
        mContext = context;
        mApps = apps;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView appIcon;
        TextView appName;
        LinearLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.appIcon);
            appName = itemView.findViewById(R.id.appName);
            parentLayout = itemView.findViewById(R.id.parent);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_list_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.appIcon.setImageDrawable(mApps.get(position).getIcon());
        holder.appName.setText(mApps.get(position).getName());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = mContext.getPackageManager().getLaunchIntentForPackage(mApps.get(position).getPackageName());
                mContext.startActivity(launchIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }
}
