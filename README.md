PLA(PinterestLikeAdapterView) v0.1
==================================
-

안드로이드 핀터레스트 앱과 유사한 멀티 컬럼 형식의 리스트 뷰를 만들기 위한 오픈 소스 프로젝트 입니다.

https://play.google.com/store/apps/details?id=com.pinterest&hl=en

본 프로젝트는 소니 개발자 블로그에서 연재된 다음 포스트를 기반으로 시작되었습니다.

http://developer.sonymobile.com/2010/05/20/android-tutorial-making-your-own-3d-list-part-1/

How it works
-------------
프로젝트를 clone 하시면 하나의 엑티비티와 커스텀 뷰 파일이 있습니다.
어플리케이션을 빌드해서 실행 시켜보시면 컬럼이 두개인 어설픈 리스트 뷰를 확인 하실 수 있습니다.

![Example Image][3]

2 개 이상의 컬럼도 지원합니다. MultiColumnAdapterView 클래스의 init() 함수에서 컬럼 수를 조절 해 보세요.


TODO
---------------------------------------------
1.Fling 및 스크롤바를 지원하지 않습니다.

2.Scroll Up / Down 시 재 정렬되는 아이템의 위치가 변경되는 버그가 있습니다.

3.Adapter View 의 Select Item 기능을 지원하지 않습니다.

4.안드로이드 4.1 호환 기기에서만 테스트 되었습니다.

5.코드가 정리되지 않았습니다. 

6.불필요한 연산이 많습니다. 

7.테스트 코드가 작성되지 않았습니다.

*그외 아주 많은 버그가 있습니다. 아직 버전 0.1 인걸요...  =) 

Contributing
---------------------------------------------
어떤 종류의 지원도 언제나 환영입니다 =)

 [3]: http://cloud.github.com/downloads/huewu/PinterestLikeAdapterView/screenshot.png