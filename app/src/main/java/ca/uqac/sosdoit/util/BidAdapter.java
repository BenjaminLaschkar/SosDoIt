package ca.uqac.sosdoit.util;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.List;

import ca.uqac.sosdoit.R;
import ca.uqac.sosdoit.data.Advert;
import ca.uqac.sosdoit.data.Bid;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder>
{
    private List<Bid> bids;
    private ColorStatus colorStatus;

    public BidAdapter(List<Bid> bids, ColorStatus colorStatus)
    {
        this.bids = bids;
        this.colorStatus = colorStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_bid, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Bid bid = bids.get(position);
        holder.offer.setText(Util.formatCurrency(bid.getOffer()));
        holder.statusBar.setBackgroundColor(colorStatus.getColor(bid.getStatus()));
    }

    @Override
    public int getItemCount()
    {
        return bids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View statusBar;
        private TextView offer;

        private ViewHolder(View view)
        {
            super(view);
            statusBar = view.findViewById(R.id.status_bar);
            offer = view.findViewById(R.id.offer);
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
    }
}
