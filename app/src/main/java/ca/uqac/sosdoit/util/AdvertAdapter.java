package ca.uqac.sosdoit.util;

import android.content.Context;
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

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder>
{
    private Context context;
    private List<Advert> adverts;
    private ColorStatus colorStatus;
    private boolean status;

    public AdvertAdapter(Context context, List<Advert> adverts, ColorStatus colorStatus)
    {
        this.context = context;
        this.adverts = adverts;
        this.colorStatus = colorStatus;
        this.status = true;
    }

    public AdvertAdapter(Context context, List<Advert> adverts, ColorStatus colorStatus, boolean status)
    {
        this(context, adverts, colorStatus);
        this.status = status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.element_advert, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Advert advert = adverts.get(position);

        if (status) {
            if (advert.hasBid()) {
                if (advert.hasWorkerUid() && advert.getWorkerUid().equals(advert.getBid().getUid())) {
                    holder.statusBar.setBackgroundColor(colorStatus.getColor(advert.getStatus()));
                } else {
                    holder.statusBar.setBackgroundColor(colorStatus.getColor(advert.getBid().getStatus()));
                }
            } else {
                holder.statusBar.setBackgroundColor(colorStatus.getColor(advert.getStatus()));
            }
        } else {
            holder.statusBar.setVisibility(View.GONE);
            holder.statusInfo.setVisibility(View.GONE);
            holder.status.setVisibility(View.GONE);
        }

        holder.title.setText(advert.getTitle());

        if (advert.hasDescription()) {
            holder.description.setText(advert.getDescription());
            holder.descriptionInfo.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
        } else {
            holder.descriptionInfo.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
        }

        holder.status.setText(context.getString(advert.getStatus().value()));
        holder.postingDate.setText(Util.formatDate(advert.getPostingDate()));

        if (advert.hasCompletionDate()) {
            holder.completionDate.setText(Util.formatDate(advert.getCompletionDate()));
            holder.completionDateInfo.setVisibility(View.VISIBLE);
            holder.completionDate.setVisibility(View.VISIBLE);
        } else {
            holder.completionDateInfo.setVisibility(View.GONE);
            holder.completionDate.setVisibility(View.GONE);
        }

        if (advert.hasAdvertiserRating()) {
            holder.advertiserRate.setText(String.valueOf(advert.getAdvertiserRating().getRate()));
            holder.advertiserRatingInfo.setVisibility(View.VISIBLE);
            holder.advertiserRate.setVisibility(View.VISIBLE);

            if (advert.getAdvertiserRating().hasComment()) {
                holder.advertiserComment.setText(advert.getAdvertiserRating().getComment());
                holder.advertiserLine.setVisibility(View.VISIBLE);
            } else {
                holder.advertiserLine.setVisibility(View.GONE);
            }
            holder.advertiserComment.setVisibility(View.VISIBLE);
        } else {
            holder.advertiserRatingInfo.setVisibility(View.GONE);
            holder.advertiserRate.setVisibility(View.GONE);
            holder.advertiserLine.setVisibility(View.GONE);
            holder.advertiserComment.setVisibility(View.GONE);
        }

        if (advert.hasWorkerRating()) {
            holder.workerRate.setText(String.valueOf(advert.getWorkerRating().getRate()));
            holder.workerRatingInfo.setVisibility(View.VISIBLE);
            holder.workerRate.setVisibility(View.VISIBLE);

            if (advert.getWorkerRating().hasComment()) {
                holder.workerComment.setText(advert.getWorkerRating().getComment());
                holder.workerLine.setVisibility(View.VISIBLE);
            } else {
                holder.workerLine.setVisibility(View.GONE);
            }
            holder.workerComment.setVisibility(View.VISIBLE);
        } else {
            holder.workerRatingInfo.setVisibility(View.GONE);
            holder.workerRate.setVisibility(View.GONE);
            holder.workerLine.setVisibility(View.GONE);
            holder.workerComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount()
    {
        return adverts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View statusBar, advertiserLine, workerLine;
        private TextView title, descriptionInfo, description, statusInfo, status, postingDate, completionDateInfo, completionDate, advertiserRatingInfo, advertiserRate, advertiserComment, workerRatingInfo, workerRate, workerComment;

        private ViewHolder(View view)
        {
            super(view);

            statusBar = view.findViewById(R.id.status_bar);
            title = view.findViewById(R.id.title);

            descriptionInfo = view.findViewById(R.id.description_info);
            description = view.findViewById(R.id.description);

            statusInfo = view.findViewById(R.id.status_info);
            status = view.findViewById(R.id.status);

            postingDate = view.findViewById(R.id.posting_date);

            completionDateInfo = view.findViewById(R.id.completion_date_info);
            completionDate = view.findViewById(R.id.completion_date);

            advertiserRatingInfo = view.findViewById(R.id.advertiser_rating_info);
            advertiserRate = view.findViewById(R.id.advertiser_rate);
            advertiserLine = view.findViewById(R.id.advertiser_line);
            advertiserComment = view.findViewById(R.id.advertiser_comment);
            
            workerRatingInfo = view.findViewById(R.id.worker_rating_info);
            workerRate = view.findViewById(R.id.worker_rate);
            workerLine = view.findViewById(R.id.worker_line);
            workerComment = view.findViewById(R.id.worker_comment);
        }
    }

    public static class ColorStatus
    {
        private int available;

        private int accepted;
        private int pending;
        private int rejected;

        private int completed;
        private int rated;
        private int canceled;
        private int deleted;

        public ColorStatus() {}

        public ColorStatus(int available, int accepted, int completed, int rated, int canceled, int deleted)
        {
            this.available = available;
            this.accepted = accepted;
            this.completed = completed;
            this.rated = rated;
            this.canceled = canceled;
            this.deleted = deleted;
        }

        public ColorStatus(int available, int accepted, int pending, int rejected, int completed, int rated, int canceled, int deleted)
        {
            this(available, accepted, completed, rated, canceled, deleted);
            this.pending = pending;
            this.rejected = rejected;
        }

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

        public ColorStatus setPending(int pending)
        {
            this.pending = pending;
            return this;
        }

        public ColorStatus setRejected(int rejected)
        {
            this.rejected = rejected;
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
