package com.huewu.lib.pla;
/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this 
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.TreeSet;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * @author huewu.ynag
 * @date 2012-11-06
 */
public class MultiColumnAdapterView extends AdapterView<Adapter> {

	private static final String TAG = "TwoColumnAdapterView";	//constructor.

	/** Distance to drag before we intercept touch events */
	private static final int TOUCH_SCROLL_THRESHOLD = 10;

	/** Children added with this layout mode will be added below the last child */
	private static final int LAYOUT_MODE_BELOW = 0;

	/** Children added with this layout mode will be added above the first child */
	private static final int LAYOUT_MODE_ABOVE = 1;

	/** User is not touching the list */
	private static final int TOUCH_STATE_RESTING = 0;

	/** User is touching the list and right now it's still a "click" */
	private static final int TOUCH_STATE_CLICK = 1;

	/** User is scrolling the list */
	private static final int TOUCH_STATE_SCROLL = 2;

	/** The adapter with all the data */
	private Adapter mAdapter;

	/** Current touch state */
	private int mTouchState = TOUCH_STATE_RESTING;

	/** X-coordinate of the down event */
	private int mTouchStartX;

	/** Y-coordinate of the down event */
	private int mTouchStartY;
	
	/////////////////////////////////////////////////////
	// Column realted fields...
	/////////////////////////////////////////////////////

	private Column[] mColumns = null;

	/** with of one column **/
	private int mColumnWidth = 0;
	
	/** number of columns **/
	private int mColumnCount = 1;

	/**
	 * Constructor
	 * @param context
	 */
	public MultiColumnAdapterView(Context context) {
		super(context);
		init();
	}

	/**
	 * Constructor
	 * @param context
	 */
	public MultiColumnAdapterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Constructor
	 * @param context
	 */
	public MultiColumnAdapterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		//TODO read attributes.
		//# of columns...
		mColumnCount = 2;
		mColumns = new Column[mColumnCount];

		for( int index = 0; index < mColumnCount; ++index ){
			mColumns[index] = new Column(index);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//calculate column width.
		mColumnWidth = getMeasuredWidth() / mColumnCount;
	}

	@Override
	public void setAdapter(final Adapter adapter) {
		mAdapter = adapter;
		removeAllViewsInLayout();
		requestLayout();
	}

	@Override
	public Adapter getAdapter() {
		return mAdapter;
	}

	@Override
	public void setSelection(final int position) {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public View getSelectedView() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public boolean onInterceptTouchEvent(final MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startTouch(event);
			return false;

		case MotionEvent.ACTION_MOVE:
			return startScrollIfNeeded(event);

		default:
			endTouch();
			return false;
		}
	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		if (getChildCount() == 0) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startTouch(event);
			break;

		case MotionEvent.ACTION_MOVE:
			if (mTouchState == TOUCH_STATE_CLICK) {
				startScrollIfNeeded(event);
			}
			if (mTouchState == TOUCH_STATE_SCROLL) {
				scrollList((int)event.getY() - mTouchStartY);
			}
			break;

		case MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_CLICK) {
				// clickChildAt((int)event.getX(), (int)event.getY());
			}
			if (mTouchState == TOUCH_STATE_SCROLL) {
				//TODO some fling effect here...
			}

			endTouch();
			break;

		default:
			endTouch();
			break;
		}
		return true;
	}

	@Override
	protected void onLayout(final boolean changed, final int left, final int top, final int right,
			final int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// if we don't have an adapter, we don't need to do anything
		if (mAdapter == null) {
			return;
		}

		removeNonVisibleViews();
		fillList();

		positionItems();
		invalidate();

		printLog();

	}
	
	/////////////////////////////////////////////////////////////
	//Fill ListView.
	/////////////////////////////////////////////////////////////

	private void fillList() {

		//add child view to viewgroup.
		//question is "how many view should be added..."
		//get last item postiion.

		fillDown();
		fillUp();
	}

	private void fillUp() {
		
		int firstItemPos = getFirstItemPostion() - 1;
		for( int itemPos = firstItemPos; itemPos >= 0; --itemPos){

			Column col = getUpEmptyColumn();
			if( col == null )	//all column is full.
				break;

 			View child = mAdapter.getView(itemPos, getCachedView(), this);
			addAndMeasureChild(child, LAYOUT_MODE_ABOVE);
			col.addFirst(child, itemPos);
		}	
	}

	private void fillDown() {
		
		int lastItemPos = getLastItemPosition() + 1;
		for( int itemPos = lastItemPos; itemPos < mAdapter.getCount(); ++itemPos){

			Column col = getDownEmptyColumn();
			if( col == null )	//all column is full.
				break;

			View child = mAdapter.getView(itemPos, getCachedView(), this);
			addAndMeasureChild(child, LAYOUT_MODE_BELOW);
			col.addLast(child, itemPos);
		}	
	}

	/**
	 * get not full column to insert a child view.
	 * @return not full column or null if all columns are full.
	 */
	private Column getDownEmptyColumn() {
		Column[] cols = sortColumns();
		for( Column c : cols ){
			if( c.isDownFull() == false )
				return c;
		}
		return null;
	}
	
	private Column getUpEmptyColumn() {
		Column[] cols = sortColumns();
		for( Column c : cols ){
			if( c.isUpFull() == false )
				return c;
		}
		return null;
	}
	
	private Comparator<Column> mColHeightComparator = new Comparator<MultiColumnAdapterView.Column>() {
		
		@Override
		public int compare(Column a, Column b) {
			return a.mHeight - b.mHeight;
		}
	};

	private Column[] sortColumns() {
		//FIXME this method is called too frequently...don't need to call every time.
		Column[] cols = mColumns.clone();
		Arrays.sort(cols, mColHeightComparator);
		return cols;
	}

	private int getLastItemPosition() {
		//find largest position between columns...
		int lastPos = Integer.MIN_VALUE;
		for( Column c : mColumns ){
			lastPos = Math.max(lastPos, c.getLastItemPosition());
		}
		
		return lastPos;
	}
	
	private int getFirstItemPostion() {
		//find smallest position between columns...
		int firstPos = Integer.MAX_VALUE;
		for( Column c : mColumns ){
			firstPos = Math.min(firstPos, c.getFirstItemPosition());
		}
		return firstPos;
	}
	
	/**
	 * Adds a view as a child view and takes care of measuring it
	 * 
	 * @param child The view to add
	 * @param layoutMode Either LAYOUT_MODE_ABOVE or LAYOUT_MODE_BELOW
	 */
	private void addAndMeasureChild(final View child, final int layoutMode) {
		LayoutParams params = child.getLayoutParams();
		if (params == null) {
			params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		final int index = layoutMode == LAYOUT_MODE_ABOVE ? 0 : -1;
		addViewInLayout(child, index, params, true);

		final int itemWidth = mColumnWidth;
		child.measure(MeasureSpec.EXACTLY | itemWidth, MeasureSpec.UNSPECIFIED);
	}	
	
	/////////////////////////////////////////////////////////////
	//Remove Non-visible Views.
	/////////////////////////////////////////////////////////////

	/**
	 * Removes view that are outside of the visible part of the list. Will not
	 * remove all views.
	 * 
	 * @param offset Offset of the visible area
	 */
	private void removeNonVisibleViews() {
		for( Column c : mColumns )
			removeNonVisibleViews(c);
	}

	private Rect mParentRect = new Rect();
	private Rect mChildRect = new Rect();

	private void removeNonVisibleViews(Column col) {

		View[] views = col.getViews();
		getDrawingRect(mParentRect);

		for( View v : views){
			if( v == null )
				continue;

			v.getHitRect(mChildRect);
			//find hidden view.
			if( Rect.intersects(mParentRect, mChildRect)  == false){
				addToCache(v);	//do not destry view. we can reuse it!.
				col.removeView(v);
				removeViewInLayout(v);
			}
		}
	}

	/////////////////////////////////////////////////////////////
	//ListView Scroll
	/////////////////////////////////////////////////////////////

	/**
	 * Sets and initializes all things that need to when we start a touch
	 * gesture.
	 * 
	 * @param event The down event
	 */
	private void startTouch(final MotionEvent event) {
		// save the start place
		mTouchStartX = (int)event.getX();
		mTouchStartY = (int)event.getY();

		for( Column c : mColumns )
			c.beginScroll(mTouchStartY);

		// we don't know if it's a click or a scroll yet, but until we know
		// assume it's a click
		mTouchState = TOUCH_STATE_CLICK;
	}

	/**
	 * Resets and recycles all things that need to when we end a touch gesture
	 */
	private void endTouch() {

		// reset touch state
		mTouchState = TOUCH_STATE_RESTING;
	}

	/**
	 * Scrolls the list. Takes care of updating rotation (if enabled) and
	 * snapping
	 * 
	 * @param scrolledDistance The distance to scroll
	 */
	private void scrollList(final int scrolledDistance) {

		for( Column c : mColumns )
			c.scroll(scrolledDistance);

		requestLayout();
	}

	/**
	 * Checks if the user has moved far enough for this to be a scroll and if
	 * so, sets the list in scroll mode
	 * 
	 * @param event The (move) event
	 * @return true if scroll was started, false otherwise
	 */
	private boolean startScrollIfNeeded(final MotionEvent event) {
		final int xPos = (int)event.getX();
		final int yPos = (int)event.getY();
		if (xPos < mTouchStartX - TOUCH_SCROLL_THRESHOLD
				|| xPos > mTouchStartX + TOUCH_SCROLL_THRESHOLD
				|| yPos < mTouchStartY - TOUCH_SCROLL_THRESHOLD
				|| yPos > mTouchStartY + TOUCH_SCROLL_THRESHOLD) {
			// we've moved far enough for this to be a scroll
			mTouchState = TOUCH_STATE_SCROLL;
			return true;
		}
		return false;
	}

	/////////////////////////////////////////////////////////////
	//Column Positioning
	/////////////////////////////////////////////////////////////

	private void positionItems() {
		for(Column c : mColumns)
			positionItems(c);
	}

	/**
	 * Positions the children of each column at the "correct" positions
	 */
	private void positionItems(Column col) {

		View[] views = col.getViews();
		//get col position
		int top = col.getOffset();	//apply scrolled position
		int left = col.getIndex() * mColumnWidth;

		for( View v : views ){
			if( v == null)
				continue;

			int width = v.getMeasuredWidth();
			int height = v.getMeasuredHeight();

			v.layout(left, top, left + width, top + height);
			top += height;
		}
	}

	/////////////////////////////////////////////////////////////
	//View Cache (re-usable item views)
	/////////////////////////////////////////////////////////////

	/** A list of cached (re-usable) item views */
	private final LinkedList<View> mCachedItemViews = new LinkedList<View>();    

	/**
	 * Checks if there is a cached view that can be used
	 * 
	 * @return A cached view or, if none was found, null
	 */
	private View getCachedView() {
		if (mCachedItemViews.size() != 0) {
			return mCachedItemViews.removeFirst();
		}
		//the adapter may create a new view instance if there is no cached view. (convertView)
		return null;
	}

	private void addToCache(View v) {
		mCachedItemViews.addLast(v);
	}

	/////////////////////////////////////////////////////////////
	//DEBUG INFOs.
	/////////////////////////////////////////////////////////////

	private void printLog() {
		Log.v(TAG, "Current Children Count: " + getChildCount());

		for( Column c : mColumns )
			printLog( c );
	}

	private void printLog(Column col) {
		//DEBUG purpose..
		//TODO comment out below code before release.

		Log.v(TAG, "Column Index: " + col.mColumnIndex);
		Log.v(TAG, "Column List Items: " + col.mItemPositions.toString());
		Log.v(TAG, "Column List Top: " + col.getDrawingTop());
		Log.v(TAG, "Column List Offset: " + col.mListOffset);
	}
	
	/////////////////////////////////////////////////////////////
	//Inner Class Column.
	/////////////////////////////////////////////////////////////
	
	private class Column {
		//TODO is it ok to use a some magic number here? (it should not be duplicated...)
		private final static int TAG_KEY = -9812323;

		private int mColumnIndex;

		/** The current top of the first item */
		private int mHeight = 0;
		private int mLastItemPosition = -1;
		
		private ArrayList<View> mViews = new ArrayList<View>();

		//TODO is it ok to use item position info to identify item??
		private TreeSet<Integer> mItemPositions = new TreeSet<Integer>();

		/** the y starting point of this column **/
		private int mListOffset;
		
		/** scrolling is started at this y position. **/
		private int mListTopStart;

		public Column(int index) {
			mColumnIndex = index;
		}

		public int getOffset(){
			return mListOffset;
		}

		public int getIndex() {
			return mColumnIndex;
		}
		
		public int getFirstItemPosition(){
			if(mItemPositions.isEmpty())
				return mLastItemPosition;
			return mItemPositions.first();
		}
		
		public int getLastItemPosition(){
			if(mItemPositions.isEmpty())
				return mLastItemPosition;
			return mItemPositions.last();
		}

		public void addLast(View child, int itemPosition) {
			mHeight += child.getMeasuredHeight();
			child.setTag(TAG_KEY, itemPosition);
			mItemPositions.add(itemPosition);
			mViews.add(child);
		}
		
		public void addFirst(View child, int itemPosition) {
			View first = mViews.get(0);
			int height = child.getMeasuredHeight();

			mHeight += height;
			child.setTag(TAG_KEY, itemPosition);

			//before do layout... add temp layout. (in order to correct top value...)
			child.layout(
					first.getLeft(), first.getTop() - height, first.getRight(), first.getTop() );
			mItemPositions.add(itemPosition);
			mViews.add(0, child);
			
			mListOffset -= height;
			mListTopStart -= height;
		}

		public void removeView(View child) {
			int height = child.getMeasuredHeight();
			
			//recalculate height.
			mHeight -= height;

			//remove view's item position.
			int itemPos = (Integer) child.getTag(TAG_KEY);
			mItemPositions.remove(itemPos);
			
			//check index of view.
			int viewPos = mViews.indexOf(child);
			
			if( viewPos == 0 ){
				//up view is removed..
				mListOffset += height;
				mListTopStart += height;
			}else if(viewPos == mViews.size() - 1){
				//down view is removed..
			}
			
			mViews.remove(child);
			
			if( mItemPositions.isEmpty() ){
				//if this is the last view... let's keep last item position of this column.
				//if not, we will lost the track of columns...
				mLastItemPosition = itemPos; 				
			}
		}

		public View[] getViews(){
			return mViews.toArray(new View[]{});
		}

		public int getDrawingTop(){
			if(mViews == null || mViews.size() == 0)
				return 0;

			return mViews.get(0).getTop();
		}

		public boolean isDownFull(){
			//check bottom of col.
			int top = getDrawingTop();
			return mHeight + top > getHeight();
		}
		
		public boolean isUpFull(){
			//if child view is inserted before doing layout process,
			//there is no proper way to get correct the first child's top position.
			//so, we need offset info.
			return getDrawingTop() < 0;
		}

		public void scroll(int scrolledDistance) {
			mListOffset = mListTopStart + scrolledDistance;
		}

		public void beginScroll(int mTouchStartY) {
			mListTopStart = mListOffset;
		}
	}//end of inner class.
	


}//end of class
