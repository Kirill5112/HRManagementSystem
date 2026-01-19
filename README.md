## Система управления персоналом и зарплатой на микросервисной архитектуре с использованием Spring Boot, Kafka и PostgreSQL.

Staff Service - управление сотрудниками, их должностями и грейдами

Handbook Service - Справочник должностей, грейдов, налоговых ставок, льготных категорий

Salary Service - Расчет зарплаты с историей

**Инфраструктура**:

- Три экземпляра PostgreSQL: `service1_db`, `service2_db`, `service3_db`.
- Kafka для обмена событиями между сервисами.
- Docker Compose для локального запуска всей системы.

## Локальный запуск без Docker

Необходимое ПО:

- Java 21
- Maven 3.9+
- PostgreSQL 16. (3 базы: `service1_db`, `service2_db`, `service3_db`)
- Kafka/ZooKeeper

1. Создайте базы и пользователей (см. `docker-compose.yml` для логинов/паролей).
2. Настройте `spring.datasource.*` в `application.yml` каждого сервиса под локальный Postgres.
3. Соберите и запустите сервисы:

**Обязательно запускать сервис handbook первым!**
```bash
cd handbook 
mvn spring-boot:run
cd staff 
mvn spring-boot:run
cd calculations 
mvn spring-boot:run
```

## Запуск через Docker

в корне проекта
```bash
mvn clean package -DskipTests
docker compose up --build
```

## Сервисы будут доступны по адресам:

Staff Service: http://localhost:8080

Handbook Service: http://localhost:8081

Calculations Service: http://localhost:8082

**Swagger ui:**

HR Staff: http://localhost:8080/swagger-ui.html

Handbook Service: http://localhost:8081/swagger-ui.html
