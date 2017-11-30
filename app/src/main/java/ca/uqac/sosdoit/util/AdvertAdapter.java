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

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder>
{
    private List<Advert> adverts;
    private ColorStatus colorStatus;

    public AdvertAdapter(List<Advert> adverts, ColorStatus colorStatus)
    {
        this.adverts = adverts;
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
        Advert advert = adverts.get(position);
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
        holder.statusBar.setBackgroundColor(colorStatus.getColor(advert.getStatus()));
    }

    @Override
    public int getItemCount()
    {
        return adverts.size();
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
        private int available;
        private int accepted;
        private int completed;
        private int rated;
        private int canceled;
        private int deleted;

        public ColorStatus() {}

        public int getColor(Advert.Status status)
        {
            switch (status) {
                case AVAILABLE:
                    return available;
                case ACCEPTED:
                    return accepted;
                case COMPLETED:
                    return completed;
                case RATED:
                    return rated;
                case CANCELED:
                    return canceled;
                case DELETED:
                    return deleted;
                default:
                    return Color.TRANSPARENT;
            }
        }

        public ColorStatus setAvailable(int available)
        {
            this.available = available;
            return this;
        }

        public ColorStatus setAccepted(int accepted)
        {
            this.accepted = accepted;
            return this;
        }

        public ColorStatus setCompleted(int completed)
        {
            this.completed = completed;
            return this;
        }

        public ColorStatus setRated(int rated)
        {
            this.rated = rated;
            return this;
        }

        public ColorStatus setCanceled(int canceled)
        {
            this.canceled = canceled;
            return this;
        }

        public ColorStatus setDeleted(int deleted)
        {
            this.deleted = deleted;
            return this;
        }
    }
}
