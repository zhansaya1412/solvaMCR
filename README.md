# solvaMCR
Это мой микросервис solvaMCR.
## Запуск сервиса
1. Убедитесь, что у вас установлены все необходимые зависимости и библиотеки.
2. Установите PostgreSQL и создайте базу данных `solva_mcr`.
3. Внесите следующие настройки в файл `application.properties`:
spring.application.name=solvaMCR
server.port=8001
spring.datasource.url=jdbc:postgresql://localhost:5432/solva_mcr
spring.datasource.username=postgres
spring.datasource.password=postgres
feign.client.alphaVantageClient.url=https://www.alphavantage.co
CURRENCY_UPDATE_SCHEDULE=0 0 10 * * *
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.schemas=public
4. Скомпилируйте и запустите приложение.
5. API микросервиса будет доступно по адресу `http://localhost:8001/api/client`.
## Зависимости
- **Spring Boot**: Фреймворк для создания микросервисов.
- **Spring Cloud OpenFeign**: Для работы с клиентом Feign.
- **Flyway**: Для управления миграциями базы данных.
- **PostgreSQL JDBC Driver**: Драйвер для подключения к PostgreSQL.
- **Lombok**: Для упрощения написания кода с помощью аннотаций.
- **MapStruct**: Для маппинга объектов.
- **JUnit и Mockito**: Для написания тестов.
  
### DOCKER
Запуск сервиса через docker compose

**Докер файл:** Dockerfile находится в корне проекта. (docker-compose.yml)

**Эндпоинты:**
API Client
- **localhost:8888/api/client/limits:** По данному адресу получаем список лимитов.
- **localhost:8888/api/client/transactions/limit-exceeded:** Получаем список транзакций, превышающих лимит.
- **localhost:8888/api/client/limit:** Устанавливаем новый лимит.

API Transations
- **localhost:8888/api/transactions/save** По данному адресу отправляется новая транзакция в формате JSON:
{
    "accountFrom": "8989898980",
    "accountTo": "8789898900",
    "currencyShortName": "RUB",
    "amount": 67000,
    "dateTime": "2024-04-24T23:11:53",
    "category": "GOODS"
}

API Currency
- **localhost:8888/currency/update-daily-rates** По данному адресу происходит запрос на внешний сервер alphavantage, чтобы получить курс валют. 





