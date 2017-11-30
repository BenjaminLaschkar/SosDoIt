package ca.uqac.sosdoit.util;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ca.uqac.sosdoit.R;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Bid;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.ViewHolder>
{
    private List<Advert> jobs;
    private ColorStatus colorStatus;

    public JobAdapter(List<Advert> adverts, ColorStatus colorStatus)
    {
        this.jobs = adverts;
        this.colorStatus = colorStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_advert, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Advert advert = jobs.get(position);
        holder.title.setText(advert.getTitle());
        if (advert.hasDescription()) {
            holder.description.setText(advert.getDescription());
        } else {
            holder.description.setVisibility(View.GONE);
        }
        holder.status.setText(advert.getStatus().name());
        holder.postingDate.setText(Util.formatDate(advert.getPostingDate()));
        if (advert.hasCompletionDate()) {
            holder.completionDate.setText(Util.formatDate(advert.getCompletionDate()));
        } else {
            holder.completionDate.setVisibility(View.GONE);
        }
        holder.statusBar.setBackgroundColor(colorStatus.getColor(advert.getBid().getStatus()));
    }

    @Override
    public int getItemCount()
    {
        return jobs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View statusBar;
        private TextView title, description, status, postingDate, completionDate;

        private ViewHolder(View view)
        {
            super(view);
            statusBar = view.findViewById(R.id.status_bar);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            status = view.findViewById(R.id.status);
            postingDate = view.findViewById(R.id.posting_date);
            completionDate = view.findViewById(R.id.completion_date);
        }
    }

    public static class ColorStatus
    {
        private int accepted;
        private int pending;
        private int rejected;

        public ColorStatus() {}

        public ColorStatus(int accepted, int pending, int rejected)
        {
            this.accepted = accepted;
            this.pending = pending;
            this.rejected = rejected;
        }

        public int getColor(Bid.Status status)
        {
            switch (status) {
                case ACCEPTED:
                    return accepted;
                case PENDING:
                    return pending;
                case REJECTED:
                    return rejected;
                default:
                    return Color.TRANSPARENT;
            }
        }

        public JobAdapter.ColorStatus setAccepted(int accepted)
        {
            this.accepted = accepted;
            return this;
        }

        public JobAdapter.ColorStatus setPending(int pending)
        {
            this.pending = pending;
            return this;
        }

        public JobAdapter.ColorStatus setRejected(int rejected)
        {
            this.rejected = rejected;
            return this;
        }
    }
}
