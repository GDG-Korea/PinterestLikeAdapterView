PLA(PinterestLikeAdapterView)
==================================
-

Open source project in order to implement pinterest like list view on android. 
(You can check how pinterest app looks like form below link..)

https://play.google.com/store/apps/details?id=com.pinterest&hl=en

This project is statred based on sony deveoper's blog post 'making your own 3d list'. 

http://developer.sonymobile.com/2010/05/20/android-tutorial-making-your-own-3d-list-part-1/

But, currenty it is implemented based on android framework 2.3's list view source. 
You can check modified list view sources in internal package.

Not supported Features
---------------

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

  3. in option menu, you can add items or lunch pull-to-refresh sample.


*To use Pinterest Like Multi Column View.*

  1. check this project as library project.

  2. MultiColumListView is the view what you need.

*To use pull-to-refresh features.*
  
  1. check this project as library project.

  2. MultiColumnPullToRefreshListView class in extra folder is what you need.

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

Change Log
-----------

**2012.11.7**

http://developer.sonymobile.com/2010/05/20/android-tutorial-making-your-own-3d-list-part-1/

기반으로 러프하게 작성되었습니다. 

**2012.11.8**

패키지 구조가 com.huewu.pla.* 형태로 변경되었습니다.

안드로이드 2.3 버전의 ListView 기반으로 수정되었습니다. (Don't reinvent the wheel)
프레임워크 소스 중 리스트 뷰 관련 파일을 긁어와 많은 Rabbit Hole 을 땜방했습니다. 

결과물은 com.huewu.pla.lib.internal 패키지를 참고하시기 바랍니다.

**2012.11.9**

Android-PullToRefresh 라이브러이와 연동되도록 extra package 를 추가했습니다. 

이 오픈소스 프로젝트를 기반으로, https://github.com/chrisbanes/Android-PullToRefresh 
해당 소스의 일부 내용을 수정 했습니다. (확장을 위한 visiblity)

스크롤 업/다움 시 그리고 어댑터에 아이템 추가 시 컬럼 위치가 변경되는 버그를 수정했습니다.

**2012.11.10**

xml layout 상에서 컬럼 수를 지정할 수 있도록 두 개의 Attribute 를 추가했습니다.

*plaColumnNumber*

*plaLandscapeColumnNumber*

ActionBarSherlock 라이브러리와 함께 사용할 시 발생하는 Attriubte 충돌 문제를 수정했습니다.

**2012.11.12**

Fling 및 Scroll 시 영역 계산이 제대로 이루어지지 않아서 스크롤이 끝까지 이루어지지 않거나, 화면이 떨리는 버그를 수정했습니다.

Override 가능한 함수들의 이름을 이해하기 쉽게(?) 변경했습니다.

**2012.11.13**

Header View 를 정상적으로 지원합니다.

Pull-To-Refresh 기능을 위해, 기존 사용하던 오픈 소스 대신
https://github.com/erikwt/PullToRefresh-ListView
를 사용하도록 수정하였습니다. 다른 프로젝트와 의존성 없이 사용 가능합니다.

**2012.11.14**

하나 이상의 Header View 와 Footer View 를 지원합니다.

**2012.11.22**

첫번째 아이템이 보이는 경우, 각 컬럼의 Top 포지션을 강제로 정렬 시키도록 수정했습니다. 

**2012.11.26**

리스트에 새로운 아이템이 추가되는 경우 Fling 속도가 비정상적으로 빨라지는 버그를 수정했습니다.

**2012.11.28**

Layout 이벤트 시, 아이템의 위치가 변경되는 버그를 수정했습니다.

Item Select 관련된 코드가 제거되었습니다.

TODO
---------------------------------------------
> 1. Fling 및 스크롤바를 지원하지 않습니다.

> 2. Scroll Up / Down 시 재 정렬되는 아이템의 위치가 변경되는 버그가 있습니다.

> 3. 리스트의 마지막 부분에서 스크롤이 자연스럽지 않습니다.

> 4. 어뎁터에 데이터를 추가해도 정상적으로 동작 

> 5. PullToRefresh 라이브러리와 연동.

> 6. Fling 속도가 이상함.

> 7. 복잡한 뷰를 사용하는 경우, 스크롤 할 떄 마다 뷰의 위치가 Y Position 이 변경됩니다.

8. 어댑터에 데이터가 삭제, 변경되거나 중간에 삽입 되는 경우 처리

*그외 아주 많은 문제가 있습니다... 아직 열혈 개발 중 인걸요...  =) 

Contributing
---------------------------------------------
어떤 종류의 지원도 언제나 환영입니다 =)

Any kinds of helps ( bug report / push request / suggestionss ) are welcomed =)

## License

    Copyright 2012 huewu.yang

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [3]: http://cloud.github.com/downloads/huewu/PinterestLikeAdapterView/screenshot.png