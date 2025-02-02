### 구성
1. SpringBoot3
2. Maven
3. MySQL
4. Mybatis
5. Thymeleaf
6. Tailwind css

### 개발환경 참고사항
1. spring-boot-devtools IDE 환경에 맞게 적용 추천
2. 터미널 src/main/frontend 경로에서 npm run watch 실행 &rarr; 타임리프에 tailwindcss 실시간 적용

### 배포
1. ./mvnw clean package
2. java -jar target/lms-0.0.1-SNAPSHOT.jar

### Docker
1. 아티팩트를 대상으로 수동으로 도커 명령어로 진행 
2. 추후 배포 스크립트 추가 예정