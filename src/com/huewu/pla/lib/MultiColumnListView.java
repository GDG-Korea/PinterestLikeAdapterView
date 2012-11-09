/*******************************************************************************
 * Copyright 2012 huewu.yang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.huewu.pla.lib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import com.huewu.pla.lib.internal.PLA_ListView;
import com.huewu.pla.smaple.R;

/**
 * @author huewu.ynag
 * @date 2012-11-06
 */
public class MultiColumnListView extends PLA_ListView {

	@SuppressWarnings("unused")
	private static final String TAG = "MultiColumnListView";

	private static final int DEFAULT_COLUMN_NUMBER = 2;
	
	private int mColumnNumber = 2;
	private Column[] mColumns = null;
	private SparseIntArray mItems = new SparseIntArray();
	
	private class Column {
		
		private int mIndex;
		private int mColumnWidth;
		private int mColumnLeft;
		private int mSynchedTop = 0;
		private int mSynchedBottom = 0;

		//TODO is it ok to use item position info to identify item??

		public Column(int index) {
			mIndex = index;
		}

		public int getColumnLeft() {
			return mColumnLeft;
		}

		public int getColumnWidth() {
			return mColumnWidth;
		}

		public int getIndex() {
			return mIndex;
		}

		public int getBottom() {
			//find biggest value.
			int bottom = Integer.MIN_VALUE;
			int childCount = getChildCount();

			for( int index = 0; index < childCount; ++index ){
				View v = getChildAt(index);
				if(v.getLeft() != mColumnLeft)
					continue;
				bottom = bottom < v.getBottom() ? v.getBottom() : bottom;
			}
			
			if( bottom == Integer.MIN_VALUE )
				return mSynchedBottom;	//no child for this column..
			return bottom;
		}

		public int getTop() {
			//find smallest value.
			int top = Integer.MAX_VALUE;
			int childCount = getChildCount();
			for( int index = 0; index < childCount; ++index ){
				View v = getChildAt(index);
				if(v.getLeft() != mColumnLeft)
					continue;
				top = top > v.getTop() ? v.getTop() : top;
			}
			
			if( top == Integer.MAX_VALUE )
				return mSynchedTop;	//no child for this column. just return saved synched top..
			return top;
		}

		public void save() {
			mSynchedTop = 0;
			mSynchedBottom = getTop(); //getBottom();
		}

		public void clear() {
			mSynchedTop = 0;
			mSynchedBottom = 0;
		}
	}

	public MultiColumnListView(Context context) {
		super(context);
		init(null);
	}

	public MultiColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs);
	}
	
	private Rect mFrameRect = new Rect();
	private void init(AttributeSet attrs) {
		getWindowVisibleDisplayFrame(mFrameRect);
		
		if( attrs == null ){
			mColumnNumber = DEFAULT_COLUMN_NUMBER; 	//default column number is 2.
		}else{
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinterestLikeAdapterView);
			
			int landColNumber = a.getInteger(R.styleable.PinterestLikeAdapterView_plaLandscapeColumnNumber, -1);
			int defColNumber = a.getInteger(R.styleable.PinterestLikeAdapterView_plaColumnNumber, -1);

			if(mFrameRect.width() > mFrameRect.height() && landColNumber != -1 ){
				mColumnNumber = landColNumber;
			}else if(defColNumber != -1){
				mColumnNumber = defColNumber;
			}else{
				mColumnNumber = DEFAULT_COLUMN_NUMBER;
			}
			a.recycle();
		}

		mColumns = new Column[mColumnNumber];
		for( int i = 0; i < mColumnNumber; ++i )
			mColumns[i] = new Column(i);
	}
	
	///////////////////////////////////////////////////////////////////////
	//Override Methods...
	///////////////////////////////////////////////////////////////////////

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth() / mColumnNumber;

		for( int index = 0; index < mColumnNumber; ++ index ){
			mColumns[index].mColumnWidth = width;
			mColumns[index].mColumnLeft = mListPadding.left + width * index;
		}
	}
	
	@Override
	protected void onMeasureChild(View child, int position, int widthMeasureSpec,
			int heightMeasureSpec) {
		//super.onMeasureChild(child, widthMeasureSpec, heightMeasureSpec);
		child.measure(MeasureSpec.EXACTLY | getColumnWidth(position), heightMeasureSpec);
	}
	
	@Override
	protected void onItemAddedToList(int position, boolean flow ) {
		super.onItemAddedToList(position, flow);
		
		//Column col = getNextColumn(flow);
		Column col = getNextColumn( flow, position );
		mItems.append(position, col.getIndex());
		Log.v("PLA_ListView", String.format("Item [%d] dAdded to Column [%d].", position, col.getIndex()) );
	}
	
	@Override
	protected void onLayoutSync(int syncPos) {
		for( Column c : mColumns ){
			c.save();
		}
	}
	
	@Override
	protected void onLayoutSyncFinished(int syncPos) {
		for( Column c : mColumns ){
			c.clear();
		}
	}
	
	@Override
	protected int getSmallestChildBottom() {
		//return smallest bottom value.
		//in order to determine fill down or not... (calculate below space)
		int result = Integer.MAX_VALUE;
		for(Column c : mColumns){
			int bottom = c.getBottom();
			result = result > bottom ? bottom : result;
		}
		return result;
	}

	@Override
	protected int getChildBottom() {
		//return largest bottom value.
		//for checking scrolling region...
		int result = Integer.MIN_VALUE;
		for(Column c : mColumns){
			int bottom = c.getBottom();
			result = result < bottom ? bottom : result;
		}
		return result;
	}
	
	@Override
	protected int getChildTop() {
		//find largest column.
		int result = Integer.MIN_VALUE;
		for(Column c : mColumns){
			int top = c.getTop();
			result = result < top ? top : result;
		}
		return result;
	}
	
	@Override
	protected int getChildLeft(int pos) {
		return getColumnLeft(pos);
	}
	
	@Override
	protected int getItemTop( int pos ){
		int colIndex = mItems.get(pos, -1);
		if(colIndex == -1)
			return getSmallestChildBottom();

		return mColumns[colIndex].getBottom();
	}
	
	@Override
	protected int getItemBottom( int pos ){
		int colIndex = mItems.get(pos, -1);
		if(colIndex == -1)
			return getChildTop();

		return mColumns[colIndex].getTop();
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//Private Methods...
	//////////////////////////////////////////////////////////////////////////////
	
	//flow If flow is true, align top edge to y. If false, align bottom edge to y.
	private Column getNextColumn(boolean flow, int position) {
		
		//we already have this item...
		int colIndex = mItems.get(position, -1);
		if( colIndex != -1 )
			return mColumns[colIndex];
		
		if( flow ){
			//find column which has the smallest bottom value.
			return gettBottomColumn();
		}else{
			//find column which has the smallest top value.
			return getTopColumn();
		}
	}	
	
	private Column getTopColumn() {
		int childCount = getChildCount();
		if( childCount < mColumnNumber )
			return mColumns[childCount];

		Column result = mColumns[0];
		for( Column c : mColumns ){
			result = result.getTop() > c.getTop() ? c : result;
		}
		return result;
	}

	private Column gettBottomColumn() {
		int childCount = getChildCount();
		if( childCount < mColumnNumber )
			return mColumns[childCount];

		Column result = mColumns[0];
		for( Column c : mColumns ){
			result = result.getBottom() > c.getBottom() ? c : result;
		}
		
		Log.v("Column", "get Shortest Bottom Column: " + result.getIndex());
		return result;
	}	

	private int getColumnLeft(int pos) {
		int colIndex = mItems.get(pos, -1);
		
		if( colIndex == -1 )
			return 0;
		
		return mColumns[colIndex].getColumnLeft();
	}
	
	private int getColumnWidth(int pos) {
		int colIndex = mItems.get(pos, -1 );
		
		if( colIndex == -1 )
			return 0;
		
		return mColumns[colIndex].getColumnWidth();
	}

}//end of class
