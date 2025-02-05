### 구성
1. SpringBoot3
2. Maven
3. MySQL
4. Mybatis
5. Thymeleaf
6. Tailwindcss
7. Alpinejs

### 개발 환경 참고사항
1. node 22 설치
2. 터미널 src/main/frontend 경로에서 npm i 실행 &rarr; node_modules 구성
3. 터미널 src/main/frontend 경로에서 npm run watch 실행 &rarr; 타임리프에 tailwindcss 실시간 적용
4. HTML 탭 간격 2
5. spring-boot-devtools IDE 환경에 맞게 적용 (권장)
6. ui 컴포넌트 www.penguinui.com 참고 (권장)

### 배포
1. ./mvnw clean package
2. java -jar target/lms-0.0.1-SNAPSHOT.jar

### Docker
1. 아티팩트를 대상으로 수동으로 도커 명령어로 진행
2. 추후 배포 스크립트 추가 예정