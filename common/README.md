# vmc-microservice-be
project vmc-common
## Lombok
Cài Lombok plugin cho eclips và IntelliJ

https://www.baeldung.com/lombok-ide

## Quản lí Database Changelog

Khi có bất kì thay đổi cấu trúc trong Database.

Trước khi commit code mới lên, Chạy lệnh:

```
mvn liquibase:diff
```

Sẽ sinh ra file src/main/resources/db/changelog/yyyy_MM_dd_HH_mm.xml

Add file vừa tạo vào trong commit của bạn.

```
git add .
```

## Apply thay đổi DB

Khi pull code mới về, chạy lệnh dưới để đồng bộ Database

```
mvn liquibase:update
```
## Spring fox
Trên môi trường development truy cập địa chỉ http://localhost:8080/swagger-ui/ để truy xuất thông tin của tất cả các api bao gồm url, method, request và response body.
