Initial Change Log
==================

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

