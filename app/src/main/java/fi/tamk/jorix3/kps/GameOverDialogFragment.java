package fi.tamk.jorix3.kps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * KPS
 *
 * @author Jyri Virtaranta jyri.virtaranta@cs.tamk.fi
 * @version 2018.02.15
 * @since 1.8
 */
public class GameOverDialogFragment extends DialogFragment {
    public interface GameOverDialogListener {
        public void onGameOverClick(DialogFragment dialog, String name);
    }
    
    public GameOverDialogFragment.GameOverDialogListener selectListener;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        try {
            selectListener = (GameOverDialogFragment.GameOverDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement GameOverDialogListener");
        }
    }
    
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        builder.setTitle(R.string.save_score)
                .setView(inflater.inflate(R.layout.input_name, null))
                .setPositiveButton(R.string.submit, (dialog, id) -> {
                    TextView textView = getDialog().findViewById(R.id.username);
                    String name = "Anonymous";
                    
                    if (textView != null) {
                        name = textView.getText().toString();
                    }
                    
                    selectListener.onGameOverClick(GameOverDialogFragment.this, name);
                })
                .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    GameOverDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
