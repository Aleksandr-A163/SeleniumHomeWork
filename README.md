# 🧪 Автотесты для OTUS.ru

Этот проект содержит набор автоматизированных тестов для сайта [otus.ru](https://otus.ru), реализованных с использованием **Java 17**, **Selenium WebDriver 4+**, **JUnit 5**, **Google Guice (DI)** и **Gradle**.

---

## 📦 Структура проекта

```
src/
├─ main/java/
│  ├─ components/    # Web‑компоненты (CourseListComponent, CookieBannerComponent и др.)
│  ├─ pages/         # Page Object классы (MainPage, CourseCatalogPage, CoursePage)
│  ├─ driver/        # Фабрика WebDriver (WebDriverProvider, WebDriverFactory)
│  ├─ di/            # Guice‑модуль и расширение (TestModule, GuiceExtension)
│  └─ utils/         # Утилиты (HighlightingListener и др.)
└─ test/java/
   └─ scenarios/     # Тестовые сценарии (CourseSearchTest, CourseDateTest и др.)
```

---

## 🚀 Быстрый старт

1. **Клонировать репозиторий**

   ```bash
   git clone https://github.com/Aleksandr-A163/SeleniumHomeWork
   cd SeleniumHomeWork
   ```

2. **Запустить тесты**

   Запуск всех тестов по умолчанию (браузер Chrome):

   ```bash
   ./gradlew clean test
   ```

   Запуск в конкретном браузере:

   ```bash
   ./gradlew test -Dbrowser=chrome
   ./gradlew test -Dbrowser=firefox
   ./gradlew test -Dbrowser=edge
   ```

3. **Открытие отчёта**

   После завершения выполнения откройте:

   ```
   build/reports/tests/test/index.html
   ```

---

## ⚙️ Конфигурация

- Java 17
- WebDriverManager автоматически загружает драйвера
- Для корректной работы с кириллицей в консоли добавьте:

  ```
  org.gradle.jvmargs=-Dfile.encoding=UTF-8
  ```

  в файл `gradle.properties`.

---

## ✅ Реализованные задачи

- **Dependency Injection** через Google Guice: все компоненты и страницы создаются через `@Inject`
- **JUnit 5 Extension** (`GuiceExtension`) вместо `BaseTest`
- **Подсветка элементов** перед действиями (`HighlightingListener`)
- **Декоратор WebDriver**: `EventFiringDecorator` для логирования и расширения поведения
- **Checkstyle + SpotBugs** подключены через Gradle
- **Page Object + Component Based Design**
- **Сценарии тестирования**:
  - Навигация на случайный курс из фиксированного списка
  - Поиск курса с самой ранней/поздней датой начала (с использованием Stream API и reduce)
  - Переход на случайную категорию из меню «Обучение» и проверка URL

---

## 📌 Параметры запуска

| Параметр              | Описание                           | Пример                        |
|------------------------|------------------------------------|-------------------------------|
| `-Dbrowser=chrome`     | Браузер: chrome, firefox, edge     | `./gradlew test -Dbrowser=firefox` |
| `-Dfile.encoding=UTF-8`| Правильное отображение кириллицы   | в `gradle.properties`         |


---

## 📌 TODO / Планы развития

- [ ] Интеграция с Allure Report
- [ ] Миграция тестов на Cucumber
- [ ] Параллельный запуск тестов
- [ ] Поддержка Docker/CI

---

## 📧 Обратная связь

Автор: Aleksandr Anosov  
GitHub: [Aleksandr-A163](https://github.com/Aleksandr-A163)
