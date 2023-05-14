
<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/github_username/repo_name">
    <img src="https://x-lines.ru/letters/i/cyrillicfancy/1194/b4b4b6/32/0/e3wsa5mxqjoze3e.png">
  </a>

  <p align="center">
  </p>
</div>


</details>
<details><summary><b>Содержание</b></summary>
  
    1. Краткое описание
    2. Стек-технологий
    3. Функциональности проекта
    4. Схема базы данных
    5. Системные требования
    
</details>


## Краткое описание

Это социальная сеть для оценки фильмов с возможностью добавления пользователей в друзья, отображения рекомендаций по фильмам для просмотра, поиска фильмов по названию / режиссеру. Использованные технологии : Java 11, Maven, Spring-Boot, Lombok, JDBC, H2Database, RestTemplate.

## Стек-технологий

* Система сборки Maven
* Библиотека Lombok
* Spring Boot Framework
* Инструмент тестирования Postman
* SQL
* СУБД PostgreSQL
* СУБД H2 Database
* RestTemplate

## Функциональности проекта

### Функциональности пути /films

**GET /films** получение списка всех фильмов.

**GET /films/{filmId}** получение фильма по filmId.

**GET /films/popular** получение списка самых популярных фильмов.

**POST /films** создание и внесение в БД нового фильма.

**PUT /films** обновление данных фильма.

**PUT /films/{filmId}/like/{userId}** добавление лайка фильму по filmId пользователем с userId.

**DELETE /films** удаление фильма.

**DELETE /films/{filmId}/like/{userId}** удаление лайка фильму по filmId пользователем с userId.

### Функциональности пути /genres

**GET /genres** получение списка всех жанров фильмов.

**GET /genres/{id}** получение жанра фильма по id.

### Функциональности пути /mpa

**GET /mpa** получение списка всех рейтингов фильмов.

**GET /mpa/{id}** получение рейтинга фильма по id.

### Функциональности пути /users

**GET /users** получение списка всех пользователей.

**GET /users/{userId}** получение пользователя по userId.

**GET /users/{userId}/friends** получение списка друзей пользователя с userId.

**GET /users/{userId}/friends/common/{otherId}** получение списка общих друзей пользователя с userId и пользователя с otherId.

**POST /users** создание пользователя.

**PUT /users** обновленние данных пользователя.

**PUT /users/{userId}/friends/{friendId}** добавление пользователя с friendId в друзья пользователя с userId.

**DELETE /users** удаление пользователя.

**DELETE /users/{userId}/friends/{friendId}** удаление пользователя с friendId из друзей пользователя с userId.


## Схема базы данных
![ShareIt Data Base diagram](https://github.com/DmitreeV/java-filmorate/blob/main/images/ER-diagram.png)

## Системные требования

В данном репозитории представлен бэкенд приложения. Работоспособность приложения протестирована, тесты расположены в
папкe: [src/test](./src/test). Также программа протестирована по WEB API с помощью
Postman-тестов, тесты расположены в папке [postman](./postman/).

Приложение работает корректно в текущем виде при наличии:

- установленный [JDK версии 11](https://docs.aws.amazon.com/corretto/),
- сборка с использованием [Maven](https://maven.apache.org/)
