package fi.tamk.jorix3.kps;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.SortedSet;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.16
 * @since 1.8
 */
public class MyScoreAdapter extends RecyclerView.Adapter<MyScoreAdapter.ViewHolder> {
    private Object[] scores;
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;
        
        public ViewHolder(TextView item) {
            super(item);
            this.item = item;
        }
    }
    
    public MyScoreAdapter(SortedSet<Score> scores) {
        this.scores = scores.toArray();
    }
    
    @Override
    public MyScoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        
        return new ViewHolder(textView);
    }
    
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item.setText(scores[position].toString());
    }
    
    @Override
    public int getItemCount() {
        return scores.length;
    }
}
