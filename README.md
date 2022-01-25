# 공부하는습관 
김민채 정지영 조용문
<br>
## 폴더 구조

```
─── main
    ├── java
    │   └── com.gongsp
    │       │   ServerApplication.java  // 메인 실행 파일
    │       ├── api                     // Backend API 구현 Class 집합
    │       │   ├── controller          // REST API 요청 컨트롤러
    │       │   ├── request             // REST API 요청 DTO
    │       │   ├── response            // REST API 응답 DTO
    │       │   └── service             // 비즈니스 로직처리 서비스
    │       ├── common                  // 공용 Class 집합
    │       │   ├── auth
    │       │   │       JwtAuthenticationFilter.java  // 인증을 위한 JWT 필터
    │       │   │       GongUserDetails.java          // 인증을 위한 UserDetails 구현체
    │       │   │       GongUserDetailService.java    // 인증을 위한 UserDateilsService 구현체
    │       │   ├── model.response      
    │       │   │       BaseResponseBody.java         // 공용 Response Body Class
    │       │   └── util
    │       │   │       JwtTokenUtil.java             // JWT 토큰 발급 및 검증 유틸
    │       │   │       ResponseBodyWriteUtil.java    // Response Body 생성 유틸
    │       ├──  config
    │       │       SecurityConfig.java               // Spring Security 설정
    │       └── db
    │           ├── entity
    │           └── repository
    └── resources
            application.properties       // Spring Application 설정 파일
```
