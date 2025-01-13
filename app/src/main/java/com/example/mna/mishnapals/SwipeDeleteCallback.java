package com.example.mna.mishnapals;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private CustomAdapter cAdapter;

    private Drawable icon;
    private ColorDrawable background;
    private boolean completed;

    public SwipeDeleteCallback (CustomAdapter cstadap) {
        super(0, ItemTouchHelper.LEFT);// | ItemTouchHelper.RIGHT);
        cAdapter = cstadap;
        icon = ContextCompat.getDrawable(cAdapter.mReyclerView.getContext(), R.drawable.ic_menu_delete);
        int color = Color.RED;
        //int colorP = ColorUtils.setAlphaComponent(color, 200);
        //background =new ColorDrawable(colorP);
        background =new ColorDrawable(color);
    }

    //took this function code from online
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX,
                dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;

        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
            icon.setBounds(0,0,0,0);
        }

        background.draw(c);
        icon.draw(c);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    //TODO: perhaps give undo option
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(cAdapter.mReyclerView.getContext());
        int position = viewHolder.getAbsoluteAdapterPosition();
        if (!cAdapter.cases.get(position).isFinished()) {
            completed = false;
            builder.setMessage("You have not yet completed this masechta. Are you sure you want to remove it from your account?").setTitle("Confirm Deletion");
        } else {
            completed = true;
            builder.setMessage("This will be removed permanently from your account").setTitle("Confirm Deletion");
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User taps OK button.

                cAdapter.deleteItemFromFirebase(position, completed);
                cAdapter.cases.remove(position);
                cAdapter.notifyItemRemoved(position);
                cAdapter.notifyItemRangeChanged(position, null != cAdapter.cases ? cAdapter.cases.size() : 0);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancels the dialog.
                cAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
            }
        });
        builder.show();



    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return .5f;
    }
}
