# Hướng Dẫn Cài Đặt & Triển Khai Hệ Thống vmc-mes-ms
## 1. Cài đặt các thành phần hệ thống
### 1.1. Database: PostgreSQL
Chạy lệnh sau để khởi tạo container PostgreSQL với user/database mặc định:
``` bash
docker run --name postgres-db -p 5432:5432 -e POSTGRES_USER=vmc_mes -e POSTGRES_PASSWORD=vmc_mes_1234@ -d postgres
```
#### (Tùy chọn) Tạo database, user riêng cho Keycloak:
``` sql
CREATE DATABASE keycloak;
CREATE USER keycloak WITH PASSWORD 'keycloak@123';
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;
```
### 1.2. Redis
Khởi tạo Redis bằng docker-compose:
``` bash
docker-compose -p redis-group up -d
```
**Kết nối CLI:**
``` bash
docker run -it --network some-network --rm redis redis-cli -h redis-server
```
### 1.3. Consul (Service Discovery & Config Key Vault)
``` bash
docker run -d --name consul-server -p 8500:8500 consul:1.15.4
```
Truy cập: [http://localhost:8500](http://localhost:8500)
### 1.4. Keycloak (SSO, AuthZ)
Di chuyển vào thư mục `vmc-mes-ms/mes-setup/keycloak`. Sử dụng file docker-compose có sẵn:
``` bash
docker-compose -p keycloak-group --env-file .env up -d
```
**Truy cập:** [http://localhost:8080](http://localhost:8080)
**Tài khoản mặc định:** admin/password
**Import cấu hình mẫu:**
- Truy cập Keycloak Admin
- Chọn hoặc tạo realm tên: `vmc-mes`
- Import file `vmc-mes-client-keycloak.json` trong thư mục `mes-setup/keycloak`
- (Nhớ định cấu hình DB connection nếu không dùng H2 mặc định)

### 1.5. Cài đặt logstash
- Nếu chỉ muốn quan sát log có được push vào logstash không thì dùng lệnh sau:
  docker run --rm -it \
  -v "$(pwd)/logstash.conf":/usr/share/logstash/pipeline/logstash.conf \
  -p 5000:5000 \
  docker.elastic.co/logstash/logstash:9.0.2
- Khi đó nếu tắt docker thì logstash container cũng bị remove. Nếu muốn cài logstash container lâu dài thì dùng lệnh sau:
  docker run -it \
  --name logstash \
  -v "$(pwd)/logstash.conf":/usr/share/logstash/pipeline/logstash.conf \
  -p 5000:5000 \
  docker.elastic.co/logstash/logstash:9.0.2

## 2. Cấu hình hệ thống
### 2.1. Cấu hình file application cho `api-gateway`
Các file cấu hình nằm tại:
`vmc-mes-ms/mes-api-gateway/src/main/resources/`
- **application.yml**: Cấu hình chung (port, spring application, discovery, security, ...)
- **application-xxx.yml**: Cấu hình riêng môi trường (dev, prod, sit, uat)
- **bootstrap.yml**: Kết nối với Consul/Config Server

**Ví dụ cấu hình tối thiểu với Keycloak**

```
spring:
  security:
    oauth2:
      client:
        provider:
          keycloak:
            issuer-uri: http://localhost:8080/realms/vmc-mes
        registration:
          keycloak:
            provider: keycloak
            client-id: vmc-mes-admin-client
            client-secret: 1zGJS3RaIZpdcEZV4Bmz0Jn86byAfCtS
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:9000/login/oauth2/code/keycloak
            scope:
              - openid
              - email
              - profile
              - roles
      resourceserver:
        jwt:
          # The URI of the OpenID Connect Provider that will issue ID Tokens and Access Tokens
          issuer-uri: http://localhost:8080/realms/vmc-mes

keycloak:
  client:
    ssl-required: none
    resource: vmc-mes-admin-client
    credentials:
      secret: 1zGJS3RaIZpdcEZV4Bmz0Jn86byAfCtS
    public-client: false
    auth-server-url: http://localhost:8080
    realm: vmc-mes
```
- Tham khảo thêm trong các file: `application.yml`, `bootstrap.yml`
## 3. Cấu hình Keycloak (realm, client, scope, permission, policy)
### 3.1. Tạo Realm:
- Truy cập admin console & tạo realm tên `vmc-mes`.

### 3.2. Client
- Loại `confidential`, tạo mới/Import `vmc-mes-client-keycloak.json`.
- Quan trọng: Lấy `client-id`, `client-secret`.

### 3.3. Scope (Role, Quyền)
- Trong Client → Tab "Client Scopes": xác định các scope cho ứng dụng (ví dụ: product.read, product.write,...)

### 3.4. Permission & Policy
- Policy: Định nghĩa điều kiện truy cập dịch vụ (theo role, nhóm, attribute...)
- Permission: Ánh xạ policy vào resource (API, endpoint,...)
- Resource: Định nghĩa tài nguyên (ví dụ: từng API/url...)

**Xem và quản lý các cấu hình này trực tiếp trong tab Authorization của Client trong Keycloak Admin UI**
## 4. Thêm mới một service giống `product-service`
### 4.1. Tạo module mới
- Copy cấu trúc từ `mes-product-service` (hoặc tạo Spring Boot project tương tự)
- Tạo các class, controller, service, repository theo nhu cầu

### 4.2. Cấu hình các file application
- `application.yml`, `application-dev.yml`, `application-prod.yml`, ...
- Chỉnh tên service/id, port, database (nếu cần)
- Kết nối Keycloak, Redis, Consul giống cấu hình mẫu trong `mes-product-service`

**Ví dụ:**
``` yaml
spring:
  application:
    name: new-service
  datasource:
    url: jdbc:postgresql://localhost:5432/vmc_mes
    username: vmc_mes
    password: vmc_mes_1234@
  redis:
    host: localhost
    port: 6379

keycloak:
  realm: vmc-mes
  auth-server-url: http://localhost:8080/
  resource: vmc-mes-client
  credentials:
    secret: <client-secret>
```
### 4.3. Đăng ký service mới với gateway & consul
- Bổ sung route trong `mes-api-gateway/application.yml`
- Service sẽ tự động đăng ký lên Consul nếu cấu hình Spring Cloud Consul phù hợp

### 4.4. Phân quyền API trên Keycloak
- Tạo resource, scope, permission mới cho service vừa thêm
- Cài đặt policy phù hợp để quản lý truy cập các endpoint

### 4.5. Triển khai code mới
``` bash
cd vmc-mes-ms/new-service
mvn clean package
docker build -t new-service .
docker run ...
```
Hoặc dùng docker-compose tùy theo cấu hình tích hợp.
## 5. Một số lưu ý tiện ích
- **Lombok**: Cài plugin cho IDE ([Hướng dẫn Baeldung](https://www.baeldung.com/lombok-ide))
- **Quản lý database migration**: Sử dụng Liquibase
    - Tạo changelog: `mvn liquibase:diff`
    - Sync DB: `mvn liquibase:update`

- **Swagger UI**: 
- Cấu hình ở các service:
``` yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html # Path to access the main Swagger UI page
  api-docs:
    path: /v1/api-docs # Path for the Gateway's own API documentation (if any)
```
- Cấu hình gateway:
``` yaml
springdoc:
  swagger-ui:
    path: /swagger-ui.html # Path to access the main Swagger UI page
    urls:
      # Configuration for aggregating Swagger docs from downstream services
      - name: Product Service # Display name for the service in the Swagger UI dropdown
        url: /api/product-service/v1/api-docs # Path where the Gateway exposes the aggregated docs for this service
  api-docs:
    path: /api-docs # Path for the Gateway's own API documentation (if any)
```
- Bổ sung thêm cấu hình nếu có service mới
- Truy cập link swagger tổng của gateway [http://localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/) ở môi trường development để test API.
