[Deprecated] PLA(PinterestLikeAdapterView)
==================================

**Deprecated**: This project is not maintaned anymore. Thanks.

[![Build Status](https://travis-ci.org/GDG-Korea/PinterestLikeAdapterView.png?branch=master)](https://travis-ci.org/GDG-Korea/PinterestLikeAdapterView)[![Stories in Ready](https://badge.waffle.io/GDG-Korea/PinterestLikeAdapterView.png?label=ready&title=Ready)](https://waffle.io/GDG-Korea/PinterestLikeAdapterView)

Open source project in order to implement pinterest like list view on android. (You can check how [pinterest app](https://play.google.com/store/apps/details?id=com.pinterest) looks like form)

This project is statred based on Anders Ericson's blog post '[making your own 3d list](http://developer.sonymobile.com/2010/05/20/android-tutorial-making-your-own-3d-list-part-1/)'.

But, currenty it is implemented based on android framework 2.3's list view source. 
You can check modified list view sources in internal package.

Not supported Features
----------------------
* Entry from XML layout.
* Choice Mode & Item Selection.
* Filter
* Handle Key Event & Arrow Scrolling..


Screen Shot
----------------
This is a screen shot of sample activity.

![Example Image][3]

How to use
-------------
*To run Sample App.*

  1. clone project.

  2. run on your android phone.

*To use Pinterest Like Multi Column View.*

  1. check this project as library project.

  2. MultiColumListView is the view what you need.

Attributes
-----------
* **plaColumnNumber**

	Number of column. (default value is 2)

* **plaLandscapeColumnNumber**

	Number of column in landscape mode (the orientation that window's width is longer than height.)

Overridable Methods
--------------------

PLA_ListView was made based on Android 2.3 Framework's ListView, 
and support those protected methods to let a user customize list view's behavior.

	@Override
	protected void onMeasureChild(View child, int position, int widthMeasureSpec, int heightMeasureSpec);	

	@Override
	protected void onItemAddedToList(int position, boolean flow );

	@Override
	protected void onLayoutSync(int syncPos);

	@Override
	protected void onLayoutSyncFinished(int syncPos);	

	@Override
	protected int getFillChildBottom();

	@Override
	protected int getFillChildTop();

	@Override
	protected int getScrollChildBottom(); 

	@Override
	protected int getScrollChildTop();

	@Override
	protected int getItemLeft(int pos);
	
	@Override
	protected int getItemTop( int pos );	

	@Override
	protected int getItemBottom( int pos );

	@Override
	protected void onAdjustChildViews( boolean down );


Contributing
------------
Any kinds of helps (bug report / pull request / suggestions) are welcomed =)

If you are submitting a patch, please add your name to [AUTHORS.md](https://github.com/huewu/PinterestLikeAdapterView/blob/master/AUTHORS.md).

License
-------

    Copyright 2012 PinterestLikeAdapterView authors. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [3]: https://raw.github.com/GDG-Korea/PinterestLikeAdapterView/master/screenshot.png
