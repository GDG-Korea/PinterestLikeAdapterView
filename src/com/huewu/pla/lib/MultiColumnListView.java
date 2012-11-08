package com.huewu.pla.lib;
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



import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

import com.huewu.pla.lib.internal.PLA_ListView;

/**
 * @author huewu.ynag
 * @date 2012-11-06
 */
public class MultiColumnListView extends PLA_ListView {

	@SuppressWarnings("unused")
	private static final String TAG = "MultiColumnListView";
	
	private int mColumnCount = 2;
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
		init();
	}

	public MultiColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		//TODO read from attribute.
		mColumnCount = 3;
		mColumns = new Column[mColumnCount];
		
		for( int i = 0; i < mColumnCount; ++i )
			mColumns[i] = new Column(i);
	}
	
	///////////////////////////////////////////////////////////////////////
	//Override Methods...
	///////////////////////////////////////////////////////////////////////

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = getMeasuredWidth() / mColumnCount;

		for( int index = 0; index < mColumnCount; ++ index ){
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
		if( childCount < mColumnCount )
			return mColumns[childCount];

		Column result = mColumns[0];
		for( Column c : mColumns ){
			result = result.getTop() > c.getTop() ? c : result;
		}
		return result;
	}

	private Column gettBottomColumn() {
		int childCount = getChildCount();
		if( childCount < mColumnCount )
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
