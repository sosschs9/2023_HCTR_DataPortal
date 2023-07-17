<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <link href="https://fonts.googleapis.com/css?family=Nanum+Gothic+Coding:400,700" rel="stylesheet">
    <link rel="stylesheet" href="../../resources/css/test.css?after">
    <title>CrowdQuake Data Portal</title>
</head>
<body>
<!-- header -->
<header>
    <a href="#" class="header-icon"></a>
    <div class="btn-box">
        <ul>
            <li class="login-btn"><a href="/dataPortal/signIn">로그인</a></li>
            <li class="sign-btn"><a href="/dataPortal/signUp">회원가입</a></li>
        </ul>

    </div>
    <nav class="navbar">
        <ul>
            <li><a href="#">정보공유</a></li>
            <li><a href="#">이용안내</a></li>
            <li><a href="#">데이터 활용</a></li>
            <li><a href="#">데이터</a></li>
        </ul>
    </nav>
</header>
<!-- main contents -->
<div class="container">
    <div class="search-bar">
        <input placeholder="검색어를 입력하세요"/>
        <button type="button"></button>
    </div>
    <div class="wrapper">
        <span id="section1">
            <div class="title">
                <h3>지역별 지진 발생 데이터</h3>
            </div>
            <div id="area">
                <span id="ar1">
                    <ul>
                        <li><a href="">서울특별시</a></li>
                        <li><a href="">부산광역시</a></li>
                        <li><a href="">대구광역시</a></li>
                        <li><a href="">인천광역시</a></li>
                        <li><a href="">광주광역시</a></li>
                        <li><a href="">대전광역시</a></li>
                        <li><a href="">울산광역시</a></li>
                        <li><a href="">세종특별자치시</a></li>
                    </ul>
                </span>
                <span id="ar2">
                     <ul>
                        <li><a href="">경기도</a></li>
                        <li><a href="">강원특별자치도</a></li>
                        <li><a href="">충청북도</a></li>
                        <li><a href="">충청남도</a></li>
                        <li><a href="">전라북도</a></li>
                        <li><a href="">전라남도</a></li>
                        <li><a href="">경상북도</a></li>
                        <li><a href="">경상남도</a></li>
                        <li><a href="">제주도</a></li>
                    </ul>
                </span>
            </div>
        </span>
        <span id="section2">
            <div class="title">
                <h3>데이터 시각화</h3>
            </div>
            <div id="map"></div>
        </span>
        <span id="section3">
            <div class="title">
                <h3>이슈 데이터</h3>
            </div>
            <div class="issue-data"></div>
        </span>
        <span id="section4">
            <div class="title">
                <h3>자주 찾는 데이터</h3>
            </div>
            <div class="frequency-data">
                <span class="ranking">
                    <div class="circle">1</div>
                    <div class="ranking-detail">
                    <h2>경남 하동군</h2>
                    <p>Et hendrerit turpis habitant cursus semper ultrices in nunc. Erat eget etiam senectus ....</p>
                    </div>
                </span>
                <span class="ranking">
                    <div class="circle">2</div>
                    <div class="ranking-detail">
                    <h2>경북 포항시 남구</h2>
                    <p>At tristique elementum metus blandit id sed vel eu a. Diam sed feugiat diam nisi est. ....</p>
                    </div>
                </span>
                <span class="ranking">
                    <div class="circle">3</div>
                    <div class="ranking-detail">
                    <h2>경남 함안군</h2>
                    <p>Eu metus, at orci dui sapien. Semper fames est ut gravida. Neque pellentesque risus ut odio.</p>
                    </div>
                </span>
            </div>
        </span>
    </div>
</div>
<!-- footer -->
<footer>
    <nav>
        <div class="footer-container">
            <span>
                <h3>Information</h3>
                <p>Manger: Young-Woo-Kwon</p>
                <p>Deputy Manager: Jangsoo Lee</p>
            </span>
            <span>
                <h3>Contact</h3>
                <p>경북대학교(Kyungpook National University)</p>
                <p>초연결융합기술연구소 (Hyper-connected Convergence Technology and Research)</p>
                <p>ywkwon@knu.ac.kr </p>
            </span>
            <span class="subscribe-box">
                <h3>Subscribe</h3>
                <div class="input-box">
                    <input placeholder="Email address"/>
                    <button type="button">></button>
                </div>
            </span>
        </div>
        <hr>
    </nav>
    <div class="footer-icon"></div>
</footer>

<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=a6ba933008bd1e6586481be7bb5d1237"></script>
<script>
    var container = document.getElementById('map');
    var options = {
        center: new kakao.maps.LatLng(35.8899242, 128.610697),
        level: 5
    };

    var map = new kakao.maps.Map(container, options);
</script>
</body>
</html>

