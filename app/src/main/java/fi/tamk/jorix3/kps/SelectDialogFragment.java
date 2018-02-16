package fi.tamk.jorix3.kps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.14
 * @since 1.8
 */
public class SelectDialogFragment extends DialogFragment {
    public interface SelectDialogListener {
        public void onSelectClick(DialogFragment dialog, int id);
    }
    
    public SelectDialogListener selectListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
           selectListener = (SelectDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement SelectDialogListener");
        }
    }
    
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose)
                .setItems(R.array.selections, (dialog, id) -> {
                    selectListener.onSelectClick(SelectDialogFragment.this, id);
                });
        return builder.create();
    }
}
