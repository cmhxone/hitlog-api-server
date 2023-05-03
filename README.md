# ARS 사용이력 저장 API 서버

---

### 빌드

```
$> gradle shadowJar
```

### 실행

실행 시, 아래 리소스 파일이 위치한 경로를 Java 클래스패스에 명시

- [`server.properties`](app/src/main/resources/server.properties)
- [`logback.xml`](app/src/main/resources/logback.xml)

```
$> java -cp=. -jar hitlog-api-server-1.0.0-RELEASE.jar
```
