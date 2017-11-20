package ca.uqac.sosdoit.util;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ca.uqac.sosdoit.R;
import ca.uqac.sosdoit.data.Address;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.AdvertStatus;
import ca.uqac.sosdoit.data.Task;

/**
 * Created by benja on 13/11/2017.
 */

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.MyViewHolder> {

    private List<Advert> advertList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView idAdvert,task,description, creationDate, workAddress, status, price, idAdvertiser, idWorker;
        public MyViewHolder(View view) {
            super(view);
            idAdvert = view.findViewById(R.id.idAdvert);
            task = view.findViewById(R.id.task);
            description = view.findViewById(R.id.description);
            creationDate = view.findViewById(R.id.creationDate);
            workAddress = view.findViewById(R.id.workAddress);
            status = view.findViewById(R.id.status);
            price = view.findViewById(R.id.price);
            idAdvertiser = view.findViewById(R.id.idAdvertiser);
            idWorker = view.findViewById(R.id.idWorker);
        }
    }


    public AdvertAdapter(List<Advert> moviesList) {
        this.advertList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advert_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Advert advert = advertList.get(position);
        holder.idAdvert.setText(advert.getIdAdvert());
        holder.task.setText(advert.getTask().toString());
        holder.description.setText(advert.getDescription());
        holder.creationDate.setText(advert.getCreationDate().toString());
        holder.workAddress.setText(advert.getWorkAddress().toString());
        holder.status.setText(advert.getStatus().toString());
        holder.price.setText("0");
        holder.idAdvertiser.setText(advert.getIdAdvertiser());
        holder.idWorker.setText(advert.getIdWorker());
    }

    @Override
    public int getItemCount() {
        return advertList.size();
    }
}
