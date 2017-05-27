package valdes.cindyslist.utilities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import valdes.cindyslist.R;

/***************************************************************************************************
 * Class to handle swipe actions within a RecyclerView
 */
public abstract class SwipeUtility extends ItemTouchHelper.SimpleCallback {

    private Drawable background;
    // private Drawable deleteIcon;

    private int xMarkMargin;

    private boolean initiated;
    private Context context;

    private int leftcolorCode;
    private String leftSwipeLable;

    /***********************************************************************************************
     *
     * @param dragDirs
     * @param swipeDirs
     * @param context
     */
    public SwipeUtility(int dragDirs, int swipeDirs, Context context) {
        super(dragDirs, swipeDirs);
        this.context = context;
    }


    /***********************************************************************************************
     *
     */
    private void init() {
        background = new ColorDrawable();
        xMarkMargin = (int) context.getResources().getDimension(R.dimen.ic_clear_margin);
        // deleteIcon = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete);
        // deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }

    /***********************************************************************************************
     * Android method
     *
     * Called when ItemTouchHelper wants to move the dragged item from its old position to the
     * new position
     *
     * @param recyclerView      The RecyclerView to which attached
     * @param viewHolder        The ViewHolder being dragged by the user
     * @param target            The ViewHolder appearing as top view is being dragged
     * @return                  True if viewHolder has been moved
     */
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        return false;
    }

    /***********************************************************************************************
     * Android method
     *
     * Called when a ViewHolder is swiped by the user
     *
     * @param viewHolder        The ViewHolder that has been swiped
     * @param direction         The direction of the swipe
     */
    @Override
    public abstract void onSwiped(RecyclerView.ViewHolder viewHolder, int direction);

    /***********************************************************************************************
     * Android method
     *
     * Returns the swipe direction of the ViewHolder
     *
     * @param recyclerView      The RecyclerView to which attached
     * @param viewHolder        The RecyclerView for which the swipe direction is queried
     * @return                  Binary or direction flags
     */
    @Override
    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        return super.getSwipeDirs(recyclerView, viewHolder);
    }

    /***********************************************************************************************
     * Android method
     *
     * Called by ItemTouchHelper on RecyclerView's onDraw callback
     *
     * @param c                     The canvas which RecyclerView is drawing its children
     * @param recyclerView          The RecyclerView to which attached
     * @param viewHolder            The ViewHolder being interacted
     * @param dX                    The amount of horizontal displacement due to interaction
     * @param dY                    The amount of vertical displacement due to interaction
     * @param actionState           The type of interaction on the View
     * @param isCurrentlyActive     True if being controlled by user, false if returning to
     *                              original state.
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        View itemView = viewHolder.itemView;
        if (!initiated) {
            init();
        }

        int itemHeight = itemView.getBottom() - itemView.getTop();

        //Setting Swipe Background
        ((ColorDrawable) background).setColor(getLeftcolorCode());
        background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);
/*
        int intrinsicWidth = deleteIcon.getIntrinsicWidth();
        int intrinsicHeight = deleteIcon.getIntrinsicWidth();

        int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
        int xMarkRight = itemView.getRight() - xMarkMargin;
        int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int xMarkBottom = xMarkTop + intrinsicHeight;


        //Setting Swipe Icon
        deleteIcon.setBounds(xMarkLeft, xMarkTop + 16, xMarkRight, xMarkBottom);
        deleteIcon.draw(c);
*/
        //Setting Swipe Text
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.CENTER);
        //c.drawText(getLeftSwipeLable(), xMarkLeft + 40, xMarkTop + 10, paint);

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    /***********************************************************************************************
     *
     * @return
     */
    private String getLeftSwipeLable() {
        return leftSwipeLable;
    }

    /***********************************************************************************************
     *
     * @param leftSwipeLable
     */
    public void setLeftSwipeLable(String leftSwipeLable) {
        this.leftSwipeLable = leftSwipeLable;
    }

    /***********************************************************************************************
     *
     * @return
     */
    private int getLeftcolorCode() {
        return leftcolorCode;
    }

    /***********************************************************************************************
     *
     * @param leftcolorCode
     */
    public void setLeftcolorCode(int leftcolorCode) {
        this.leftcolorCode = leftcolorCode;
    }

}
